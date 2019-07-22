package openway.service;

import com.google.gson.Gson;
import openway.model.Lesser;
import openway.repository.LesserRepository;
import org.springframework.stereotype.Service;

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
}
