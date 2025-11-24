/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteDao {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Registrar
    public boolean RegistrarCliente(Cliente cl){
        String sql = "INSERT INTO clientes (dni, nombre, telefono, direccion) VALUES (?,?,?,?)";
        try {
            con = cn.establecerconexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, cl.getDni());
            ps.setString(2, cl.getNombre());
            ps.setString(3, cl.getTelefono());
            ps.setString(4, cl.getDireccion());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }finally{
            try { con.close(); } catch (SQLException e) {}
        }
    }

    // Listar
    public List<Cliente> ListarCliente(){
        List<Cliente> ListaCl = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try {
            con = cn.establecerconexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {               
               Cliente cl = new Cliente();
               cl.setId(rs.getInt("id"));
               cl.setDni(rs.getString("dni"));
               cl.setNombre(rs.getString("nombre"));
               cl.setTelefono(rs.getString("telefono"));
               cl.setDireccion(rs.getString("direccion"));
               ListaCl.add(cl);
           }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaCl;
    }

    // Eliminar
    public boolean EliminarCliente(int id){
       String sql = "DELETE FROM clientes WHERE id = ?";
       try {
           con = cn.establecerconexion();  
           ps = con.prepareStatement(sql);
           ps.setInt(1, id);
           ps.execute();
           return true;

       } catch (SQLException e) {
           System.out.println(e.toString());
           return false;

       }finally{
           try { con.close(); } catch (SQLException ex) {}
       }
    }

    // Modificar
    public boolean ModificarCliente(Cliente cl){
        String sql = "UPDATE clientes SET dni=?, nombre=?, telefono=?, direccion=? WHERE id=?";

        try {
            con = cn.establecerconexion();  
            ps = con.prepareStatement(sql);
            ps.setString(1, cl.getDni());
            ps.setString(2, cl.getNombre());
            ps.setString(3, cl.getTelefono());
            ps.setString(4, cl.getDireccion());
            ps.setInt(5, cl.getId());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;

        }finally{
            try { con.close(); } catch (SQLException e) {}
        }
    }

    // Buscar por DNI
    public Cliente Buscarcliente(String dni){   
       Cliente cl = new Cliente();
       String sql = "SELECT * FROM clientes WHERE dni = ?";

       try {
           con = cn.establecerconexion();
           ps = con.prepareStatement(sql);
           ps.setString(1, dni);
           rs = ps.executeQuery();

           if (rs.next()) {
               cl.setId(rs.getInt("id"));
               cl.setDni(rs.getString("dni"));
               cl.setNombre(rs.getString("nombre"));
               cl.setTelefono(rs.getString("telefono"));
               cl.setDireccion(rs.getString("direccion"));
           }

       } catch (SQLException e) {
           System.out.println(e.toString());
       }
       return cl;
       }  
    }