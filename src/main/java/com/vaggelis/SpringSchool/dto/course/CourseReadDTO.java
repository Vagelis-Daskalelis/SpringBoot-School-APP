package com.vaggelis.SpringSchool.dto.course;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseReadDTO {
    private Long id;
    private String name;
    private DayOfWeek dayOfWeek;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
