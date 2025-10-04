package com.vaggelis.SpringSchool.dto.speciality;

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
public class SpecialityUpdateDTO {
    @NotNull
    private Long id;
    @NotNull
    @Size(max = 20)
    private String name;
}
