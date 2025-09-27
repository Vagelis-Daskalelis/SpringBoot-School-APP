package com.vaggelis.SpringSchool.dto;

import com.vaggelis.SpringSchool.models.Role;
import com.vaggelis.SpringSchool.models.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserReadDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private Status status;
}
