/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
 
    // Método para listar todos los clientes
    public List<Cliente> ListarCliente() {
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
            System.out.println("Error al listar clientes: " + e.toString());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        
        return ListaCl;
    }
    
    // Método para registrar un nuevo cliente
    public boolean RegistrarCliente(Cliente cl) {
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
            System.out.println("Error al registrar cliente: " + e.toString());
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    // Método para modificar un cliente
    public boolean ModificarCliente(Cliente cl) {
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
            System.out.println("Error al modificar cliente: " + e.toString());
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    // Método para eliminar un cliente
    public boolean EliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        
        try {
            con = cn.establecerconexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.toString());
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }
    
    // Método para buscar cliente por DNI (usado en ventas)
    public Cliente BuscarCliente(String dni) {
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
            System.out.println("Error al buscar cliente: " + e.toString());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        
        return cl;
    }
}