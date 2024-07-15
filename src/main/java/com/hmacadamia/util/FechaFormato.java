package com.hmacadamia.util;


import java.time.LocalDate;

public class FechaFormato {
            // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        int mes = fechaActual.getMonthValue();
        int dia = fechaActual.getDayOfMonth();
        int ano = fechaActual.getYear();
        String fechaFormateada = String.valueOf(mes) + String.valueOf(dia) + String.valueOf(ano);

    public LocalDate getFechaActual() {
        return fechaActual;
    }

    public int getMes() {
        return mes;
    }

    public int getDia() {
        return dia;
    }

    public int getAno() {
        return ano;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }
}

