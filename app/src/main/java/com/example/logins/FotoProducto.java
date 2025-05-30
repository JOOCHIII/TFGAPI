package com.example.logins;

import java.io.Serializable;

public class FotoProducto implements Serializable {
    private int id;
    private String urlFoto;

    public FotoProducto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUrlFoto() { return urlFoto; }
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }
}
