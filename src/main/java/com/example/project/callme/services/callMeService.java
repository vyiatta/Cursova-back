package com.example.project.callme.services;

import com.example.project.callme.data.callMe;
import com.example.project.callme.data.help.callMeStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class callMeService {

    private final callMeRepo callMeRepo;

    public callMeService(com.example.project.callme.services.callMeRepo callMeRepo) {
        this.callMeRepo = callMeRepo;
    }

    public void createANewCall(callMe callMe){
        callMeRepo.save(callMe);
    }
    public List<callMe> findAllActiveCall(){
        return callMeRepo.findAllByCallMeStatusNot(callMeStatus.DONE);
    }

    public void markCallAsDone(Long callMeId) {
        callMe callMe = callMeRepo.findById(callMeId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));

        callMe.setCallMeStatus(callMeStatus.DONE);
        callMeRepo.save(callMe);
    }
}
