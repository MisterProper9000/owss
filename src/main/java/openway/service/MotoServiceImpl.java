package openway.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import openway.model.Motoroller;
import openway.repository.MotoRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class MotoServiceImpl implements MotoService {
    private static Logger logger = Logger.getLogger(MotoServiceImpl.class.getName());

    final private MotoRepository motoRepository;

    final private ClientService clientService;

    public MotoServiceImpl(MotoRepository motoRepository, ClientService clientService) {
        this.motoRepository = motoRepository;
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String checkQr(String qrAndEmail) {
        JsonObject jsonObject = new JsonParser().parse(qrAndEmail).getAsJsonObject();
        String qr = jsonObject.get("qr").getAsString();
        String email = jsonObject.get("email").getAsString();

        //qr format: sfb_moto:{id}
        String[] dataOfQrCode = qr.split(":", 2);
        int id = Integer.valueOf(dataOfQrCode[1]);

        if ((motoRepository.findMotorollerById(id) != null) && (clientService.isEmailOfClientExist(email))) {
            logger.info("called checkQr(): correct qr");
            return String.valueOf(Status.OK);
        } else {
            logger.info("called checkQr(): incorrect qr");
            return String.valueOf(Status.DOESNTEXIST);
        }
    }
}