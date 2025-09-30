package com.vaggelis.SpringSchool.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchRequest {
    @NotNull
    Long id;
    @NotNull
    @Size(min = 3, max = 20)
    private String firstname;
    @NotNull
    @Size(min = 3, max = 20)
    private String lastname;
}
