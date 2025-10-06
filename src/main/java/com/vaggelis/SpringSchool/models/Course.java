package com.vaggelis.SpringSchool.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate date;
    private int hours;


    @ManyToMany(mappedBy = "courses")
    private List<Student> students;

    public void addStudent(Student student){
        this.students.add(student);
    }

    public void removeStudent(Student student){
        this.students.remove(student);
    }

    public Course(Long id, String name, LocalDate date, int hours) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.hours = hours;
    }
}
