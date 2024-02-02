package com.example.app_gestio_fichar.Model;

public class Neteja {
    private String carreg;
    private String areaDeFeina;

    public Neteja() {
    }

    public Neteja(String carreg, String areaDeFeina) {
        this.carreg = carreg;
        this.areaDeFeina = areaDeFeina;
    }

    public String getCarreg() {
        return carreg;
    }

    public void setCarreg(String carreg) {
        this.carreg = carreg;
    }

    public String getAreaDeFeina() {
        return areaDeFeina;
    }

    public void setAreaDeFeina(String areaDeFeina) {
        this.areaDeFeina = areaDeFeina;
    }

    @Override
    public String toString() {
        return "Neteja{" +
                "carreg='" + carreg + '\'' +
                ", areaDeFeina='" + areaDeFeina + '\'' +
                '}';
    }
}
