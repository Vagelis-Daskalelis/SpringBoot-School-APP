package com.vaggelis.SpringSchool.controller;

import com.itextpdf.text.BadElementException;
import com.vaggelis.SpringSchool.service.pdf.IPDFGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pdf")
public class PDFGeneratorController {

    private final IPDFGeneratorService pdfGeneratorService;

    @GetMapping("/generate")
    public void generatePDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.export(response);
    }

    @GetMapping("/students")
    public void StudentsPDF(HttpServletResponse response) throws IOException{
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.allStudents(response);
    }

    @GetMapping("/students/images")
    public void StudentsImagePDF(HttpServletResponse response) throws IOException, BadElementException {
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.allStudentsWithImage(response);
    }

    @GetMapping("/teachers/images")
    public void TeachersImagePDF(HttpServletResponse response) throws IOException, BadElementException {
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.allTeachersWithImage(response);
    }
}
