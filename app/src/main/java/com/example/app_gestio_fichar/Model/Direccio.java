package com.example.app_gestio_fichar.Model;

public class Direccio {
    private String carreg;
    private String departament;

    public Direccio() {
    }

    public Direccio(String carreg, String departament) {
        this.carreg = carreg;
        this.departament = departament;
    }

    public String getCarreg() {
        return carreg;
    }

    public void setCarreg(String carreg) {
        this.carreg = carreg;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    @Override
    public String toString() {
        return "Direccio{" +
                "carreg='" + carreg + '\'' +
                ", departament='" + departament + '\'' +
                '}';
    }
}
