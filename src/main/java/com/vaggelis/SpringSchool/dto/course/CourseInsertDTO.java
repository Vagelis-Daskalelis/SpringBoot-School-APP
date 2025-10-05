package com.vaggelis.SpringSchool.dto.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseInsertDTO {
    @NotNull
    @Size(max = 20)
    private String name;
    @NotNull
    @Schema(
            description = "Start date of the course (Europe/Athens timezone, format: d/M/yyyy)",
            example = "5/10/2025"
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/M/yyyy")
    private LocalDate date;
    @NotNull
    private int hours;
}
