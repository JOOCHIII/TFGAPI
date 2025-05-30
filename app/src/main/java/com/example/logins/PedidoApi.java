package com.example.logins;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PedidoApi {
    @POST("/api/pedido/tramitar")
    Call<Void> tramitarPedido(@Query("idUsuario") long idUsuario);
}