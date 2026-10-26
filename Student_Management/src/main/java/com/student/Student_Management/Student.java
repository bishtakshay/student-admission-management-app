package com.student.Student_Management;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "student_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("GENERAL")
public class Student {

    @Id
    private String applicationId;
    private String name;
    private int age;
    private double marks;

    public Student() {}

    public Student(String applicationId, String name, int age, double marks) {
        this.applicationId = applicationId;
        this.name = name;
        this.age = age;
        this.marks = marks;
    }

    public boolean isEligible() {
        return this.age >= 17 && this.age <= 25 && this.marks >= 50.0;
    }

    // Getters and Setters
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }
}

// Child subclass extending the parent within the same entity context file
@Entity
@DiscriminatorValue("ENGINEERING")
class EngineeringStudent extends Student {
    
    private String specialization;

    public EngineeringStudent() {}

    public EngineeringStudent(String applicationId, String name, int age, double marks, String specialization) {
        super(applicationId, name, age, marks);
        this.specialization = specialization;
    }

    @Override
    public boolean isEligible() {
        // Engineering specific overridden target criteria (Minimum 60%)
        return super.isEligible() && getMarks() >= 60.0;
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}
