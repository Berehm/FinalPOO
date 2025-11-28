/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    Connection conectar = null;

    String usuario = "usersql";
    String contrasenia = "root";
    String bd = "SistemaVentas";
    String ip = "localhost";
    String puerto = "1433";

    public Connection establecerconexion() {
        try {

            // 🔹Cargar el driver SQL Server (OBLIGATORIO)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 🔹Cadena correcta para SQL Server
            String cadena = "jdbc:sqlserver://" + ip + ":" + puerto 
                    + ";databaseName=" + bd 
                    + ";encrypt=false;trustServerCertificate=true";

            // 🔹Conectando
            conectar = DriverManager.getConnection(cadena, usuario, contrasenia);

            System.out.println("CONECTADO A SQL SERVER");

        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: Falta el driver JDBC → " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR SQL → " + e.getMessage());
        }

        return conectar;
    }
}