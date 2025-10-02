package com.vaggelis.SpringSchool.service.pdf;

//import com.lowagie.text.*;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//import com.vaggelis.SpringSchool.dto.ImageReadDTO;
//import com.vaggelis.SpringSchool.dto.StudentReadDTO;
//import com.vaggelis.SpringSchool.mapper.Mapper;
//import com.vaggelis.SpringSchool.models.Student;
//import com.vaggelis.SpringSchool.repository.IStudentRepository;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import com.lowagie.text.Image;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;


import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPCell;
import com.vaggelis.SpringSchool.dto.ImageReadDTO;
import com.vaggelis.SpringSchool.dto.StudentReadDTO;
import com.vaggelis.SpringSchool.dto.TeacherReadDTO;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.repository.IStudentRepository;
import com.vaggelis.SpringSchool.repository.ITeacherRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class PDFGeneratorServiceImpl implements IPDFGeneratorService{

    private final IStudentRepository studentRepository;
    private final ITeacherRepository teacherRepository;

    @Override
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("This is a title", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Paragraph paragraph2 = new Paragraph("Tis is a paragraph", fontParagraph);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph);
        document.add(paragraph2);
        document.close();
    }


    @Override
    public void allStudents(HttpServletResponse response) throws IOException, DocumentException {
        // Set PDF content type
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=students.pdf");

        Document document = new Document(PageSize.A4.rotate()); // Landscape for more space
        PdfWriter.getInstance(document, response.getOutputStream());

        List<Student> students = studentRepository.findAll();
        List<StudentReadDTO> readDTOS = students.stream()
                .map(Mapper::mappingStudentToReadDto)
                .toList();

        document.open();

        // Title
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("Student List", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Table setup
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{1f, 2f, 2f, 4f, 4f, 2f, 3f}); // last column for image/download link

        // Table headers
        Stream.of("ID", "Firstname", "Lastname", "Username", "Email", "Status", "Image/Download Link")
                .forEach(headerTitle -> {
                    PdfPCell header = new PdfPCell();
                    Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                    header.setPhrase(new Phrase(headerTitle, headFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Table rows
        for (StudentReadDTO student : readDTOS) {
            table.addCell(String.valueOf(student.getId()));
            table.addCell(student.getFirstname());
            table.addCell(student.getLastname());
            table.addCell(student.getUserReadDTO().getUsername());
            table.addCell(student.getUserReadDTO().getEmail());
            table.addCell(student.getUserReadDTO().getStatus().toString());

            ImageReadDTO img = student.getUserReadDTO().getImageReadDTO();
            if (img != null && img.getDownloadUrl() != null) {
                // Add download URL as text (optional: you could fetch bytes and embed image if needed)
                table.addCell(img.getDownloadUrl());
            } else {
                table.addCell(""); // empty if no image
            }
        }

        document.add(table);
        document.close();
    }


    @Override
    public void allStudentsWithImage(HttpServletResponse response) throws IOException, DocumentException, com.itextpdf.text.BadElementException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=students.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        List<Student> students = studentRepository.findAll();
        document.open();

        // Title
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("Student List", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Table
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{1f, 2f, 2f, 4f, 4f, 2f, 3f});

        // Table headers
        Stream.of("ID", "Firstname", "Lastname", "Username", "Email", "Status", "Image")
                .forEach(headerTitle -> {
                    PdfPCell header = new PdfPCell();
                    Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                    header.setPhrase(new Phrase(headerTitle, headFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Table rows
        for (Student student : students) {
            try {

                StudentReadDTO dto = Mapper.mappingStudentToReadDto(student);

                table.addCell(String.valueOf(dto.getId()));
                table.addCell(dto.getFirstname());
                table.addCell(dto.getLastname());
                table.addCell(dto.getUserReadDTO().getUsername());
                table.addCell(dto.getUserReadDTO().getEmail());
                table.addCell(dto.getUserReadDTO().getStatus().toString());

                // Image cell
                if (student.getUser().getImage() != null && student.getUser().getImage().getImage() != null) {
                    byte[] imgBytes = student.getUser().getImage().getImage();
                    Image pdfImg = Image.getInstance(imgBytes);
                    pdfImg.scaleToFit(50, 50);

                    PdfPCell imageCell = new PdfPCell();
                    imageCell.addElement(pdfImg);
                    imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(imageCell);
                } else {
                    table.addCell(""); // empty cell
                }
            } catch (Exception e) {
                // Add an empty cell so the table layout doesn't break
                table.addCell("");
            }
        }

        document.add(table);
        document.close();
    }

    @Override
    public void allTeachersWithImage(HttpServletResponse response) throws IOException, DocumentException, com.itextpdf.text.BadElementException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=students.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        List<Teacher> teachers = teacherRepository.findAll();

        document.open();

        // Title
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("Teachers List", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Table
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{1f, 2f, 2f, 4f, 4f, 2f, 3f});

        // Table headers
        Stream.of("ID", "Firstname", "Lastname", "Username", "Email", "Status", "Image")
                .forEach(headerTitle -> {
                    PdfPCell header = new PdfPCell();
                    Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                    header.setPhrase(new Phrase(headerTitle, headFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Table rows
        for (Teacher teacher : teachers) {
            try {
                TeacherReadDTO dto = Mapper.mappingTeacherToReadDto(teacher);

                table.addCell(String.valueOf(dto.getId()));
                table.addCell(dto.getFirstname());
                table.addCell(dto.getLastname());
                table.addCell(dto.getUserReadDTO().getUsername());
                table.addCell(dto.getUserReadDTO().getEmail());
                table.addCell(dto.getUserReadDTO().getStatus().toString());

                // Image cell
                if (teacher.getUser().getImage() != null && teacher.getUser().getImage().getImage() != null) {
                    byte[] imgBytes = teacher.getUser().getImage().getImage();
                    Image pdfImg = Image.getInstance(imgBytes);
                    pdfImg.scaleToFit(50, 50);

                    PdfPCell imageCell = new PdfPCell();
                    imageCell.addElement(pdfImg);
                    imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(imageCell);

                } else {
                    table.addCell(""); // empty cell
                }

            } catch (Exception e) {
                // Add an empty cell so the table layout doesn't break
                table.addCell("");
            }
        }

        document.add(table);
        document.close();
    }
}
