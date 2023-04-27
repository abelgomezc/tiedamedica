/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abel Gomez
 */
public class modelConexion {
    
    
    
    String url = "jdbc:postgresql://localhost/PedidoMedicamento";
    String usuario = "postgres";
    String clave = "narexd";

    Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public modelConexion() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(modelConexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            conn = DriverManager.getConnection(url, usuario, clave);

        } catch (SQLException ex) {
            Logger.getLogger(modelConexion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ResultSet consulta(String sql) {

        try {
            Statement at = conn.createStatement();
            return at.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(modelConexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean accion(String sql) {
        //INSERT-UPDATE-DELETE

        boolean correcto;

        try {
            Statement at = conn.createStatement();
            at.execute(sql);
            at.close();//Cierro la conexion
            correcto = true;

        } catch (Exception e) {
            Logger.getLogger(modelConexion.class.getName()).log(Level.SEVERE, null, e);
            correcto = false;
        }
        return correcto;
    }
    
}



















//CREATE TABLE medicamentos(
//   id serial PRIMARY KEY,
//   nombre VARCHAR(50),
//   tipo VARCHAR(50),
//   cantidad int ,
//   distribuidor varchar(60),
//   sucursal VARCHAR(60)
//);
