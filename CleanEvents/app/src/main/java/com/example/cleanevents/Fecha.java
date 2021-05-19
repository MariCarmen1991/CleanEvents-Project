package com.example.cleanevents;

import com.google.firebase.Timestamp;

public class Fecha {

    String dia;
    String horaInicio;
    String horaFinal;

    public Fecha() {
        this.dia = "";
        this.horaInicio = "";
        this.horaFinal = "";
    }

    public Fecha(String dia, String horaInicio, String horaFinal) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    @Override
    public String toString() {
        return "Fecha{" +
                "dia='" + dia + '\'' +
                ", horaInicio='" + horaInicio + '\'' +
                ", horaFinal='" + horaFinal + '\'' +
                '}';
    }
}
