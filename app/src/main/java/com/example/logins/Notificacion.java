package com.example.logins;

import com.google.gson.annotations.SerializedName;

public class Notificacion {
    private int id;
    private Integer idUsuario; // puede ser null para noti admins
    private String mensaje;
    private boolean leido;
    private String tipoDestino;
    private String fecha; // o Date, según como lo recibas de la API
    private Long idReferencia; // id del reporte o del pedido
    private String tipo;

    // Constructor vacío
    public Notificacion() {}

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }

    public String getTipoDestino() { return tipoDestino; }
    public void setTipoDestino(String tipoDestino) { this.tipoDestino = tipoDestino; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public Long getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Long idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
