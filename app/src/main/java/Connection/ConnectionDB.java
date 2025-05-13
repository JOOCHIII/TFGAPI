package Connection;

import android.os.StrictMode;
import android.util.Log;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ConnectionDB {
    private String ip = "192.168.0.31";
    private String basedatos = "tfg";
    private String usuario = "sa";
    private String password = "1234";
    private String puerto = "1433";

    public java.sql.Connection connect() {
        java.sql.Connection conexion = null;
        String connectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + puerto + ";"
                + "databaseName=" + basedatos + ";"
                + "user=" + usuario + ";"
                + "password=" + password + ";"
                + "encrypt=false;trustServerCertificate=true;";

        try {
            // Habilitar política de hilos (para evitar error en conexiones en el hilo principal)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Cargar el driver JDBC de SQL Server
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            // Establecer conexión
            conexion = DriverManager.getConnection( connectionURL);
            Log.i("ConexionBD", "Conexión a la base de datos establecida correctamente");

        } catch (ClassNotFoundException e) {
            Log.e("Error", "No se encontró el driver JDBC: " + e.getMessage());
        } catch (Exception e) {
            Log.e("Error", "No se pudo conectar a la base de datos: " + e.getMessage());
        }

        return conexion;
    }

    }
