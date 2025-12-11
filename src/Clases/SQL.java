/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import ConexionBD.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;

/**
 *
 * @author Tadeo Nuñez
 */
public class SQL {
    public static final String LlenarTabla="select a.Nombre as NOMBRE,a.Edad AS EDAD,a.Sexo AS SEXO\n" +
            ",a.Estado AS ESTADO,e.NombreComun AS ESPECIE,ci.Nombre AS VETERINARIO,\n" +
            "h.NombreHabitat AS HABITAT,o.NombreAlimento as ALIMENTACION\n" +
            "from Animales a\n" +
            "inner join Especies e on e.IDEspecie=a.IDEspecie\n" +
            "inner join AtencionVeterinaria c on c.IDAnimal=a.IDAnimal\n" +
            "inner join Empleados ci on ci.IDEmpleado=c.IDEmpleado\n" +
            "inner join Habitats h on h.IDHabitat=a.IDHabitat\n" +
            "inner join Alimentaciones m on m.IDAnimal=a.IDAnimal\n" +
            "inner join Alimentos o on o.IDAlimento=m.IDAlimento";
    public void Insertar(String nombre, int edad, String sexo, String estado,
                               String nombreComun, String veterinario, String habitad, String alimento) {
        String sql = "{ call sp_insertar(?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection conn = Conexion.getConnection(); 
             CallableStatement cstmt = conn.prepareCall(sql)) { 
            cstmt.setString(1, nombre);
            cstmt.setInt(2, edad);
            cstmt.setString(3, sexo);
            cstmt.setString(4, estado);
            cstmt.setString(5, nombreComun);
            cstmt.setString(6, veterinario);
            cstmt.setString(7, habitad);
            cstmt.setString(8, alimento);
            cstmt.execute();
            System.out.println("Procedimiento ejecutado para: " + nombre);
        } catch (SQLException e) {
            System.err.println("Error al insertar " + nombre + ": " + e.getMessage());
        }
    }
}
