package com.example.project.managerVisit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.project.managerVisit.data.managerVisit;
import com.example.project.managerVisit.services.managerVisitService;

import java.util.List;

@RestController
@RequestMapping("/manager-visit")
public class ManagerVisitController {

    private final managerVisitService visitService;

    @Autowired
    public ManagerVisitController(managerVisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNewVisit(@RequestBody managerVisit visit) {
        visitService.createANewVisit(visit);
        return ResponseEntity.ok("New visit created");
    }

    @GetMapping("/active-visits")
    public ResponseEntity<List<managerVisit>> getAllActiveVisits() {
        List<managerVisit> activeVisits = visitService.findAllActiveCall();
        return ResponseEntity.ok(activeVisits);
    }

    @PutMapping("/mark-done/{visitId}")
    public ResponseEntity<String> markVisitAsDone(@PathVariable Long visitId) {
        visitService.markCallAsDone(visitId);
        return ResponseEntity.ok("Visit marked as done");
    }
}
