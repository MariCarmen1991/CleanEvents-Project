package com.example.cleanevents;

public class DatosTemporales {
    private String nombre_evento;
    private String lugar;
    private String fecha;
    private String descripcion;
    private String zona;
    private String pista;
    private String coordenadas;
    private String tipo_actividad;
    private Boolean tesoro;

    public DatosTemporales() {
        //
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(double lat, double lon) {
        this.coordenadas = lat +""+ lon;
    }

    public String getTipo_actividad() {
        return tipo_actividad;
    }

    public void setTipo_actividad(String tipo_actividad) {
        this.tipo_actividad = tipo_actividad;
    }

    public Boolean getTesoro() {
        return tesoro;
    }

    public void setTesoro(Boolean tesoro) {
        this.tesoro = tesoro;
    }
}
