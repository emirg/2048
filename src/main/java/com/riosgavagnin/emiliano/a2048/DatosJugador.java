package com.riosgavagnin.emiliano.a2048;

// Clase que representara los datos de un jugador

public class DatosJugador {

    private int puntaje;
    private int idUsuario;
    private String nombreUsuario;

    public DatosJugador(int puntaje, int idUsuario, String nombreUsuario) {
        this.puntaje = puntaje;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
    }

    public DatosJugador() {

    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
