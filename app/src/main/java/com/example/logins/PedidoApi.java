package com.example.logins;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PedidoApi {
    @POST("/api/pedido/tramitar")
    Call<Void> tramitarPedido(@Query("idUsuario") long idUsuario);
    @GET("/api/pedido/usuario")
    Call<List<Pedido>> obtenerPedidosPorUsuario(@Query("idUsuario") Long idUsuario);
    @GET("/api/pedido/detalles")
    Call<DetallePedidoResponse> obtenerDetallesDePedido(@Query("idPedido") Long idPedido);

}