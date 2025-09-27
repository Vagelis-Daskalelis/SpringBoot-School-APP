package com.vaggelis.SpringSchool.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentReadDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private UserReadDTO userReadDTO;
}
