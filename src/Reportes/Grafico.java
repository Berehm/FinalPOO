/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

/**
 *
 * @author USER
 */

import Conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class Grafico {

    public static void Graficar(String fecha) {
        Connection con;
        Conexion cn = new Conexion();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = "SELECT total FROM ventas WHERE fecha = ?";
            con = cn.establecerconexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();

            DefaultPieDataset dataset = new DefaultPieDataset();
            int contador = 1;

            while (rs.next()) {
                double total = rs.getDouble("total");
                dataset.setValue("Venta " + contador, total);
                contador++;
            }

            JFreeChart jf = ChartFactory.createPieChart(
                "Reporte de Ventas del día " + fecha,
                dataset,
                true,   // incluir leyenda
                true,   // tooltips
                false   // URLs
            );

            ChartFrame f = new ChartFrame("Total de Ventas por Día", jf);
            f.setSize(1000, 500);
            f.setLocationRelativeTo(null);
            f.setVisible(true);

        } catch (SQLException e) {
            System.out.println("Error en gráfico: " + e.toString());
        }
    }
}