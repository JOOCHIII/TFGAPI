package com.example.logins;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import java.util.List;

public interface CarritoApi {
    @GET("/api/carrito/usuario")
    Call<List<Carrito>> getCarritoPorUsuario(@Query("idUsuario") long idUsuario);

    @DELETE("/api/carrito/eliminar")
    Call<Void> eliminarDelCarrito(@Query("idUsuario") long idUsuario, @Query("idProducto") long idProducto);
    @POST("/api/carrito/agregar")
    Call<String> agregarAlCarrito(@Query("idUsuario") long idUsuario,
                                  @Query("idProducto") long idProducto,
                                  @Query("cantidad") int cantidad);


    @PUT("api/carrito/actualizarCantidad")
    Call<Void> actualizarCantidad(
            @Query("idUsuario") Long idUsuario,
            @Query("idProducto") Long idProducto,
            @Query("cantidad") int cantidad
    );
}

