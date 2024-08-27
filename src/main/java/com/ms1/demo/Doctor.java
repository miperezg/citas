package com.ms1.demo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Doctor implements Serializable{
    private Integer id;
    private String name;
    private String lastName;
    private String email;  
    private String phoneNumber;
    private MedicalServiceEnum profession; 
    private List<LocalDateTime> availability;
}
