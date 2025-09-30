package com.vaggelis.SpringSchool.repository;

import com.vaggelis.SpringSchool.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepository extends JpaRepository<Image, Long> {
}
