package com.example.logins  ;

import com.example.logins.CarritoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CarritoApi {

    @POST("api/carrito/agregar")
    Call<String> agregarAlCarrito(
            @Query("idUsuario") long idUsuario,
            @Query("idProducto") long idProducto,
            @Query("talla") String talla,
            @Query("cantidad") int cantidad
    );

    @GET("api/carrito/usuario")
    Call<List<CarritoDTO>> obtenerCarritoUsuario(
            @Query("idUsuario") long idUsuario
    );

    @DELETE("api/carrito/eliminar")
    Call<String> eliminarDelCarrito(
            @Query("idUsuario") long idUsuario,
            @Query("idProducto") long idProducto,
            @Query("talla") String talla
    );

    @PUT("api/carrito/actualizarCantidad")
    Call<String> actualizarCantidad(
            @Query("idUsuario") long idUsuario,
            @Query("idProducto") long idProducto,
            @Query("talla") String talla,
            @Query("cantidad") int cantidad
    );
}
