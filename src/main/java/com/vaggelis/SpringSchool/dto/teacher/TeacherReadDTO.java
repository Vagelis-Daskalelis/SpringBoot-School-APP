package com.vaggelis.SpringSchool.dto.teacher;

import com.vaggelis.SpringSchool.dto.speciality.SpecialityReadDTO;
import com.vaggelis.SpringSchool.dto.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherReadDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private SpecialityReadDTO speciality;
    private UserReadDTO user;
}
