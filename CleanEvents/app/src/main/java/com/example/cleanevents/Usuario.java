package com.example.cleanevents;

public class Usuario {

    long idUsuario;
    String nombre;
    String email;
    String descripcion;
    String imagen;
    int rol;             // (0admin, 1 organizador, 2 participant)


    public Usuario() {
        this.idUsuario = 0;
        this.nombre = "";
        this.email = "";
        this.descripcion = "";
        this.imagen = "";
        this.rol = 0;
    }

    public Usuario(long idUsuario, String nombre, String email, String descripcion, String imagen, int rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.rol = rol;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", rol=" + rol +
                '}';
    }
}
