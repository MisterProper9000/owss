package openway.service;

import com.google.gson.Gson;
import openway.model.Lesser;
import openway.model.Login;
import openway.repository.LesserRepository;
import openway.utils.HashUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class LesserServiceImpl implements LesserService {
    private final static Logger logger = Logger.getLogger(LesserServiceImpl.class.getName());

    final private LesserRepository lesserRepository;

    public LesserServiceImpl(LesserRepository lesserRepository) {
        this.lesserRepository = lesserRepository;
    }

    @Override
    public void addNewLesser(String newLesser) {
        logger.info("called addNewLesser()");
        Gson g = new Gson();

        Lesser lesser = g.fromJson(newLesser, Lesser.class);

        //setPasswordHash(lesser);
        lesserRepository.save(lesser);
        logger.info("save to database:" + lesser);
    }

    @Override
    public boolean authentication(String auth) {
        logger.info("called authentication()" + auth);
        Gson g = new Gson();
        Login lesser = g.fromJson(auth, Login.class);

        Lesser lesserInDB = lesserRepository.findLesserByEmail(lesser.getEmail());
        try {
            if (HashUtil.check(lesserInDB.getPassword(),(lesser.getPassword())))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("checked entered data from start page");
        return false;
    }

    //Setting password hash
    @Override
    public void setPasswordHash() {
        Lesser lesser = lesserRepository.findLesserByEmail("kate@gmail.com");
        logger.info("info about lesser"+lesser);
        try{
            logger.info("old password:   "+lesser.getPassword());
            lesser.setPassword(HashUtil.getSaltedHash(lesser.getPassword()));
            logger.info("hash pasword:  "+lesser.getPassword());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
