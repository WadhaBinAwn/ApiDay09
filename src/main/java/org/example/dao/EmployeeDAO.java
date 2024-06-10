
package org.example.dao;

import org.example.dto.EmployeeFilterDto;
import org.example.models.Employees;

import java.sql.*;
import java.util.ArrayList;

public class EmployeeDAO {

    private static final String URL ="jdbc:sqlite:C:\\Users\\dev\\IdeaProjects\\HrApiDay09\\src\\main\\resources\\hr.db";
    private static final String SELECT_ALL_EMPLOYEES = "SELECT * FROM employees";
    private static final String SELECT_ONE_EMPLOYEES_JOIN_JOBS = "SELECT * FROM employees JOIN jobs ON employees.job_id = jobs.job_id WHERE employee_id = ?";
    private static final String SELECT_ONE_EMPLOYEE = "SELECT * FROM employees WHERE employee_id = ?";
    private static final String SELECT_BY_HIRE_DATE = "SELECT * FROM employees WHERE hire_date=?";
    private static final String SELECT_BY_JOB_ID = "SELECT * FROM employees WHERE job_id=?";
    private static final String INSERT_EMPLOYEE = "INSERT INTO employees VALUES (?, ?, ?,?,?,?,?,?,?)";
    private static final String UPDATE_EMPLOYEE = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, hire_date=?, job_id=?, salary=?, manager_id=?, department_id=? WHERE employee_id = ?";
    private static final String DELETE_EMPLOYEE = "DELETE FROM employees WHERE employee_id = ?";

    public void insertEmployee(Employees e) throws SQLException, ClassNotFoundException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement st = conn.prepareStatement(INSERT_EMPLOYEE)) {
            st.setInt(1, e.getEmployee_id());
            st.setString(2, e.getFirst_name());
            st.setString(3, e.getLast_name());
            st.setString(4, e.getEmail());
            st.setString(5, e.getPhone_number());
            st.setString(6, e.getHire_date());
            st.setInt(7, e.getJob_id());
            st.setDouble(8, e.getSalary());
            st.setInt(9, e.getManager_id());
            st.setInt(10, e.getDepartment_id());
            st.executeUpdate();
        }
    }

    public void updateEmployee(Employees e) throws SQLException, ClassNotFoundException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement st = conn.prepareStatement(UPDATE_EMPLOYEE)) {
            st.setString(1, e.getFirst_name());
            st.setString(2, e.getLast_name());
            st.setString(3, e.getEmail());
            st.setString(4, e.getPhone_number());
            st.setString(5, e.getHire_date());
            st.setInt(6, e.getJob_id());
            st.setDouble(7, e.getSalary());
            st.setInt(8, e.getManager_id());
            st.setInt(9, e.getDepartment_id());
            st.setInt(10, e.getEmployee_id());
            st.executeUpdate();
        }
    }

    public void deleteEmployee(int employee_id) throws SQLException, ClassNotFoundException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement st = conn.prepareStatement(DELETE_EMPLOYEE)) {
            st.setInt(1, employee_id);
            st.executeUpdate();
        }
    }

//    public Employees selectEmployee(int employee_id) throws SQLException, ClassNotFoundException {
//        try (Connection conn = DriverManager.getConnection(URL);
//             PreparedStatement st = conn.prepareStatement(SELECT_ONE_EMPLOYEE)) {
//            st.setInt(1, employee_id);
//            ResultSet rs = st.executeQuery();
//            return rs.next() ? new Employees(rs) : null;
//        }
//    }



    public Employees selectEmployee(int emplyee_id) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement st =conn.prepareStatement(SELECT_ONE_EMPLOYEES_JOIN_JOBS);
        st.setInt(1, emplyee_id);
        ResultSet rs = st.executeQuery();
        return rs.next() ? new Employees(rs) : null;

    }
    public ArrayList<Employees> selectAllEmployees(EmployeeFilterDto filterDto) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(URL)) {
            PreparedStatement st;

            if (filterDto.getHire_date() != null) {
                st = conn.prepareStatement(SELECT_BY_HIRE_DATE);
                st.setString(1, filterDto.getHire_date());
            } else if (filterDto.getJobId() != null) {
                st = conn.prepareStatement(SELECT_BY_JOB_ID);
                st.setInt(1, filterDto.getJobId());
            } else {
                st = conn.prepareStatement(SELECT_ALL_EMPLOYEES);
            }

            ResultSet rs = st.executeQuery();
            ArrayList<Employees> employs = new ArrayList<>();
            while (rs.next()) {
                employs.add(new Employees(rs));
            }

            return employs;
        }
    }

//    public ArrayList<Employees> selectEmployeesByHireYear(int hireYear) throws SQLException, ClassNotFoundException {
//        try (Connection conn = DriverManager.getConnection(URL);
//             PreparedStatement st = conn.prepareStatement(SELECT_BY_HIRE_DATE)) {
//            st.setString(1, "%" + hireYear + "%");
//            ResultSet rs = st.executeQuery();
//            ArrayList<Employees> employees = new ArrayList<>();
//            while (rs.next()) {
//                employees.add(new Employees(rs));
//            }
//            return employees;
//        }
//    }
//
//    public ArrayList<Employees> selectEmployeesByJobId(int jobId) throws SQLException, ClassNotFoundException {
//        try (Connection conn = DriverManager.getConnection(URL);
//             PreparedStatement st = conn.prepareStatement(SELECT_BY_JOB_ID)) {
//            st.setInt(1, jobId);
//            ResultSet rs = st.executeQuery();
//            ArrayList<Employees> employees = new ArrayList<>();
//            while (rs.next()) {
//                employees.add(new Employees(rs));
//            }
//            return employees;
//        }
//    }
}
