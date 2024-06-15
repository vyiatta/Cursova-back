package com.example.project.callme;

import com.example.project.callme.data.callMe;
import com.example.project.callme.services.callMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/call-me")
public class CallMeController {

    private final callMeService callMeService;

    @Autowired
    public CallMeController(callMeService callMeService) {
        this.callMeService = callMeService;
    }

    @GetMapping("/active-calls")
    public ResponseEntity<List<callMe>> getAllActiveCalls() {
        List<callMe> activeCalls = callMeService.findAllActiveCall();
        return ResponseEntity.ok(activeCalls);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createNewCall(@RequestBody callMe callMe) {
        callMeService.createANewCall(callMe);
        return ResponseEntity.ok("New call created successfully");
    }

    @PutMapping("/mark-done/{callMeId}")
    public ResponseEntity<String> markCallAsDone(@PathVariable Long callMeId) {
        callMeService.markCallAsDone(callMeId);
        return ResponseEntity.ok("Call marked as done");
    }
}
