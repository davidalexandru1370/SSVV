package org.example;

import org.example.domain.Student;
import org.example.domain.Tema;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AppTest {
    private StudentXMLRepo studentFileRepository;
    private StudentValidator studentValidator;

    private TemaXMLRepo temaFileRepository;
    private TemaValidator temaValidator;
    private Service service;

    @BeforeAll
    static void createXML() {
        var files = List.of("fisiere/studentiTest.xml", "fisiere/temeTest.xml");
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

    @BeforeEach
    public void setup() {
        this.studentFileRepository = new StudentXMLRepo("fisiere/studentiTest.xml");
        this.temaFileRepository = new TemaXMLRepo("fisiere/temeTest.xml");
        this.studentValidator = new StudentValidator();
        this.temaValidator = new TemaValidator();
        this.service = new Service(this.studentFileRepository, this.studentValidator, temaFileRepository, temaValidator, null, null);

    }

    @AfterAll
    static void removeXML() {
        new File("fisiere/studentiTest.xml").delete();
        new File("fisiere/temeTest.xml").delete();
    }


    @Test
    public void testAddStudent(){
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
    public void testAddStudentValidName(){
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());
        this.service.deleteStudent("1");
    }

    @Test
    public void testAddStudentEmptyName(){
        Student newStudent2 = new Student("2", "", 931, "ana@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));

    }

    @Test
    public void testAddStudentNullName(){
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
    public void testAddStudentGroupLowerBVABound(){
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
    public void testAddAssignment_TooBigDeadline() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Tema tema1 = new Tema("1", "s", 99999, 3);
        this.service.addStudent(newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addTema(tema1));
        this.service.deleteStudent("1");
        this.service.deleteTema("1");
    }

    @Test
    public void testAddAssignment_TooEarlyReceiveDate() {
        Student newStudent1 = new Student("1", "Ana", 931, "ana@gmail.com");
        Tema tema1 = new Tema("1", "s", 5, 0);
        this.service.addStudent(newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addTema(tema1));
        this.service.deleteStudent("1");
        this.service.deleteTema("1");
    }


}