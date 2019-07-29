package openway.service;

import openway.model.Motoroller;
import openway.repository.MotoRepository;
import org.springframework.stereotype.Service;

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
}