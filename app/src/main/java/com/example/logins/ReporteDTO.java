package com.example.logins;

import com.google.gson.annotations.SerializedName;

public class ReporteDTO {
    private long idUsuario;
    private String asunto;
    private String descripcion;
    private String estado;
    private String nombreAsignado;
    @SerializedName("fecha")
    private String fecha; // Puedes usar String si no vas a parsear a Date

    public ReporteDTO() {
    }

    public ReporteDTO(long idUsuario, String asunto, String descripcion) {
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreAsignado() {
        return nombreAsignado;
    }

    public void setNombreAsignado(String nombreAsignado) {
        this.nombreAsignado = nombreAsignado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
