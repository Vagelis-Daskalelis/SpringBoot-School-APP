package com.vaggelis.SpringSchool.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "week_day")
    private DayOfWeek dayOfWeek;
    @Column(name = "start_date")
    private LocalDateTime startDateTime;
    @Column(name = "end_date")
    private LocalDateTime endDateTime;


    @ManyToMany(mappedBy = "courses")
    private List<Student> students;

    public void addStudent(Student student){
        this.students.add(student);
    }

    public void removeStudent(Student student){
        this.students.remove(student);
    }

    public Course(Long id, String name, DayOfWeek dayOfWeek, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.id = id;
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
