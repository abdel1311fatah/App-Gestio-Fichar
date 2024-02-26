package com.example.app_gestio_fichar.Hours;

import java.time.LocalDateTime;

public class Validador { // clase para validar el horario
    public Validador() { // Constructor vacío
    }

    public boolean isWeekend(int day) { // agafem el dia de Info_horari i el comparem amb 6 o 7, que es el que ens donaria al excel
        if (day == 6 || day == 7) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHomeTime(String[] horesHorari) { // agafem les hores de Info_horari i les comparem amb les hores actuals
        LocalDateTime horaActual = LocalDateTime.now(); // agafem la hora actual
        String horaInici = horesHorari[0].split("-")[0].split(":")[0];
        Integer horaIniciHorari = Integer.parseInt(horaInici);
        String horaFi = horesHorari[horesHorari.length - 1].split("-")[1].split(":")[0];
        Integer horaFiHorari = Integer.parseInt(horaFi);

        if (horaActual.getHour() <= horaIniciHorari || horaActual.getHour() >= horaFiHorari) {
            return true;
        }

        return false;
    }
}
