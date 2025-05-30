package com.example.logins;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProductosApi {

        @GET("/api/productos")
        Call<List<Productos>> getProductos();

        @POST("/api/productos")
        Call<Productos> crearProducto(@Body Productos producto);
}
