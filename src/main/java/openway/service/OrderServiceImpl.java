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
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class OrderServiceImpl implements OrderService {

    final private static Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    final private OrderRepository orderRepository;
    final private ClientRepository clientRepository;
    final private MotoRepository motoRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ClientRepository clientRepository,
                            MotoRepository motoRepository){
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.motoRepository = motoRepository;
    }


    @Override
    public String startRent(String qrAndEmail) {
        JsonObject jsonObject = new JsonParser().parse(qrAndEmail).getAsJsonObject();
        String qr = jsonObject.get("qr").getAsString();
        String email = jsonObject.get("email").getAsString();
        UFXService ufxService = new UFXServiceImpl();


        //qr format: sfb_moto:{id}
        String[] dataOfQrCode = qr.split(":", 2);
        int id_moto = Integer.valueOf(dataOfQrCode[1]);


        logger.info("id_moto: " + id_moto + ",  email: " + email);

        Client client = clientRepository.findClientByEmail(email);
        int id_client = client.getId();

        logger.info("client: " + client);

        if ((motoRepository.findMotorollerById(id_moto) != null) && (client.getEmail() != null)) {
            logger.info("called checkQr(): correct qr");

            Motoroller moto = motoRepository.findMotorollerById(id_moto);
            moto.setStatus(true);
            motoRepository.save(moto);
            int id_lesser = moto.getId_owner();
            String resDeposit = ufxService.GetDepositFromClient(id_client, id_lesser);

            logger.info("deposit getting in way4" + resDeposit);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date begin_time = new Date(System.currentTimeMillis());
            logger.info("begin_date rent:  " + formatter.format(begin_time));
            logger.info("begin_date rent:  " + begin_time);


            Order order = new Order(String.valueOf(begin_time), id_moto, id_client);
            logger.info("order save: " + orderRepository.save(order));
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
    public String endRent(String id_orderStr) {
        int id_order = Integer.parseInt(id_orderStr);
        logger.info("id_order"+id_order);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date end_time = new Date(System.currentTimeMillis());
        logger.info("end_date rent:  " + formatter.format(end_time));
        logger.info("end_time: " + end_time);


        Order order = orderRepository.findOrderById(id_order);
        logger.info("order: " + order);
        if (order != null) {
            order.setEnd_time(String.valueOf(end_time));
            orderRepository.save(order);
            Motoroller moto = motoRepository.findMotorollerById(order.getId_moto());
            if(moto != null){
                moto.setStatus(false);
                motoRepository.save(moto);
            }
        }

        //String begin_time = order.getBegin_time();
        //int calculateTime = Integer.valueOf(String.valueOf(end_time)) - Integer.valueOf(String.valueOf(begin_time));
        //logger.info("begin_time: "+formatter.format(begin_time));
        //logger.info("begin_time: "+begin_time);

//        //float cost = order.getTariff()*calculateTime;
//        float cost = (float) order.getTariff()*200;
//        //order.setCost(cost);
//        logger.info("cost: "+cost);
//        return String.valueOf(cost);
        return "OK|200";
    }
}
