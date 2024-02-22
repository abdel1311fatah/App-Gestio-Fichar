package com.example.app_gestio_fichar.CSV;

import android.content.ContentResolver;
import android.net.Uri;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class Info_horari {
    private int dia; // dies de la setmana
    private ArrayList<String> hores = new ArrayList<>(); // hores de la feina
    private ArrayList<String> x = new ArrayList<>(); // si hi ha feina

    public Info_horari() {
    }

    public Info_horari(int dia, ArrayList<String> hores, ArrayList<String> x) {
        this.dia = dia;
        this.hores = hores;
        this.x = x;
    }

    public Info_horari llegirCSV(Uri selectedFileUri, ContentResolver contentResolver, LocalDateTime localDateTime) throws IOException { // aixo ens done un objecte que tindra les dades del excel com atributs

        Info_horari horari = new Info_horari();
        InputStream myInput = contentResolver.openInputStream(selectedFileUri);
        StringBuilder dades = new StringBuilder();
        int diaActual = localDateTime.getDayOfWeek().getValue(); // va del 1 al 7, per aixo no començe per 0
        int horaActual = localDateTime.getHour();
        int minutActual = localDateTime.getMinute();
        int numeroDia = 1; // 1 dilluns, 2 dimarts, per saber si el dia de la setmana del horari es avui
        ArrayList<String> dies = new ArrayList<>(); // guadre els dies del excel, a lo millor el excel te 4 dies, pos llavors n agafarie 4
        boolean primeraFila = true; // s' enten que la primera fila es on van els dies

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(myInput);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            while (rowIterator.hasNext()) { // recorre les files
                XSSFRow row = (XSSFRow) rowIterator.next(); // agafe la seguent fila del excel
                Iterator<Cell> cellIterator = row.cellIterator(); // recorre les celes del excel, es un iterador per que una fila pot tenir varies celes

                while (cellIterator.hasNext()) { // mire cada cela de la fila
                    XSSFCell cell = (XSSFCell) cellIterator.next(); // agafe la seguent cela de la fila
                    if (!cell.toString().isEmpty()) { // per exemple, al excel de proba no començe a la primera fila, començe a la segona, llavors es millor que no agafi les celes buides
                        if (numeroDia == diaActual && primeraFila) { // si el dia de la setmana del horari es avui i es la primera fila
                            horari.setDia(numeroDia);
                            primeraFila = false; // ja no ha de llegir la fila dels dies de la setmana per que ja ha trobat el dia equivalent a avui
                            if(cell.getColumnIndex() == 0){ // es la part on fique per exemple: 10:10-11:00
                                horari.getHores().add(cell.toString());
                            } else if(cell.getColumnIndex() == 1){ // es la part on fique per exemple: X
                                horari.getX().add(cell.toString());
                            }
                        }
                        dades.append(cell.toString()).append(" "); // agafe el contingut de la cela
                        numeroDia++; // al acabar el while acabe la fila, per lo que resetejem el contador
                    }
                }
                dades.append("\n"); // ha acabat la fila, per lo que fa el salt de linia per tirar la seguent fila
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (myInput != null) {
                    myInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return horari;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public ArrayList<String> getHores() {
        return hores;
    }

    public void setHores(ArrayList<String> hores) {
        this.hores = hores;
    }

    public ArrayList<String> getX() {
        return x;
    }

    public void setX(ArrayList<String> x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Info_horari{" +
                "dia=" + dia +
                ", hores=" + hores +
                ", x=" + x +
                '}';
    }
}
