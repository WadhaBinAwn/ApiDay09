package org.example.dto;

public class EmployeeIdDto {

    private String deptCode;
    private int seq;
    private int hireYear;

    public EmployeeIdDto() {
    }

    public EmployeeIdDto(String deptCode, int seq, int hireYear) {
        this.deptCode = deptCode;
        this.seq = seq;
        this.hireYear = hireYear;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getHireYear() {
        return hireYear;
    }

    public void setHireYear(int hireYear) {
        this.hireYear = hireYear;
    }

    @Override
    public String toString() {
        return deptCode + seq + hireYear;
    }
}
