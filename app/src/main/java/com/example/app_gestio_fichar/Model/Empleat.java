package com.example.app_gestio_fichar.Model;

import static android.app.SearchManager.QUERY;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Empleat {
    private int id;
    private String carreg;
    private String nom;
    private String cognom;
    private String correuElectronic;
    private String contrasenya;

    public Empleat() {
    }

    public Empleat(int id, String carreg, String nom, String cognom, String correuElectronic, String contrasenya) {
        this.id = id;
        this.carreg = carreg;
        this.nom = nom;
        this.cognom = cognom;
        this.correuElectronic = correuElectronic;
        this.contrasenya = contrasenya;
    }

    // Les credencials per entrar a la db
    static final String DB_URL = "jdbc:mysql://sql.freedb.tech/freedb_nca_hores";
    static final String USER = "freedb_abdullah_fatah";
    static final String PASS = "5uAYk&Y6RctMcq6";

    public Empleat filterById(String id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); Statement stmt = conn.createStatement()) { // Intentem conectar a la base de dades
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM empleats where id =(?)")){
                pstmt.setInt(1, Integer.parseInt(id));
                ResultSet rs = pstmt.executeQuery();

                Empleat empleat = new Empleat(rs.getInt("id"), rs.getString("carreg"), rs.getString("nom"), rs.getString("cognom"),
                        rs.getString("correu_electronic"), rs.getString("contrasenya")); // Creem el empleat amb els camps del empleat que ha trobat a la db

                return empleat;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarreg() {
        return carreg;
    }

    public void setCarreg(String carreg) {
        this.carreg = carreg;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public String getCorreuElectronic() {
        return correuElectronic;
    }

    public void setCorreuElectronic(String correuElectronic) {
        this.correuElectronic = correuElectronic;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    @Override
    public String toString() {
        return "Empleat{" +
                "id=" + id +
                ", carreg='" + carreg + '\'' +
                ", nom='" + nom + '\'' +
                ", cognom='" + cognom + '\'' +
                ", correuElectronic='" + correuElectronic + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                '}';
    }
}

