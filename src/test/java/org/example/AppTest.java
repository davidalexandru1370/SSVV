package org.example;

import org.example.domain.Nota;
import org.example.domain.Student;
import org.example.domain.Tema;
import org.example.repository.NotaXMLRepo;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.ValidationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AppTest {
    private StudentXMLRepo studentFileRepository;
    private StudentValidator studentValidator;

    private TemaXMLRepo temaFileRepository;
    private TemaValidator temaValidator;

    private NotaXMLRepo notaFileRepository;
    private NotaValidator notaValidator;

    private Service service;

    @BeforeAll
    static void createXML() {
        var files = List.of("fisiere/studentiTest.xml", "fisiere/temeTest.xml", "fisiere/noteTest.xml");
        files.stream().forEach(file -> {
            File xml = new File(file);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(xml))) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<inbox>\n" +
                        "\n" +
                        "</inbox>");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @BeforeAll
    static void createTxt() {
        LocalDate startDate = LocalDate.now();
        int year = startDate.getYear();
        int month = startDate.getMonthValue();
        int day = startDate.getDayOfMonth();
        String date = year + "," + month + "," + day;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fisiere/DataInceput.txt"))) {
            writer.write(date);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @BeforeEach
    public void setup() {
        this.studentFileRepository = new StudentXMLRepo("fisiere/studentiTest.xml");
        this.temaFileRepository = new TemaXMLRepo("fisiere/temeTest.xml");
        this.notaFileRepository = new NotaXMLRepo("fisiere/noteTest.xml");
        this.studentValidator = new StudentValidator();
        this.temaValidator = new TemaValidator();
        this.notaValidator = new NotaValidator(this.studentFileRepository, this.temaFileRepository);
        this.service = new Service(this.studentFileRepository,
                this.studentValidator,
                temaFileRepository,
                temaValidator,
                notaFileRepository,
                notaValidator);

    }

    @AfterAll
    static void removeXML() {
        new File("fisiere/studentiTest.xml").delete();
        new File("fisiere/temeTest.xml").delete();
        new File("fisiere/noteTest.xml").delete();
    }


    @Test
    public void testAddStudent() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Student newStudent2 = new Student("2", "Ana", 931, "ana@gmail.com");


        Student stud1 = this.service.addStudent(newStudent1);
        assertNull(stud1);

        Student stud2 = this.service.addStudent(newStudent2);
        assertNull(stud2);

        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        assertEquals(students.next().getID(), newStudent2.getID());

        this.service.deleteStudent("1");
        this.service.deleteStudent("2");
    }

    @Test
    public void testAddStudentValidName() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("1");
    }

    @Test
    public void testAddStudentEmptyName() {
        Student newStudent2 = new Student("2", "", 931, "ana@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));

    }

    @Test
    public void testAddStudentNullName() {
        Student newStudent3 = new Student("3", null, 931, "ana@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

    @Test
    public void testAddStudentValidGroup() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");

        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());

        this.service.deleteStudent("1");
    }

    @Test
    public void testAddStudentInvalidGroup() {
        Student newStudent2 = new Student("2", "Ana", -6, "ana@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
    }

    @Test
    public void testAddStudentValidEmail() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("1");
    }

    @Test
    public void testAddStudentEmptyEmail() {
        Student newStudent2 = new Student("2", "Ana", 931, "");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
    }

    @Test
    public void testAddStudentNullEmail() {
        Student newStudent3 = new Student("3", "Ana", 931, null);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

    @Test
    public void testAddStudentValidId() {
        Student newStudent1 = new Student("2345", "Ana", 931, "ana@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("2345");
    }

    @Test
    public void testAddStudentEmptyId() {
        Student newStudent2 = new Student("", "Ana", 931, "ana@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
    }

    @Test
    public void testAddStudentNullId() {
        Student newStudent3 = new Student(null, "Ana", 931, "ana@gmail.com");
        assertThrows(NullPointerException.class, () -> this.service.addStudent(newStudent3));
    }

    /**
     * BVA Test case
     */
    @Test
    public void testAddStudentGroupLowerBVABound() {
        Student newStudent1 = new Student("1", "Ana", 0, "ana@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("1");
    }


    @Test
    public void testAddAssignment() {

        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Tema tema1 = new Tema("1", "tema1", 5, 3);
        this.service.addStudent(newStudent1);
        this.service.addTema(tema1);
        var homeworks = this.service.getAllTeme();
        assertEquals(homeworks.iterator().next().getID(), tema1.getID());

        this.service.deleteStudent("1");
        this.service.deleteTema("1");
    }

    @Test
    public void testAddAssignment_emptyId() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Tema tema1 = new Tema("", "tema1", 5, 3);
        this.service.addStudent(newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addTema(tema1));
        this.service.deleteStudent("1");
        this.service.deleteTema("1");
    }

    @Test
    public void testAddAssignment_emptyName() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Tema tema1 = new Tema("1", "", 5, 3);
        this.service.addStudent(newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addTema(tema1));
        this.service.deleteStudent("1");
        this.service.deleteTema("1");
    }

    @Test
    public void testAddAssignment_tooBigDeadline() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Tema tema1 = new Tema("1", "s", 99999, 3);
        this.service.addStudent(newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addTema(tema1));
        this.service.deleteStudent("1");
        this.service.deleteTema("1");
    }

    @Test
    public void testAddAssignment_tooEarlyReceiveDate() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Tema tema1 = new Tema("1", "s", 5, 0);
        this.service.addStudent(newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addTema(tema1));
        this.service.deleteStudent("1");
        this.service.deleteTema("1");
    }

    @Test
    public void testAddGrade() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Tema tema1 = new Tema("1", "s", 5, 4);
        LocalDate deliveryDate = LocalDate.now();
        Nota nota = new Nota("1", "1", "1", 5, deliveryDate);
        this.service.addStudent(newStudent1);
        this.service.addTema(tema1);
        this.service.addNota(nota, "good job");

        var grade = this.service.findNota("1");
        assertEquals(grade.getID(), nota.getID());

        this.service.deleteStudent("1");
        this.service.deleteTema("1");
        this.service.deleteNota("1");
    }
}