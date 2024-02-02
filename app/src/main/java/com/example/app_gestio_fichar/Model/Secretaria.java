package com.example.app_gestio_fichar.Model;

public class Secretaria {
    private String carreg;
    private String tasques;

    public Secretaria() {
    }

    public Secretaria(String carreg, String tasques) {
        this.carreg = carreg;
        this.tasques = tasques;
    }

    public String getCarreg() {
        return carreg;
    }

    public void setCarreg(String carreg) {
        this.carreg = carreg;
    }

    public String getTasques() {
        return tasques;
    }

    public void setTasques(String tasques) {
        this.tasques = tasques;
    }

    @Override
    public String toString() {
        return "Secretaria{" +
                "carreg='" + carreg + '\'' +
                ", tasques='" + tasques + '\'' +
                '}';
    }
}

