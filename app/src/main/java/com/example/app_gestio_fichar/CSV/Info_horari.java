package com.example.app_gestio_fichar.CSV;

import java.util.Arrays;

public class Info_horari {
    private String[] dies = new String[6]; // dies de la setmana
    private String[] hores; // fer el split de "-" per saber les 2 hores
    private String[] x = new String[6]; // dies de la setmana

    public Info_horari() {
    }

    public Info_horari(String[] dies, String[] hores, String[] x) {
        this.dies = dies;
        this.hores = hores;
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info_horari that = (Info_horari) o;
        return Arrays.equals(getDies(), that.getDies()) && Arrays.equals(getHores(), that.getHores()) && Arrays.equals(getX(), that.getX());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getDies());
        result = 31 * result + Arrays.hashCode(getHores());
        result = 31 * result + Arrays.hashCode(getX());
        return result;
    }

    public String[] getDies() {
        return dies;
    }

    public void setDies(String[] dies) {
        this.dies = dies;
    }

    public String[] getHores() {
        return hores;
    }

    public void setHores(String[] hores) {
        this.hores = hores;
    }

    public String[] getX() {
        return x;
    }

    public void setX(String[] x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Info_horari{" +
                "dies=" + Arrays.toString(dies) +
                ", hores=" + Arrays.toString(hores) +
                ", x=" + Arrays.toString(x) +
                '}';
    }
}
