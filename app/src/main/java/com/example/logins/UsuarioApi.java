package com.example.logins;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UsuarioApi {

    // Endpoint para login
    @FormUrlEncoded
    @POST("/api/usuarios/login")
    Call<String> login(
            @Field("usuario") String usuario,
            @Field("contrasena") String contrasena,
            @Field("origen_app") String origenApp
    );

    // Endpoint para registro de usuario
    @FormUrlEncoded
    @POST("/api/usuarios/registro")
    Call<String> registrarUsuario(
            @Field("nombrecompleto") String nombrecompleto,
            @Field("correo") String correo,
            @Field("telefono") String telefono,
            @Field("usuario") String usuario,
            @Field("contrasena") String contrasena,
            @Field("origen_app") String origenApp
    );
}
