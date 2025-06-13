package com.example.logins;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AdministradorApi {

    @FormUrlEncoded
    @POST("/api/administradores/login")
    Call<LoginResponse> loginAdmin(
            @Field("usuarioadmin") String usuario,
            @Field("contrasenaadmin") String contrasena,
            @Field("origen_app") String origenApp
    );

    @FormUrlEncoded
    @POST("/api/administradores/registro")
    Call<String> registrarAdmin(
            @Field("nombrecompletoadmin") String nombrecompleto,
            @Field("correoadmin") String correo,
            @Field("telefonoadmin") String telefono,
            @Field("usuarioadmin") String usuario,
            @Field("contrasenaadmin") String contrasena,
            @Field("origen_app") String origenApp
    );
    @GET("/api/pedido/todos")
    Call<List<PedidoAdmin>> getPedidosAdmin();

    @PUT("/api/pedido/cambiar-estado")
    Call<Void> actualizarEstadoPedidoAdmin(@Query("idPedido") int idPedido, @Query("nuevoEstado") String nuevoEstado);


}