package com.student.Student_Management;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final List<Student> studentList = new ArrayList<>();
    private final Map<String, Student> studentMap = new HashMap<>();
    private final String FILE_NAME = "admission_records.txt";

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public boolean registerEngineeringStudent(EngineeringStudent student) {
        if (!student.isEligible()) {
            return false;
        }

        repository.saveAndFlush(student);

        if (!studentMap.containsKey(student.getApplicationId())) {
            studentList.add(student);
            studentMap.put(student.getApplicationId(), student);
        }

        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(student.getApplicationId() + "," + student.getName() + "," + 
                         student.getAge() + "," + student.getMarks() + "," + 
                         student.getSpecialization() + "\n");
        } catch (IOException e) {
            System.err.println("File backup write error: " + e.getMessage());
        }

        return true;
    }

    public List<Student> getAllStudents() {
        List<Student> dbRecords = repository.findAll();
        studentList.clear();
        studentMap.clear();
        for (Student s : dbRecords) {
            studentList.add(s);
            studentMap.put(s.getApplicationId(), s);
        }
        return new ArrayList<>(studentList);
    }

    public Optional<Student> getStudentById(String id) {
        if (studentMap.containsKey(id)) {
            return Optional.of(studentMap.get(id));
        }
        return repository.findById(id);
    }

    @Transactional
    public void deleteStudent(String id) {
        repository.deleteById(id);
        repository.flush();
        Student removedStudent = studentMap.remove(id);
        if (removedStudent != null) {
            studentList.remove(removedStudent);
        }
    }
}
