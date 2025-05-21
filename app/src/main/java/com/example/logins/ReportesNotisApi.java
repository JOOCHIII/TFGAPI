package com.example.logins;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReportesNotisApi {

    @POST("api/reporte/crear")
    Call<String> crearReporte(@Body ReporteDTO reporte);

    @GET("api/reporte/usuario")
    Call<List<ReporteDTO>> obtenerReportesPorUsuario(@Query("id_usuario") long idUsuario);

    @GET("api/notificacion/usuario")
    Call<List<Notificacion>> getNotificacionesUsuario(
            @Query("id_usuario") long idUsuario,
            @Query("tipoDestino") String tipoDestino
    );

    @PUT("api/notificacion/{id}/leida")
    Call<String> marcarNotificacionLeida(@Path("id") int idNotificacion);

}
