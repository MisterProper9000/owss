package openway.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import openway.model.Client;
import openway.model.Motoroller;
import openway.model.Order;
import openway.repository.ClientRepository;
import openway.repository.MotoRepository;
import openway.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;


@Service
public class OrderServiceImpl implements OrderService {

    final private static Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    final private OrderRepository orderRepository;
    final private ClientRepository clientRepository;
    final private MotoRepository motoRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ClientRepository clientRepository, MotoRepository motoRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.motoRepository = motoRepository;
    }

    @Override
    public String startRent(String qrEmailTariff) {
        JsonObject jsonObject = new JsonParser().parse(qrEmailTariff).getAsJsonObject();
        String qr = jsonObject.get("qr").getAsString();
        String email = jsonObject.get("email").getAsString();
        int tariff = Integer.valueOf(jsonObject.get("tariff").getAsString());

        //qr format: sfb_moto:{id}
        String[] dataOfQrCode = qr.split(":", 2);
        int id_moto = Integer.valueOf(dataOfQrCode[1]);

        logger.info("id_moto: " + id_moto + ",  email: " + email+", tariff: "+tariff);

        Client client = clientRepository.findClientByEmail(email);
        int id_client = client.getId();

        if ((motoRepository.findMotorollerById(id_moto) != null) && (client.getEmail() != null)) {
            logger.info("called checkQr(): correct qr");

            String begin_time = setCurrentDataToString();
            logger.info("begin_date rent:  " + begin_time);

            Order order = new Order(begin_time, id_moto, id_client,tariff);
            orderRepository.save(order);

            int id_order = order.getId();
            Motoroller moto = motoRepository.findMotorollerById(id_moto);
            moto.setStatus(true);
            motoRepository.save(moto);

            logger.info("startRent() return:" + Status.OK + "|" + id_order);

            return Status.OK + "|" + id_order;
        } else {
            logger.info("called checkQr(): incorrect qr");
            return String.valueOf(Status.DOESNTEXIST);
        }
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
                moto.setStatus(false);
                motoRepository.save(moto);
            }
            String begin_time = order.getBegin_time();
        float timeOfRent = (float) (setStringDateToDate(end_time).getTime()-setStringDateToDate(begin_time).getTime())/10000;
        logger.info("time of Rent: "+timeOfRent);
        float cost = order.getTariff() * timeOfRent;
        order.setCost(cost);
        orderRepository.save(order);
        logger.info("cost: " + cost);
        return "OK|"+cost;
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