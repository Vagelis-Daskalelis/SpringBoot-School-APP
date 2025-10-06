package com.vaggelis.SpringSchool.service.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vaggelis.SpringSchool.dto.image.ImageReadDTO;
import com.vaggelis.SpringSchool.dto.student.StudentReadDTO;
import com.vaggelis.SpringSchool.dto.teacher.TeacherReadDTO;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Course;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.repository.IStudentRepository;
import com.vaggelis.SpringSchool.repository.ITeacherRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import com.vaggelis.SpringSchool.service.student.IStudentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class PDFGeneratorServiceImpl implements IPDFGeneratorService{

    private final IUserRepository userRepository;
    private final IStudentRepository studentRepository;
    private final ITeacherRepository teacherRepository;
    private final IStudentService studentService;



    /**Creates a PDF with all the students
     *
     * @param response
     * @throws IOException
     * @throws DocumentException
     * @throws com.itextpdf.text.BadElementException
     */
    @Override
    public void allStudents(HttpServletResponse response) throws IOException, DocumentException, com.itextpdf.text.BadElementException {
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
                table.addCell(dto.getUser().getUsername());
                table.addCell(dto.getUser().getEmail());
                table.addCell(dto.getUser().getStatus().toString());

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

    /**Creates a PDF with all the teachers
     *
     * @param response
     * @throws IOException
     * @throws DocumentException
     * @throws com.itextpdf.text.BadElementException
     */
    @Override
    public void allTeachers(HttpServletResponse response) throws IOException, DocumentException, com.itextpdf.text.BadElementException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=teachers.pdf");

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
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{1f, 2f, 2f, 4f, 4f, 2f, 3f, 2f});

        // Table headers
        Stream.of("ID", "Firstname", "Lastname", "Username", "Email", "Status", "Image", "Speciality")
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
                table.addCell(dto.getUser().getUsername());
                table.addCell(dto.getUser().getEmail());
                table.addCell(dto.getUser().getStatus().toString());

                // Image cell
                if (teacher.getUser().getImage() != null && teacher.getUser().getImage().getImage() != null) {
                    byte[] imgBytes = teacher.getUser().getImage().getImage();
                    Image pdfImg = Image.getInstance(imgBytes);
                    pdfImg.scaleToFit(70, 70);

                    PdfPCell imageCell = new PdfPCell();
                    imageCell.addElement(pdfImg);
                    imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(imageCell);

                } else {
                    table.addCell(""); // empty cell
                }

                // Speciality cell
                if (teacher.getSpeciality() != null && teacher.getSpeciality().getName() != null) {

                    table.addCell(teacher.getSpeciality().getName());
                }else {
                    table.addCell("");
                }

            } catch (Exception e) {
                // Add an empty cell so the table layout doesn't break
                table.addCell("");
            }
        }

        document.add(table);
        document.close();
    }

    /**Creates a PDF of the courses a logged student has
     *
     * @param response
     * @throws IOException
     * @throws com.itextpdf.text.BadElementException
     * @throws DocumentException
     */
    @Override
    public void allCoursesYourStudentHas(HttpServletResponse response)
            throws IOException, com.itextpdf.text.BadElementException, DocumentException {

        // Get the logged-in user's email from the JWT authentication context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Student targetStudent;

        try {
            // Find the logged-in user
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Find the student
            targetStudent = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new StudentNotFoundException(Student.class, currentUser.getId()));

            // Authorization check
            if (!targetStudent.getUser().getId().equals(currentUser.getId())) {
                throw new SecurityException("You are not authorized to see this profile");
            }

            // Set response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=courses.pdf");

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Title
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph title = new Paragraph("Your Courses", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2f, 4f, 3f, 2f});

            // Table headers
            Stream.of("ID", "Name", "Date", "Hours")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                        header.setPhrase(new Phrase(headerTitle, headFont));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            // Table rows
            List<Course> courses = studentService.findAllStudentsCourses(targetStudent.getId());
            for (Course course : courses) {
                table.addCell(String.valueOf(course.getId()));
                table.addCell(course.getName());
                table.addCell(course.getDate().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
                table.addCell(String.valueOf(course.getHours()));
            }

            document.add(table);
            document.close();

        } catch (StudentNotFoundException | SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
