package openway.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import openway.model.Client;
import openway.model.Motoroller;
import openway.model.Order;
import openway.repository.ClientRepository;
import openway.repository.LesserRepository;
import openway.repository.MotoRepository;
import openway.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Math.ceil;


@Service
public class OrderServiceImpl implements OrderService {

    final private static Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    final private OrderRepository orderRepository;
    final private ClientRepository clientRepository;
    final private MotoRepository motoRepository;

    private float reserveCancelPenalty = 1;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ClientRepository clientRepository,
                            MotoRepository motoRepository){
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.motoRepository = motoRepository;
    }

    @Override
    public String startRent(String qrEmailTariff) {
        JsonObject jsonObject = new JsonParser().parse(qrEmailTariff).getAsJsonObject();
        String qr = jsonObject.get("qr").getAsString();
        String email = jsonObject.get("email").getAsString();
        UFXService ufxService = new UFXServiceImpl();

        double tariff = Double.valueOf(jsonObject.get("tariff").getAsString());
        int period = Integer.valueOf(jsonObject.get("period").getAsInt());
        //qr format: sfb_moto:{id}
        String[] dataOfQrCode = qr.split(":", 2);
        int id_moto = Integer.valueOf(dataOfQrCode[1]);

        logger.info("id_moto: " + id_moto + ",  email: " + email+", tariff: "+tariff);

        Client client = clientRepository.findClientByEmail(email);
        int id_client = client.getId();

        String balanceStr = ufxService.BalanceRequestInWay4(id_client).split(" ")[0];
        double balance = Double.valueOf(balanceStr);
        if(balance < ufxService.getDepositSize()){
            return String.valueOf(Status.NOTENOUGH);
        }


        if ((motoRepository.findMotorollerById(id_moto) != null) && (client.getEmail() != null)) {
            logger.info("called checkQr(): correct qr");

            Order tmp = null;
            int tmpOrderId = 0;
            Motoroller moto = motoRepository.findMotorollerById(id_moto);
            if(moto.isRes())
            {
                List<Order> orders = orderRepository.findOrdersByCost(-1.0f);
                int i;
                for(i = 0; i < orders.size(); i++){
                    if(orders.get(i).getId_client() == id_client){
                        tmp = orders.get(i);
                        tmpOrderId = tmp.getId();
                        break;
                    }
                }

                if(i == orders.size()) {
                    logger.info("start rent: last client id" + i);
                    logger.info("start rent: list of zero costs list size" + orders.size());
                    logger.info("returned data: " + String.valueOf(Status.BLOCKED));
                    return String.valueOf(Status.BLOCKED);
                }


                moto.setStatusReserve(false);
                String resReverseDeposit = ufxService.ReverseDeposit(id_client, moto.getId_owner(), tmp.getRRN());

                logger.info("reverse deposit by starting rent: " + resReverseDeposit);
                logger.info("tmpOrderId: " + tmpOrderId);
                orderRepository.deleteById(tmpOrderId);
            }

            moto.setStatusRent(true);
            moto.setStatusReserve(false);
            motoRepository.save(moto);

            int id_lesser = moto.getId_owner();
            String resDeposit = ufxService.GetDepositFromClient(id_client, id_lesser);

            logger.info("deposit getting in way4" + resDeposit);

            String begin_time = setCurrentDataToString();

            logger.info("begin_date rent:  " + begin_time);

            String RRN = ufxService.GetRrn();
            Order order = new Order(begin_time, id_moto, id_client, tariff, period, RRN);
            orderRepository.save(order);

            int id_order = order.getId();


            logger.info("startRent() return:" + Status.OK + "|" + id_order);

            return Status.OK + "|" + id_order;
        } else {
            logger.info("called checkQr(): incorrect qr");
            return String.valueOf(Status.DOESNTEXIST);
        }
    }

    @Override
    public String motoReserve(int moto_id, String email){
        Motoroller moto = motoRepository.findMotorollerById(moto_id);
        UFXService ufxService = new UFXServiceImpl();
        if(moto.isRes() || moto.isRent())
            return String.valueOf(false);

        moto.setStatusReserve(true);
        motoRepository.save(moto);

        String rmail = email.split("\\|")[1];
        logger.info(rmail);
        Client client = clientRepository.findClientByEmail(rmail);
        if(client == null)
            return String.valueOf(false);

        Order order = new Order("null", "null",
                moto_id, client.getId(), 10,1  ,-1, ufxService.GenerateRRN("time"));
        orderRepository.save(order);

        String checkBalance = ufxService.BalanceRequestInWay4(client.getId());
        double balance = Double.valueOf(checkBalance);
        if(balance < ufxService.getDepositSize()){
            return String.valueOf(false);
        }

        String resGetDep = ufxService.GetDepositFromClient(client.getId(), moto.getId_owner());
        logger.info("get dep for client by reserve " + resGetDep);

        return "OK|" + String.valueOf(true) + "|" + order.getId();
    }

    @Override
    public String motoReserveCanceled(int moto_id, String data){
        String str[] = data.split("\\|");
        int order_id = Integer.valueOf(str[0]);
        UFXService ufxService = new UFXServiceImpl();
        String email = str[1];

        Order order = orderRepository.findOrderById(order_id);
        Client client = clientRepository.findClientByEmail(email);
        Motoroller moto = motoRepository.findMotorollerById(moto_id);

        String  RRN = order.getRRN();
        String resReverseDeposit = ufxService.ReverseDeposit(client.getId(), moto.getId_owner(), RRN);
        logger.info("reverse deposit by reserve canceled: " + resReverseDeposit);


        String payRes = ufxService.GetPayment(client.getId(), moto.getId_owner(), reserveCancelPenalty);
        logger.info("get payment for cancelled reserve: " + payRes);

        moto.setStatusReserve(false);
        motoRepository.save(moto);

        return "OK";
    }

    @Override
    public String endRent(String id_orderStr) throws ParseException {
        int id_order = Integer.parseInt(id_orderStr);
        logger.info("id_order"+id_order);

        String end_time = setCurrentDataToString();
        logger.info("end_date rent: " + end_time);

        Order order = orderRepository.findOrderById(id_order);
        logger.info("order: " + order);
        order.setEnd_time(end_time);
        Motoroller moto = motoRepository.findMotorollerById(order.getId_moto());
        if(moto != null){
            moto.setStatusRent(false);
            motoRepository.save(moto);
        }
        String begin_time = order.getBegin_time();
        float timeOfRent = (float) (setStringDateToDate(end_time).getTime() - setStringDateToDate(begin_time).getTime())/10000;
        logger.info("time of Rent: " + timeOfRent);
        float cost = (float)ceil((float)order.getTariff() / order.getTariff_time()) * timeOfRent;
        order.setCost(cost);
        orderRepository.save(order);

        // TODO check here
        UFXService ufxService = new UFXServiceImpl();
        int clientId = order.getId_client();
        int lesserId = moto.getId_owner();
        String RRN = order.getRRN();

        String resRevDeposit = ufxService.ReverseDeposit(clientId, lesserId, RRN);
        logger.info(resRevDeposit);

        logger.info("cost: " + cost);
        return "OK| " + cost;
    }

    public String reserveTM(int id_moto) {
        UFXService ufxService = new UFXServiceImpl();
        Order tmp = null;
        int tmpOrderId = 0;
        Motoroller moto = motoRepository.findMotorollerById(id_moto);
        List<Order> orders = orderRepository.findOrdersByCost(-1.0f);
        int i;
        for (i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId_moto() == id_moto) {
                tmp = orders.get(i);
                tmpOrderId = tmp.getId();
                break;
            }
        }
        ufxService.ReverseDeposit(tmp.getId_client(), moto.getId_owner(), tmp.getRRN());
        ufxService.GetPayment(tmp.getId_client(), moto.getId_owner(), 1);
        return "OK|";
    }


    private String setCurrentDataToString(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date currentDate = new Date();
        return formatter.format(currentDate);
    }

    private Date setStringDateToDate(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return formatter.parse(dateStr);

    }
}