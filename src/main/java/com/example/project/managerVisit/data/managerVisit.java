package com.example.project.managerVisit.data;

import com.example.project.callme.data.help.callMeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="managerVisit")
public class managerVisit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "managerVisitId")
    private Long managerVisitId;

    private String name;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfVisit;
    private callMeStatus callMeStatus;
}
