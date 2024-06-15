package com.example.project.managerVisit.services;

import com.example.project.callme.data.help.callMeStatus;
import com.example.project.managerVisit.data.managerVisit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface managerVisitRepo extends CrudRepository<managerVisit,Long> {

    List<managerVisit> findAllByCallMeStatusNot(callMeStatus callMeStatus);
}
