package com.example.project.callme.services;

import com.example.project.callme.data.callMe;
import com.example.project.callme.data.help.callMeStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface callMeRepo extends CrudRepository<callMe,Long> {

    List<callMe> findAllByCallMeStatusNot(callMeStatus callMeStatus);
}
