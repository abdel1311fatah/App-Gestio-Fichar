package com.example.app_gestio_fichar.Hours;

public class Validador { // clase para validar el horario
    public Validador() { // Constructor vacío
    }

    public boolean isWeekend(int day) { // agafem el dia de Info_horari i el comparem amb 6 o 7, que es el que ens donaria al excel
        if (day == 6 || day == 7){
            return true;
        }else {
            return false;
        }
    }

    public boolean isHomeTime(String hores) { // agafem les hores de Info_horari i les comparem amb les hores actuals
        String horaInici = hores.split("-")[0].split(":")[0]; // el hores.split("-")[0] es ens done per exemple, 19:43, i amb el split(":")[0] ens quedem amb 19, que es la hora que necesitem
        String horaFinal = hores.split("-")[1]; // lo mateix que el de dalt
        if(Integer.parseInt(horaInici) <= 8 || Integer.parseInt(horaFinal) >= 21) { // per a que no vingi algu i fiqui que començe a treballar a les 2 de la nit i acabe a les 12 de la i apuntarse 20 hores diaries
            return true; // La Salle obre a les 8 i tanca a les 9 de la nit, per lo que si no s ha de poder fixar abans de les 8 ni despres de les 9
        }else{
            return false;
        }
    }
}
