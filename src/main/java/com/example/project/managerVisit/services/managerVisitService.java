package com.example.project.managerVisit.services;

import com.example.project.callme.data.callMe;
import com.example.project.callme.data.help.callMeStatus;
import com.example.project.managerVisit.data.managerVisit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class managerVisitService {

    private final managerVisitRepo managerVisitRepo;

    public managerVisitService(managerVisitRepo managerVisitRepo) {
        this.managerVisitRepo = managerVisitRepo;
    }

    public void createANewVisit(managerVisit managerVisit){
        managerVisitRepo.save(managerVisit);
    }
    public List<managerVisit> findAllActiveCall(){
        return managerVisitRepo.findAllByCallMeStatusNot(callMeStatus.DONE);
    }

    public void markCallAsDone(Long visitId) {
        managerVisit managerVisit = managerVisitRepo.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));

        managerVisit.setCallMeStatus(callMeStatus.DONE);
        managerVisitRepo.save(managerVisit);
    }
}
