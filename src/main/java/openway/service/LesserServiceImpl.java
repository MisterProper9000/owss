package openway.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import openway.model.Lesser;
import openway.model.Login;
import openway.repository.LesserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        lesser.setSum_moto(0);
        lesserRepository.save(lesser);
        logger.info("save to database:" + lesser);

        // TODO check result
        //  it's == OK or ERROR
        String resWay4 = ufxService.AddNewLesserInWay4(lesser);
        String checkResWay4 = ufxService.CheckRes(resWay4);
        logger.info("saved way4: " + checkResWay4);

    }

    @Override
    public String authentication(String auth) {
        logger.info("called authentication()" + auth);
        Gson g = new Gson();
        Login lesser = g.fromJson(auth, Login.class);
        Lesser lesserInDB = lesserRepository.findLesserByEmail(lesser.getEmail());
        logger.info("lesserInDB: " + lesserInDB);
        try {
            if (lesserInDB.getPassword().equals(lesser.getPassword()) && lesserInDB.getEmail().equals(lesser.getEmail())) {
                logger.info("checked entered data from start page");
                String s = String.valueOf(lesserInDB.getId());
                logger.info("auth id_lessor: " + s);
                return s;
            } else {
                logger.info("error with login or password");
                return "false";
            }
        } catch (NullPointerException e) {
            logger.info("error with login or password");
            return "false";
        } catch (Exception e) {
            return "false";
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

    @Override
    public List<String> getNameSerName(String id) {
        JsonObject jsonObject = new JsonParser().parse(id).getAsJsonObject();
        int id_lessor = jsonObject.get("id").getAsInt();
        Lesser lesser = lesserRepository.findLesserById(id_lessor);
        List<String> list = new ArrayList<>();
        list.add(lesser.getFirst_name());
        list.add(lesser.getLast_name());
        return list;
    }

    @Override
    public String checkBalanceLessor(String data){
        logger.info("called check balance" + data);
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        String email = jsonObject.get("email").getAsString();
        int lesserId = lesserRepository.findLesserByEmail(email).getId();
        logger.info("lesserId:"+lesserId);

        UFXService ufxService = new UFXServiceImpl();
        String balance = ufxService.BalanceLesserRequestInWay4(lesserId);
        JsonObject balanceObj = new JsonObject();
        balanceObj.addProperty("balance",balance);
        logger.info("balance lesser:"+balanceObj);
        return String.valueOf(balanceObj);
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
