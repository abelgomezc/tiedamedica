/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abel Gomez
 */
public class modelMedicamento extends Medicamento {

    modelConexion mpgc = new modelConexion();

    public modelMedicamento() {
    }

    public modelMedicamento(int id, String nombre, String tipo, int cantidad, String distribuidor, String sucursal) {
        super(id, nombre, tipo, cantidad, distribuidor, sucursal);
    }

    public boolean setMediamento() {
        String sql = "INSERT INTO medicamentos (nombre,tipo,cantidad,distribuidor,sucursal) ";
        sql += "VALUES (?,?,?,?,?)";

        try {
            PreparedStatement ps = mpgc.conn.prepareStatement(sql);
            // ps.setInt(1, getId());
            ps.setString(1, getNombre());
            ps.setString(2, getTipo());

            ps.setInt(3, getCantidad());
            ps.setString(4, getDistribuidor());
            ps.setString(5, getSucursal());
            
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(modelMedicamento.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public List<Medicamento> getMedicamentos() {

        List<Medicamento> listaMedicamentos = new ArrayList<Medicamento>();

        //  String sql = "select * from persona";
        String sql = "SELECT * FROM medicamentos ORDER BY id ASC";
        ResultSet rs = mpgc.consulta(sql);
     
        try {
            while (rs.next()) {

                Medicamento medicamento = new Medicamento();
                medicamento.setId(rs.getInt(1));
                medicamento.setNombre(rs.getString(2));
                medicamento.setTipo(rs.getString(3));
                medicamento.setCantidad(rs.getInt(4));
                medicamento.setDistribuidor(rs.getString(5));
                medicamento.setSucursal(rs.getString(6));

                listaMedicamentos.add(medicamento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(modelMedicamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();//cierro conexion BD
        } catch (SQLException ex) {
            Logger.getLogger(modelMedicamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaMedicamentos;
    }

    public void setCantidad(String cantidad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
