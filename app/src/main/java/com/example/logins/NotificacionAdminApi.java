package com.example.logins;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificacionAdminApi {

    @GET("/api/pedido/notificaciones-admin")
    Call<List<NotificacionPedidoAdmin>> getNotificacionesAdmin();

    @PUT("/api/pedido/notificacion-admin/{id}/leida")
    Call<Void> marcarNotificacionAdminLeida(@Path("id") Long id);

}

