package com.vaggelis.SpringSchool.dto.user;

import com.vaggelis.SpringSchool.dto.image.ImageReadDTO;
import com.vaggelis.SpringSchool.models.Role;
import com.vaggelis.SpringSchool.models.Status;
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
    private ImageReadDTO image;
}
