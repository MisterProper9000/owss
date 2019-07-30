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

    public MotoServiceImpl(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
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
                QRCode.from("https://memorynotfound.com")
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();

        try {
            OutputStream out = new FileOutputStream("/tmp/qr-code.png");
            bout.writeTo(out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}