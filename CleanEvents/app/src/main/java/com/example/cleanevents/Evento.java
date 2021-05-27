package com.example.cleanevents;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class Evento implements Serializable {

    long idEvento;
    String nombre;
    long idUsuario;
    String nombreOrganizador;
    String tipoActividad;
    String imagen;
    Fecha fecha;
    String descripcion;
    String poblacion;
    long numParticipantes;
    Double latitud;
    Double longitud;
    String dia;


    public Evento() {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.nombreOrganizador = nombreOrganizador;
        this.tipoActividad = tipoActividad;
        this.imagen = imagen;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.poblacion = poblacion;
        this.numParticipantes = numParticipantes;
        this.latitud = latitud;
        this.longitud = longitud;
        this.dia = dia;
    }


    public Evento(long idEvento, String nombre, long idUsuario, String nombreOrganizador, String tipoActividad, String imagen, Fecha fecha, String descripcion, String poblacion, long numParticipantes, Double latitud, Double longitud, String dia) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.nombreOrganizador = nombreOrganizador;
        this.tipoActividad = tipoActividad;
        this.imagen = imagen;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.poblacion = poblacion;
        this.numParticipantes = numParticipantes;
        this.latitud = latitud;
        this.longitud = longitud;
        this.dia = dia;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }



    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreOrganizador() {
        return nombreOrganizador;
    }

    public void setNombreOrganizador(String nombreOrganizador) {
        this.nombreOrganizador = nombreOrganizador;
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

    public long getNumParticipantes() {
        return numParticipantes;
    }

    public void setNumParticipantes(long numParticipantes) {
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
