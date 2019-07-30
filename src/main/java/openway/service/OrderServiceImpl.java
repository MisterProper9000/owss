package openway.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import openway.model.Client;
import openway.model.Order;
import openway.repository.ClientRepository;
import openway.repository.MotoRepository;
import openway.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class OrderServiceImpl implements OrderService {

    final private static Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    final private ClientRepository clientRepository;
    final private OrderRepository orderRepository;
    final private MotoRepository motoRepository;

    @Autowired
    public OrderServiceImpl(ClientRepository clientRepository, OrderRepository orderRepository, MotoRepository motoRepository) {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.motoRepository = motoRepository;
    }

//    @Override
//    public String startRent(String qrAndEmail) {
//        JsonObject jsonObject = new JsonParser().parse(qrAndEmail).getAsJsonObject();
//        String qr = jsonObject.get("qr").getAsString();
//        String email = jsonObject.get("email").getAsString();
//
//        //qr format: sfb_moto:{id}
//        String[] dataOfQrCode = qr.split(":", 2);
//        int id_moto = Integer.valueOf(dataOfQrCode[1]);
//
//        Client client = clientRepository.findClientByEmail(email);
//
//        if ((motoRepository.findMotorollerById(id_moto) != null) && (client.getEmail() != null)) {
//            logger.info("called checkQr(): correct qr");
//
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//            Date begin_time = new Date(System.currentTimeMillis());
//            logger.info("begin_date rent:  " + formatter.format(begin_time));
//
//            int id_client = client.getId();
//            Order order = new Order(begin_time,id_moto,id_client);
//            orderRepository.save(order);
//            int id_order = orderRepository.findOrderByBegin_time(begin_time).getId();
//
//            return Status.OK+"|"+id_order;
//        } else {
//            logger.info("called checkQr(): incorrect qr");
//            return String.valueOf(Status.DOESNTEXIST);
//        }
//    }
//
//    @Override
//    public String endRent(String id_order) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        Date end_time = new Date(System.currentTimeMillis());
//        logger.info("begin_date rent:  " + formatter.format(end_time));
//
//        Order order = orderRepository.findOrderById(Integer.parseInt(id_order));
//        order.setEnd_time(end_time);
//
//        Date begin_time = order.getBegin_time();
//        int calculateTime = Integer.valueOf(String.valueOf(end_time)) - Integer.valueOf(String.valueOf(begin_time));
//        float cost = order.getTariff()*calculateTime;
//        return String.valueOf(cost);
//    }
}
