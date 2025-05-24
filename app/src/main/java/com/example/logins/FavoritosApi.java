package com.example.logins;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FavoritosApi {
    @POST("api/favoritos/agregar")
    Call<String> agregarFavorito(@Query("idUsuario") long idUsuario, @Query("idProducto") long idProducto);

    @DELETE("api/favoritos/eliminar")
    Call<String> eliminarFavorito(@Query("idUsuario") long idUsuario, @Query("idProducto") long idProducto);

    @GET("api/favoritos/usuario")
    Call<List<Productos>> obtenerFavoritos(@Query("idUsuario") long idUsuario);


}

