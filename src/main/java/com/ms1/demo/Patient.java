package com.ms1.demo;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Patient implements Serializable{
    private Integer id;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
}
