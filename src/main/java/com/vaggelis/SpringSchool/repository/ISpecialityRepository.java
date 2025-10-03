package com.vaggelis.SpringSchool.repository;

import com.vaggelis.SpringSchool.models.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISpecialityRepository extends JpaRepository<Speciality, Long> {
    Optional<Speciality> findByName(String name);
    Speciality findSpecialityById(Long id);
}
