/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

/**
 *
 * @author USER
 */
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
/**
 *
 * @author USER
 */
public class Conexion {
    Connection conectar=null;
    
    String usuario = "usersql";
    String contrasenia ="root";
    String bd ="dbsupermercado";
    String ip ="localhost";
    String puerto= "1433";
    String cadena = "jdbc:sqlserver://"+ip+":"+puerto+"/"+bd;

    public Connection establecerconexion(){
     try {   
       String cadena = "jdbc:sqlserver://" + ip + ":" + puerto + 
                          ";databaseName=" + bd + 
                          ";encrypt=false;trustServerCertificate=true";
       conectar= DriverManager.getConnection(cadena,usuario,contrasenia);
       
     }catch (Exception e){
         
       }
       return conectar;
    }    

  }   