package com.example.logins;

public class ReporteDTO {
    private long idUsuario;
    private String asunto;
    private String descripcion;

    public ReporteDTO(long idUsuario, String asunto, String descripcion) {
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
    }

    public ReporteDTO() {

    }

    // Getters y setters (puedes generar con el IDE)
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
}
