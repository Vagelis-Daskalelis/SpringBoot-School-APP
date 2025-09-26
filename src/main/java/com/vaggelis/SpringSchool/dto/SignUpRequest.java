package com.vaggelis.SpringSchool.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotNull
    @Size(min = 3, max = 20)
    private String firstname;
    @NotNull
    @Size(min = 3, max = 20)
    private String lastname;
    @NotNull
    @Size(min = 3, max = 20)
    private String username;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Wrong email address")
    @Size(min = 10, max = 20)
    private String email;
    @NotNull
    @Size(min = 3, max = 20)
    private String password;
}
