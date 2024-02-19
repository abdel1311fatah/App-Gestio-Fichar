package com.example.app_gestio_fichar.Hours;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class Validador { // clase para validar el horario
    public Validador() { // Constructor vacío
    }
    ZoneId zonaHoraria = ZoneId.systemDefault(); // Obtengo la zona horaria del móvil

    LocalDateTime horaActual = LocalDateTime.now(zonaHoraria); // Obtengo la hora actual de la zona horaria

    public boolean isWeekend(int day) {
        return (day == 6 || day == 7);
    }

    public boolean isHomeTime(int hour) {
        // Teniendo en cuenta que La Salle Mollerussa abre a las 8 y cierra a las 21
        return ((hour <= 8) || hour >= 21);
    }

    public boolean isRestTime(String[] x) {
        // Si todos los días de la semana tienen esa franja horaria vacía, significa que es hora de descanso
        int contador = 0;
        for (String value : x) {
            if (value.equals("X")) {
                contador++;
            }
        }

        return contador == 0; // No hay trabajo en esta franja horaria, así que es tiempo de descanso
    }

    public boolean canviDeClasse(LocalTime hora){
        LocalTime horaActual = LocalTime.now(zonaHoraria);
        return horaActual.isAfter(hora.plusMinutes(50));
    }

    public boolean isHoliday(int day, int month) {
        // Según el código proporcionado
        if (month == 1 && day == 1) {
            return true; // Año Nuevo
        }
        if (month == 1 && day == 6) {
            return true; // Reyes
        }
        if (month == 5 && day == 1) {
            return true; // Día del Trabajador
        }
        if (month == 8 && day == 15) {
            return true; // Asunción de la Virgen María
        }
        if (month == 10 && day == 12) {
            return true; // Hispanidad
        }
        if (month == 11 && day == 1) {
            return true; // Todos los Santos
        }
        if (month == 12 && day == 6) {
            return true; // Día de la Constitución
        }
        if (month == 12 && day == 8) {
            return true; // Inmaculada Concepción
        }
        if (month == 12 && day == 25) {
            return true; // Navidad
        }
        return false;
    }
}
