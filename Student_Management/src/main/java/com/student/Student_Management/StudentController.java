package com.student.Student_Management;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admission")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public String showDashboard(Model model, @RequestParam(required = false) String searchId) {
        List<Student> students;
        if (searchId != null && !searchId.trim().isEmpty()) {
            students = service.getStudentById(searchId).map(List::of).orElse(List.of());
        } else {
            students = service.getAllStudents();
        }
        
        model.addAttribute("students", students);
        return "index";
    }

    @PostMapping("/register")
    public String processApplication(@RequestParam("id") String id, 
                                     @RequestParam("name") String name,
                                     @RequestParam("age") int age, 
                                     @RequestParam("marks") double marks,
                                     @RequestParam("specialization") String specialization, 
                                     Model model) {
        
        EngineeringStudent student = new EngineeringStudent(id, name, age, marks, specialization);
        boolean success = service.registerEngineeringStudent(student);

        if (!success) {
            model.addAttribute("errorMessage", "Application rejected! Requirements not met (Age: 17-25, Marks >= 60%).");
            model.addAttribute("students", service.getAllStudents());
            return "index"; 
        }

        return "redirect:/admission";
    }

    @GetMapping("/delete/{id}")
    public String removeRecord(@PathVariable("id") String id) {
        service.deleteStudent(id);
        return "redirect:/admission";
    }
}
