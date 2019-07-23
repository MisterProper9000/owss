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


        lesserRepository.save(lesser);
        logger.info("save to database:" + lesser);
    }

    @Override
    public boolean authentication(String auth) {
        logger.info("called authentication()" + auth);
        Gson g = new Gson();
        Login lesser = g.fromJson(auth, Login.class);
        logger.info("infoooo" + lesser.getEmail() + lesser.getPassword());

        Lesser lesserInDB = lesserRepository.findLesserByEmail(lesser.getEmail());
        logger.info("lesserInDB" + lesserInDB);
        try {
            if (lesserInDB.getPassword().equals(lesser.getPassword()))
                return true;
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info(lesser.getEmail() + lesser.getPassword());
            logger.info(lesserInDB.getEmail() + lesserInDB.getPassword());
        }
        logger.info("check entered data from start page");
        return false;
    }
}
