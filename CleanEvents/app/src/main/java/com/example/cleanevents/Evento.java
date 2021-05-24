package com.example.cleanevents;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class Evento implements Serializable {

    int idEvento;
    String nombre;
    int idUsuario;
    String tipoActividad;
    String imagen;
    Fecha fecha;
    String descripcion;
    String poblacion;
    Integer numParticipantes;
    Double latitud;
    Double longitud;


    public Evento() {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.tipoActividad = tipoActividad;
        this.imagen = imagen;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.poblacion = poblacion;
        this.numParticipantes = numParticipantes;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Evento(int idEvento, String nombre, int idUsuario, String tipoActividad, String imagen, Fecha fecha, String descripcion, String poblacion, Integer numParticipantes, Double latitud, Double longitud) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.tipoActividad = tipoActividad;
        this.imagen = imagen;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.poblacion = poblacion;
        this.numParticipantes = numParticipantes;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public void setNumParticipantes(Integer numParticipantes) {
        this.numParticipantes = numParticipantes;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public int getNumParticipantes() {
        return numParticipantes;
    }

    public void setNumParticipantes(int numParticipantes) {
        this.numParticipantes = numParticipantes;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "idEvento=" + idEvento +
                ", nombre='" + nombre + '\'' +
                ", idUsuario=" + idUsuario +
                ", tipoActividad='" + tipoActividad + '\'' +
                ", imagen='" + imagen + '\'' +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", poblacion='" + poblacion + '\'' +
                ", numParticipantes=" + numParticipantes +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }


}
