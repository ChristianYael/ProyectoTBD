/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Player
 */
public class Conexion {
    //private static String url = "jdbc:sqlserver://DESKTOP-2FF6MOA;databaseName=Zoologico;encrypt=false"; //chris
    private static String url = "jdbc:sqlserver://localhost:1433;databaseName=Zoologico;encrypt=false"; //tadeo
    private static String user = "sa";
    private static String password = "1234";
    private static Connection con = null;

    
    public static Connection getConnection(){
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexion exitosa");
        }catch (SQLException ex) {
            System.getLogger(Conexion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return con;
    }//getConnection
}
