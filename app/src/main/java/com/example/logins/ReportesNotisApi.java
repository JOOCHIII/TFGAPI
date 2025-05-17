package com.example.logins;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportesNotisApi {
    @POST("/api/reporte/crear")
    Call<String> crearReporte(@Body ReporteDTO reporte);
}
