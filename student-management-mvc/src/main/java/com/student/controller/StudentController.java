package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new": showNewForm(request, response); break;
            case "edit": showEditForm(request, response); break;
            case "delete": deleteStudent(request, response); break;
            case "search": searchStudents(request, response); break;
            default: listStudents(request, response); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "insert": insertStudent(request, response); break;
            case "update": updateStudent(request, response); break;
        }
    }

    // List students (with sort/filter)
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        String major = request.getParameter("major"); // optional filter

        List<Student> students;
        if (major != null && !major.isEmpty()) {
            students = studentDAO.getStudentsFiltered(major, sortBy, sortOrder);
            request.setAttribute("filterMajor", major);
        } else {
            students = studentDAO.getStudentsFiltered(null, sortBy, sortOrder);
        }

        request.setAttribute("students", students);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("isNew", true);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDAO.getStudentById(id);
        request.setAttribute("student", existingStudent);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // Insert new student
    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Student student = extractStudentFromRequest(request);

        if (!validateStudent(student, request)) {
            request.setAttribute("student", student);
            request.setAttribute("isNew", true);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (studentDAO.addStudent(student)) {
            response.sendRedirect("student?action=list&message=Student added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }

    // Update student
    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student student = extractStudentFromRequest(request);
        student.setId(id);

        if (!validateStudent(student, request)) {
            request.setAttribute("student", student);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=Student updated successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to update student");
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Student deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }

    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Student> students = studentDAO.searchStudents(keyword);
        request.setAttribute("students", students);
        request.setAttribute("searchKeyword", keyword);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // --- Helper methods ---

    private Student extractStudentFromRequest(HttpServletRequest request) {
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");
        return new Student(studentCode, fullName, email, major);
    }

    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;

        // Student code
        String codePattern = "[A-Z]{2}[0-9]{3,}";
        if (student.getStudentCode() == null || student.getStudentCode().isEmpty()) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        } else if (!student.getStudentCode().matches(codePattern)) {
            request.setAttribute("errorCode", "Invalid format. Use 2 letters + 3+ digits (e.g., SV001)");
            isValid = false;
        }

        // Full name
        if (student.getFullName() == null || student.getFullName().isEmpty()) {
            request.setAttribute("errorName", "Full name is required");
            isValid = false;
        } else if (student.getFullName().length() < 2) {
            request.setAttribute("errorName", "Full name must be at least 2 characters");
            isValid = false;
        }

        // Email (optional)
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (student.getEmail() != null && !student.getEmail().isEmpty()) {
            if (!student.getEmail().matches(emailPattern)) {
                request.setAttribute("errorEmail", "Invalid email format");
                isValid = false;
            }
        }

        // Major
        if (student.getMajor() == null || student.getMajor().isEmpty()) {
            request.setAttribute("errorMajor", "Major is required");
            isValid = false;
        }

        return isValid;
    }
}
