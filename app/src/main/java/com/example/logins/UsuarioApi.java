package com.example.logins;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioApi {
    @POST("/api/usuarios/login")
    Call<LoginResponse> login(
            @Query("usuario") String usuario,
            @Query("contrasena") String contrasena,
            @Query("origen_app") String origenApp
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
    @GET("/api/usuarios/buscar/{usuario}")
    Call<Usuario> getUsuarioPorNombre(@Path("usuario") String usuario);



    @PUT("api/usuarios/{id}")
    Call<Void> actualizarUsuario(@Path("id") long id, @Body Usuario usuario);


}