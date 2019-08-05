package openway.service;

import com.google.gson.Gson;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import openway.model.Lesser;
import openway.model.Motoroller;
import openway.repository.LesserRepository;
import openway.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MotoServiceImpl implements MotoService {

    private static Logger logger = Logger.getLogger(MotoServiceImpl.class.getName());

    final private MotoRepository motoRepository;

    @Autowired
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

    @Override
    public int addMoto(String moto) {
        logger.info("called addNewLesser()");
        Gson g = new Gson();
        Motoroller motoroller = g.fromJson(moto,Motoroller.class);
        motoRepository.save(motoroller);
        logger.info("save moto to database:" + moto);
        return motoroller.getId_owner();
    }

    @Override
    public List<Motoroller> findAll() {
        return motoRepository.findAll();
    }

    @Override
    public List<Integer> listofidmoto() {
        List<Integer> listOfId = new ArrayList<>();
        List<Motoroller> forms = findAll();
        for (Motoroller form : forms) {
            listOfId.add(form.getId());
        }
        int i = listOfId.size();
        logger.info("listOfId.size()=  " + i);
        return listOfId;
    }


}