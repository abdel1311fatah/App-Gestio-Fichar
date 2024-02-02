package com.example.app_gestio_fichar.Model;

public class Professor {
    private String carreg;
    private String assignatura;

    public Professor() {
    }

    public Professor(String carreg, String assignatura) {
        this.carreg = carreg;
        this.assignatura = assignatura;
    }

    public String getCarreg() {
        return carreg;
    }

    public void setCarreg(String carreg) {
        this.carreg = carreg;
    }

    public String getAssignatura() {
        return assignatura;
    }

    public void setAssignatura(String assignatura) {
        this.assignatura = assignatura;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "carreg='" + carreg + '\'' +
                ", assignatura='" + assignatura + '\'' +
                '}';
    }
}

