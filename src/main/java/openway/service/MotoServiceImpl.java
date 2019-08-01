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
    final private LesserRepository lesserRepository;

    @Autowired
    public MotoServiceImpl(MotoRepository motoRepository, LesserRepository lesserRepository) {
        this.motoRepository = motoRepository;
        this.lesserRepository = lesserRepository;
    }

    @Override
    public boolean getStatus(int id) {
        Motoroller moto = motoRepository.findMotorollerById(id);
        boolean motoStatus = moto.isStatus();
        return motoStatus;
    }

    @Override
    public void createQrCode(int id) {
        String template = "sfb_moto:";
        ByteArrayOutputStream bout =
                QRCode.from(template + id)
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();

        try {
            String currentWorkingDir = System.getProperty("user.dir");
            OutputStream out = new FileOutputStream(currentWorkingDir + "\\src\\main\\resources\\qr_codes\\qr-code" + id + ".png");
            bout.writeTo(out);
            out.flush();
            out.close();
            logger.info("generate Qr-code: "+template+id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addMoto(String moto) {
        logger.info("called addNewLesser()");
        Gson g = new Gson();
        Motoroller motoroller = g.fromJson(moto,Motoroller.class);
        motoRepository.save(motoroller);
        Lesser lesser = lesserRepository.findLesserById(motoroller.getId_owner());
        int currCountMoto = lesser.getSum_moto();
        lesser.setSum_moto(currCountMoto+1);
        lesserRepository.save(lesser);
        createQrCode(motoroller.getId());
        logger.info("save moto to database:" + moto);
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