package com.example.app_gestio_fichar;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Calculator { // classe per validar l horari
    public Calculator () { // Constructor buit
    }

    ZoneId zonaHoraria = ZoneId.systemDefault(); // Agafe la zona horaria del mobil
    LocalDateTime horaActual = LocalDateTime.now(zonaHoraria); // Agafe la hora actual de la zona horaria, al ser LocalDateTime ens donarie algo amb aquest format: 2019-01-31T12:30:45

    public boolean isWeekend (int day) {
        if (day == 6 || day == 7) {
            return true;
        }
        return false;
    }

    public boolean isHomeTime(int hour, int minute){ // tenint en compte que lasalle mollerussa obre a les 8 i tanque a les 21
        if (hour < 8 || hour > 21) { // si la hora es abans de les 8 o despres de les 9 de la nit
            return true;
        }else{
            return false;
        }
    }

    public boolean isRestTime(String[] feina){ // si tots els dies de la setmana tens aquella parcela de temps buida, signifique que es hora de descans // per utilitzar fer un array de 5 amb una columna despres d haber passat totes les altres validacions
        int contador = 0;
        for (int i = 0; i < feina.length; i++) { // feina es un string, ja sigui per que pot ser una x o el nom d' una assignatura o una tasca
            if (feina[i].equals("X")) {
                contador++;
            }
        }

        if (contador == 0) { // no hi ha feina entre aquesta franja horaria, per lo que es temps de descans
            return true;
        }else{
            return false;
        }
    }
    public boolean itsNow (int hour, int minute){

        int horaCSV = horaActual.getHour(); // Agafe la hora actual
        int minutCSV = horaActual.getMinute(); // Agafe els minuts actuals

        if (horaCSV == hour && minutCSV == minute) { // Si la hora i els minuts del CSV coincideixen amb la hora actual, es a dir, estem a una de les hores que marquen l' horari
            return true;
        }else{
            return false;
        }
    }

    public boolean isHoliday (int day, int month) { // by copilot
        if (month == 1 && day == 1) { // any nou
            return true;
        }
        if (month == 1 && day == 6) { // reis
            return true;
        }
//        if (month == 4 && day == 10) { // els que estan comentats poden no ser aquests dies ja que varia segons l any
//            return true;
//        }
//        if (month == 4 && day == 13) {
//            return true;
//        }
        if (month == 5 && day == 1) { // dia del treballador
            return true;
        }
//        if (month == 6 && day == 24) {
//            return true;
//        }
        if (month == 8 && day == 15) { // Assumpcio de la Verge Maria
            return true;
        }
//        if (month == 9 && day == 11) { // No se si varia el dia cada any
//            return true;
//        }
        if (month == 10 && day == 12) { // hispanitat
            return true;
        }
        if (month == 11 && day == 1) { // tots sants
            return true;
        }
        if (month == 12 && day == 6) { // Dia de la Constitucio
            return true;
        }
        if (month == 12 && day == 8) { // Inmaculada Concepcio
            return true;
        }
        if (month == 12 && day == 25) { // nadal
            return true;
        }
        return false;
    }

    public int contadorHores(String horari) {
        int horesEntrades = 0;

        for (int i = 0; i < horari.length(); i++) {
            if (horari.charAt(i) == 'X') {
                horesEntrades++;
            }
        }
        return horesEntrades;
    }

}
