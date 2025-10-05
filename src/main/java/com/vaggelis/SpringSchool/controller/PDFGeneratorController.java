package com.vaggelis.SpringSchool.controller;

import com.itextpdf.text.BadElementException;
import com.vaggelis.SpringSchool.service.pdf.IPDFGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.allStudents(response);
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Generate PDF of students with images (ADMIN, TEACHER)",
            description = "Creates a PDF containing all students, including their images, " +
                    "and sends it as a file download in the HTTP response."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF successfully generated and sent"),
            @ApiResponse(responseCode = "500", description = "Error occurred while generating the PDF")
    })
    @GetMapping("/students/images")
    public void StudentsImagePDF(HttpServletResponse response) throws IOException, BadElementException {
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.allStudentsWithImage(response);
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Generate PDF of teachers with images (ADMIN, TEACHER)",
            description = "Creates a PDF containing all teachers, including their images, " +
                    "and sends it as a file download in the HTTP response."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF successfully generated and sent"),
            @ApiResponse(responseCode = "500", description = "Error occurred while generating the PDF")
    })
    @GetMapping("/teachers/images")
    public void TeachersImagePDF(HttpServletResponse response) throws IOException, BadElementException {
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.allTeachersWithImage(response);
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }


    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Generate PDF of all courses for the logged-in student (STUDENT)",
            description = "Creates and returns a PDF file listing all courses associated with the currently authenticated student. " +
                    "The PDF is returned as an attachment in the HTTP response."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF generated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - the user is not authorized to access this resource"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - PDF generation failed")
    })
    @GetMapping("/students/courses")
    public void StudentsCoursesPDF(HttpServletResponse response) throws IOException, BadElementException {
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.allCoursesYourStudentHas(response);
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }
}
