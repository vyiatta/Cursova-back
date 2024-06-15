package com.example.project.callme.data;

import com.example.project.callme.data.help.callMeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="callMe")
public class callMe {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "callMeId")
    private Long callMeId;

    private String name;
    private String phoneNumber;
    private callMeStatus callMeStatus;

}
