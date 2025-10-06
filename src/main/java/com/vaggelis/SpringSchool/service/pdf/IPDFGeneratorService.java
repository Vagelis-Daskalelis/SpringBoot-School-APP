package com.vaggelis.SpringSchool.service.pdf;

import com.itextpdf.text.BadElementException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IPDFGeneratorService {
    void allStudents(HttpServletResponse response) throws IOException, BadElementException;
    void allTeachers(HttpServletResponse response) throws IOException, BadElementException;
    void allCoursesYourStudentHas(HttpServletResponse response) throws IOException, BadElementException;
}
