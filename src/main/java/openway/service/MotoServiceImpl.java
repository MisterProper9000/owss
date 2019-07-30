package openway.service;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import openway.model.Motoroller;
import openway.repository.MotoRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.logging.Logger;

@Service
public class MotoServiceImpl implements MotoService {

    private static Logger logger = Logger.getLogger(MotoServiceImpl.class.getName());

    final private MotoRepository motoRepository;
    final private OrderService orderService;

    final private ClientService clientService;

    public MotoServiceImpl(MotoRepository motoRepository, OrderService orderService, ClientService clientService) {
        this.motoRepository = motoRepository;
        this.orderService = orderService;
        this.clientService = clientService;
    }

    @Override
    public boolean getStatus(int id) {
        Motoroller moto = motoRepository.findMotorollerById(id);
        boolean motoStatus = moto.isStatus();
        return motoStatus;
    }

    @Override
    public void createQrCode(int id) {
        ByteArrayOutputStream bout =
                QRCode.from("sfb_moto:" + id)
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();

        try {
            String currentWorkingDir = System.getProperty("user.dir");
            OutputStream out = new FileOutputStream(currentWorkingDir + "\\src\\main\\resources\\qr_codes\\qr-code" + id + ".png");
            bout.writeTo(out);
            out.flush();
            out.close();
            logger.info("generate Qr-code");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}