package com.ms1.demo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MainController {

    private List<Cita> citas = new ArrayList<>();
    private List<Doctor> doctors = new ArrayList<>();

    @Autowired
    private final ObjectMapper objectMapper;

    private void LoadDoctors() throws IOException, StreamReadException, DatabindException{
        ClassPathResource resource = new ClassPathResource("static/doctors.json");
        File file = resource.getFile();
        doctors = objectMapper.readValue(file, new TypeReference<List<Doctor>>(){});
        for (Doctor doctor : doctors){
            List<LocalDateTime> availability = new ArrayList<>();
            for (int i = 0; i < 3; i++){
                var now = LocalDateTime.now();
                now.plusDays(1);
                now.plusHours(i);
                availability.add(now);
            }
            doctor.setAvailability(availability);
        }
    }

    public MainController() throws StreamReadException, DatabindException, IOException{
        this.objectMapper = new ObjectMapper();
        try{
            LoadDoctors();
        }
        catch(Exception e){
            log.info(e.getMessage());
        }
    }

    @GetMapping("/citas")
    public ResponseEntity<List<Cita>> getCitas(){
        if (citas.size() < 1){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getDoctors(){
        if (doctors.size() < 1){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PostMapping("/citas")
    public ResponseEntity<Cita> createCita(@RequestBody Request request){
        if(Objects.isNull(request)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Cita cita = new Cita();
        for (Doctor doctor : doctors){
            if(doctor.getId().equals(request.getDoctorId())){
                cita.setDoctor(doctor);
                cita.setFechaCita(doctor.getAvailability().get(request.getAvailableIndex()));
            }
        }
        cita.setPatient(request.getPatient());
        cita.setCreationDateTime(LocalDateTime.now());
        cita.setValor(1000);
        cita.setPaymentStatus(PaymentStatus.PAYED);
        cita.setStatus(CitaEstado.PENDING);
        cita.setId(Integer.toString(citas.size()+1));
        citas.add(cita);
        return new ResponseEntity<Cita>(cita, HttpStatus.OK);
    }

    @PostMapping("/citas/cancel/{id}")
    public ResponseEntity<Cita> cancel(@PathVariable String id){
        if(Objects.isNull(id) || id.length() < 1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Cita cita = null;
        for (Cita c : citas){
            c.setStatus(CitaEstado.CANCELLED);
            c.setPaymentStatus(PaymentStatus.REIMBURSED);
            cita = c;
        }
        if (Objects.isNull(cita)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cita>(cita, HttpStatus.OK);
    }

}
