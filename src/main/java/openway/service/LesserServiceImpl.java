package openway.service;

import com.google.gson.Gson;
import openway.model.Lesser;
import openway.model.Login;
import openway.repository.LesserRepository;
import openway.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class LesserServiceImpl implements LesserService {
    private final static Logger logger = Logger.getLogger(LesserServiceImpl.class.getName());

    final private LesserRepository lesserRepository;

    @Autowired
    public LesserServiceImpl(LesserRepository lesserRepository) {
        this.lesserRepository = lesserRepository;
    }

    @Override
    public void addNewLesser(String newLesser) {
        logger.info("called addNewLesser()");
        Gson g = new Gson();
        UFXService ufxService = new UFXServiceImpl();

        Lesser lesser = g.fromJson(newLesser, Lesser.class);
        lesserRepository.save(lesser);
        logger.info("save to database:" + lesser);
        String resWay4 = ufxService.AddNewLesserInWay4(lesser);
        logger.info("saved way4: " + resWay4);

    }

    @Override
    public boolean authentication(String auth) {
        logger.info("called authentication()" + auth);
        Gson g = new Gson();
        Login lesser = g.fromJson(auth, Login.class);
        //logger.info("lesser.getmail:   "+lesser.getEmail());
        Lesser lesserInDB = lesserRepository.findLesserByEmail(lesser.getEmail());
        //logger.info("lesserInDB:   "+lesserInDB);
        try {
            if (lesserInDB.getPassword().equals(lesser.getPassword())) {
                logger.info("checked entered data from start page");
                return true;
            } else return false;
        } catch (NullPointerException e) {
            logger.info("error with login or password");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Lesser> findAll() {
        logger.info("find all lessers (called findAll())");
        return lesserRepository.findAll();
    }

    @Override
    public List<Integer> listofidlessers() {
        logger.info("called listidlessers");
        List<Integer> listOfId = new ArrayList<>();
        List<Lesser> forms = findAll();
        for (Lesser form : forms) {
            listOfId.add(form.getId());
        }
        int i = listOfId.size();
        logger.info("listOfId.size()=  " + i);
        return listOfId;
    }


    //Setting password hash
//    @Override
//    public void setPasswordHash() {
//        Lesser lesser = lesserRepository.findLesserByEmail("kate@gmail.com");
//        logger.info("info about lesser"+lesser);
//        try{
//            logger.info("old password:   "+lesser.getPassword());
//            lesser.setPassword(HashUtil.getSaltedHash(lesser.getPassword()));
//            logger.info("hash pasword:  "+lesser.getPassword());
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
