package com.example.cleanevents;

import com.google.firebase.Timestamp;

public class Fecha {

    Timestamp dia;
    Timestamp horaInicio;
    Timestamp horaFinal;

    public Fecha() {
        this.dia = null;
        this.horaInicio = null;
        this.horaFinal = null;
    }

    public Fecha(Timestamp dia, Timestamp horaInicio, Timestamp horaFinal) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
    }

    public Timestamp getDia() {
        return dia;
    }

    public void setDia(Timestamp dia) {
        this.dia = dia;
    }

    public Timestamp getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Timestamp horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Timestamp getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Timestamp horaFinal) {
        this.horaFinal = horaFinal;
    }

    @Override
    public String toString() {
        return "Fecha{" +
                "dia=" + dia +
                ", horaInicio=" + horaInicio +
                ", horaFinal=" + horaFinal +
                '}';
    }
}
