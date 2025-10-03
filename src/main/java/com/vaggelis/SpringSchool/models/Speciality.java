package com.vaggelis.SpringSchool.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "specialities")
public class Speciality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "speciality", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Teacher> teachers;

    public void addTeacher(Teacher teacher){
        this.teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher){
        this.teachers.remove(teacher);
    }

    public Speciality(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
