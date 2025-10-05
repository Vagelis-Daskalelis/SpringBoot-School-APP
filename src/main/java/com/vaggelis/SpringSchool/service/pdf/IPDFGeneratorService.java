package com.vaggelis.SpringSchool.service.pdf;

import com.itextpdf.text.BadElementException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IPDFGeneratorService {
    void export(HttpServletResponse response) throws IOException;
    void allStudents(HttpServletResponse response) throws IOException;
    void allStudentsWithImage(HttpServletResponse response) throws IOException, BadElementException;
    void allTeachersWithImage(HttpServletResponse response) throws IOException, BadElementException;
    void allCoursesYourStudentHas(HttpServletResponse response) throws IOException, BadElementException;
}
