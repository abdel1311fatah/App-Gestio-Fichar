package com.example.app_gestio_fichar.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    Class.forName("com.mysql.jdbc.Driver");
    static final String DB_URL = "jdbc:mysql://sql.freedb.tech/freedb_springbootAA";
    static final String USER = "freedb_alexaiguade";
    static final String PASS = "J@Dp$6nvsaGt?Uz";

    public Empleat filterById(int id) { // mirar si va be i veure si es poden eliminar els altres models tant de java com de db

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM empleats WHERE id = ?")) { // aqui falle

            pstmt.setInt(1, id);
            ResultSet resultat = pstmt.executeQuery();

            if (resultat.next()) {

                Empleat empleat = new Empleat(resultat.getInt("id"), resultat.getString("carreg"), resultat.getString("nom"), resultat.getString("cognom"),
                        resultat.getString("correu_electronic"), resultat.getString("contrasenya")); // Creem el empleat amb els camps del empleat que ha trobat a la db

                return empleat;

            } else { // no ha trobat ningu

                return null;

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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

