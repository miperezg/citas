package com.ms1.demo;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Request implements Serializable{
    private Patient patient;
    private Integer doctorId;
    private Integer availableIndex; 
}
