package com.ms1.demo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.print.Doc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cita implements Serializable{
    private String id;
    private Integer valor;
    private PaymentStatus paymentStatus;
    private CitaEstado status;
    private LocalDateTime creationDateTime;
    private LocalDateTime fechaCita;
    private Doctor doctor;
    private Patient patient;
}
