package openway.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import openway.model.Client;
import openway.model.Lesser;
import openway.model.Motoroller;
import openway.model.Order;
import openway.repository.ClientRepository;
import openway.repository.LesserRepository;
import openway.repository.MotoRepository;
import openway.repository.OrderRepository;
import openway.utils.DateUfx;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.lang.Math.*;


@Service
public class OrderServiceImpl implements OrderService {

    final private static Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    final private OrderRepository orderRepository;
    final private ClientRepository clientRepository;
    final private MotoRepository motoRepository;
    final private LesserRepository lesserRepository;

    private float reserveCancelPenalty = 1;
    private int TIME_OUT_RES = 15;


    public OrderServiceImpl(OrderRepository orderRepository,
                            ClientRepository clientRepository,
                            MotoRepository motoRepository, LesserRepository lesserRepository){
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.motoRepository = motoRepository;
        this.lesserRepository = lesserRepository;
    }

    @Override
    public String startRent(String qrEmailTariff) {
        JsonObject jsonObject = new JsonParser().parse(qrEmailTariff).getAsJsonObject();
        String qr = jsonObject.get("qr").getAsString();
        String email = jsonObject.get("email").getAsString();
        UFXService ufxService = new UFXServiceImpl();

        double tariff;
        int period;
        String[] dataOfQrCode;
        int id_moto;
        try{
            tariff = Double.valueOf(jsonObject.get("tariff").getAsString());
            period = Integer.valueOf(jsonObject.get("period").getAsString());
            //qr format: sfb_moto:{id}
            dataOfQrCode = qr.split(":", 2);
            id_moto = Integer.valueOf(dataOfQrCode[1]);
        }catch (Exception e)
        {
            logger.info("start rent error: " + e.toString());
            return String.valueOf(Status.ERROR);
        }

        logger.info("parsed data for start rent: id_moto: " + id_moto
                + ",  email: " + email +
                ", tariff: " + tariff);

        Client client = clientRepository.findClientByEmail(email);
        int id_client = client.getId();

        if ((motoRepository.findMotorollerById(id_moto) != null) && (client.getEmail() != null)) {
            logger.info("called checkQr(): correct qr");

            Order tmp = null;
            int tmpOrderId = 0;
            Motoroller moto = motoRepository.findMotorollerById(id_moto);
            if(moto.isRent())
            {
                return String.valueOf(Status.BLOCKED);
            }

            if(moto.isRes())
            {
                List<Order> orders = orderRepository.findOrdersByCost(-1.0f);
                int i;
                for(i = 0; i < orders.size(); i++){
                    if(orders.get(i).getIdclient() == id_client){
                        tmp = orders.get(i);
                        tmpOrderId = tmp.getId();
                        break;
                    }
                }

                if(i == orders.size()) {
                    logger.info("start rent: last client id: " + i);
                    logger.info("start rent: list of zero costs list size: " + orders.size());
                    logger.info("returned data: " + Status.BLOCKED);
                    return String.valueOf(Status.BLOCKED);
                }

                moto.setStatusReserve(false);
                motoRepository.save(moto);
                String resReverseDeposit = "";
                try {
                    resReverseDeposit = ufxService.ReverseDeposit(id_client, moto.getIdowner(), tmp.getRRN());
                } catch (Exception e){
                    logger.info("start rent error: " + e.toString());
                    return String.valueOf(Status.ERROR);
                }

                logger.info("reverse deposit by starting rent: " + resReverseDeposit);
                logger.info("tmpOrderId: " + tmpOrderId);
                orderRepository.deleteById(tmpOrderId);
            }

            int id_lesser = moto.getIdowner();

            String balanceStr;
            double balance;
            try {
                balanceStr = ufxService.BalanceRequestInWay4(id_client).split(" ")[0];
                balance = Double.valueOf(balanceStr);
            }catch (Exception e){
                logger.info("start rent error: " + e.toString());
                return String.valueOf(Status.ERROR);
            }

            if(balance < ufxService.getDepositSize()){
                logger.info("start rent returned data: " + Status.NOTENOUGH);
                return String.valueOf(Status.NOTENOUGH);
            }

            moto.setStatusRent(true);
            moto.setStatusReserve(false);
            moto.settimeresst(null);
            motoRepository.save(moto);

            String resDeposit = ufxService.GetDepositFromClient(id_client, id_lesser);
            logger.info("deposit getting in way4 by start rent" + resDeposit);
            String begin_time = setCurrentDataToString();
            logger.info("begin_date rent:  " + begin_time);

            String RRN = ufxService.GetRrn();
            Order order = new Order(begin_time, id_moto, id_client, tariff, period, RRN);
            orderRepository.save(order);

            int id_order = order.getId();
            logger.info("startRent() return:" + Status.OK + "|" + id_order);
            //return String.valueOf(Status.OK);
            return Status.OK + "|" + id_order;

        } else {
            logger.info("called checkQr(): incorrect qr");
            return String.valueOf(Status.DOESNTEXIST);
        }
    }

    @Override
    public String motoReserve(String data){
        String args[];
        String moto_idStr;
        int moto_id = 0;
        String email = "";
        Motoroller moto = null;

        try {
            args = data.split("\\|");
            moto_idStr = args[0];
            moto_id = Integer.valueOf(moto_idStr.split(":")[1]);
            email = String.valueOf(args[1]);
            moto = motoRepository.findMotorollerById(moto_id);
        }catch (Exception e){
            logger.info("motoReserve: " + e.toString());
            logger.info("motoReserve returned data: " + Status.OK + "|" + false);
            return Status.OK + "|" + false;
        }


        UFXService ufxService = new UFXServiceImpl();
        if(moto.isRes() || moto.isRent())
        {
            logger.info("motoReserve(service)returned data: " + "OK|" + false);
            return "OK|" + false;
        }

        Client client = clientRepository.findClientByEmail(email);
        if(client == null)
        {
            logger.info("motoReserve(service)returned data: " + "OK|" + false);
            return "OK|" + false;
        }

        String checkBalanceStr = ufxService.BalanceRequestInWay4(client.getId());
        String checkBalance[] = checkBalanceStr.split(" ");
        double balance = Double.valueOf(checkBalance[0]);
        if(balance < ufxService.getDepositSize()){
            logger.info("motoReserve(service)returned data: "
                    + Status.NOTENOUGH + "|" + false);
            return Status.NOTENOUGH + "|" + false;
        }

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String begin_time = format.format(new Date());
        logger.info("start reservation time " + begin_time);
        moto.settimeresst(begin_time);
        moto.setStatusReserve(true);
        motoRepository.save(moto);

        String resGetDep = ufxService.GetDepositFromClient(client.getId(), moto.getIdowner());
        Order order = new Order(begin_time, "null",
                moto_id, client.getId(), 10,1  ,-1, ufxService.GetRrn());
        orderRepository.save(order);

        logger.info("get dep for client by reserve: " + resGetDep);

        return "OK|" + true + "|" + order.getId();
    }

    @Override
    public String motoReserveCanceled(String data){
        String str[];
        int order_id = 0;
        try{
            str = data.split("\\|");
            order_id = Integer.valueOf(str[0]);
        }catch (Exception e){
            logger.info("motoReserveCanc: " + e.toString());
            logger.info("motoReserveCanc: ret data" + "OK|");
            return Status.OK + "|";
        }

        UFXService ufxService = new UFXServiceImpl();
        String email = str[1];

        Order order = orderRepository.findOrderById(order_id);
        Client client = clientRepository.findClientByEmail(email);
        int moto_id = order.getId_moto();
        Motoroller moto = motoRepository.findMotorollerById(moto_id);

        String  RRN = order.getRRN();
        String resReverseDeposit = ufxService.ReverseDeposit(client.getId(), moto.getIdowner(), RRN);
        logger.info("reverse deposit by reserve canceled: " + resReverseDeposit);

        String payRes = ufxService.GetPayment(client.getId(), moto.getIdowner(), reserveCancelPenalty);
        logger.info("get payment for cancelled reserve: " + payRes);

        moto.setStatusReserve(false);
        moto.settimeresst(null);
        motoRepository.save(moto);

        orderRepository.deleteById(order_id);
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
        float timeOfRent = (float) (setStringDateToDate(end_time).getTime() - setStringDateToDate(begin_time).getTime())/1000;
        logger.info("time of Rent: " + timeOfRent);
        float cost = (float)ceil((float)order.getTariff() / order.getTariff_time()) * timeOfRent;
        order.setCost(cost);
        orderRepository.save(order);

        // TODO check here
        UFXService ufxService = new UFXServiceImpl();
        int clientId = order.getIdclient();
        int lesserId = moto.getIdowner();
        String RRN = order.getRRN();

        String resRevDeposit = ufxService.ReverseDeposit(clientId, lesserId, RRN);
        logger.info(resRevDeposit);

        logger.info("cost: " + cost);
        return "OK| " + cost;
    }

    public String reserveTM(int id_moto) {
        UFXService ufxService = new UFXServiceImpl();
        Order tmp = null;

        Motoroller moto = motoRepository.findMotorollerById(id_moto);
        if(!moto.isRes()){
            //logger.info();
            moto.settimeresst(null);
            return "reserveTm: moto with id == " + id_moto + " is not res";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date currentDate = new Date();

        Date dateStart = new Date();
        try {
            dateStart = formatter.parse(moto.gettimeresst());
        } catch (NullPointerException ne) {
            return " timeresst == null value";
        }
        catch (Exception e) {
            return "date parse error (str308)" + e.toString();
        }

        logger.info("now time: " + currentDate.toString());
        logger.info("start date: " + dateStart);

        double difInMil = (double)((double)currentDate.getTime() -
                (double)dateStart.getTime());

        difInMil /= (1000 * 1 );
        //TODO check 240 in time diff
        //long timeDelta = TimeUnit.MINUTES.convert(difInMil, TimeUnit.MILLISECONDS);
        int timeDelta = (int)difInMil;
        logger.info("curent time delta (in sec): " + timeDelta);
        if (timeDelta > TIME_OUT_RES) {
            try {
                //moto.settimeresst(formatter.parse("00-00-0000 00:00").toString());
                moto.settimeresst(null);
                moto.setStatusReserve(false);
            } catch (Exception e) {
                return "time delta error(str318) " + e.toString();
            }
            motoRepository.save(moto);

            int tmpOrderId = 0;
            List<Order> orders = orderRepository.findOrdersByCost(-1.0f);
            int i;
            for (i = 0; i < orders.size(); i++) {
                if (orders.get(i).getId_moto() == moto.getId()) {
                    tmp = orders.get(i);
                    tmpOrderId = tmp.getId();
                    break;
                }
            }
            orderRepository.deleteById(tmpOrderId);

            String resRevDeposit = ufxService.ReverseDeposit(tmp.getIdclient(), moto.getIdowner(), tmp.getRRN());
            logger.info("reverse deposit by timeout of reserve: " + resRevDeposit);
            String resPayment = ufxService.GetPayment(tmp.getIdclient(),
                    moto.getIdowner(),
                    reserveCancelPenalty);
            logger.info("get payment by timeout reservation: " + resPayment);
        }
        return "OK|";
    }

    @Override
    public List<Order> listrentmobile(String email) {
        Client client = null;
        try{
            client = clientRepository.findClientByEmail(email);
        }
        catch (Exception e)
        {
            logger.info("listrentmobile: " + e.toString());
            return null;
        }

        logger.info("orders: " + orderRepository.findOrdersByIdclient(client.getId()));
        List<Order> listorder = orderRepository.findOrdersByIdclient(client.getId());
        List<Order> list = new ArrayList<>();
        for (Order order :listorder) {
            if(order.getCost()>0){
                list.add(order);
            }
        }
        return list;
    }

    public String checkResState(String checkResData){
        String args[];
        int orderId = 0;
        try {
            args = checkResData.split("\\|");
            orderId = Integer.valueOf(args[0]);
        } catch (Exception e){
            logger.info("checkResState: " + e.toString());
            logger.info("checkResState returned data: " + Status.ERROR + "|");
            return Status.ERROR + "|";
        }

        Order order = orderRepository.findOrderById(orderId);
        if(order == null){
            logger.info("checkResState returned data (not find order): "
                    + Status.OK + "|");
            return Status.OK + "|" + false;
        }
        Motoroller moto = motoRepository.findMotorollerById(order.getId_moto());
        if(moto.isRes())
        {
            logger.info("moto with id " + order.getId_moto() + " is res");
            logger.info("checkResState returned data : " + Status.OK + "|" + true);
            return Status.OK + "|" + true;
        }

        return Status.OK + "|" + false;
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