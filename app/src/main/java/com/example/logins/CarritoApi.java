package com.example.logins;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CarritoApi {

    // Obtener el carrito de un usuario
    @GET("/api/carrito/usuario")
    Call<List<Carrito>> getCarritoPorUsuario(@Query("idUsuario") long idUsuario);

    // Agregar un producto al carrito
    @POST("/api/carrito/agregar")
    Call<String> agregarAlCarrito(
            @Query("idUsuario") long idUsuario,
            @Query("idProducto") long idProducto,
            @Query("talla") String talla,
            @Query("cantidad") int cantidad
    );

    // Eliminar un producto del carrito
    @DELETE("/api/carrito/eliminar")
    Call<Void> eliminarDelCarrito(
            @Query("idUsuario") long idUsuario,
            @Query("idProducto") long idProducto,
            @Query("talla") String talla
    );

    // Actualizar la cantidad de un producto en el carrito
    @PUT("/api/carrito/actualizarCantidad")
    Call<Void> actualizarCantidad(
            @Query("idUsuario") long idUsuario,
            @Query("idProducto") long idProducto,
            @Query("talla") String talla,
            @Query("cantidad") int cantidad
    );
}
