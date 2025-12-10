/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionBD;

import java.sql.Connection;

/**
 *
 * @author Tadeo Nuñez
 */
public class PruebaConexion {
    public static void main(String[] args) {
        
        // Llamar al método estático para intentar obtener la conexión
        Connection miConexion = Conexion.getConnection();
        
        // Verificar si el objeto Connection es diferente de null
        if (miConexion != null) {
            System.out.println("✅ ¡Conexión verificada y lista para usar!");
            
            // Aquí puedes empezar a ejecutar consultas (SELECT, INSERT, etc.)
            // ...
            
            // Es buena práctica cerrar la conexión cuando termines
            try {
                miConexion.close();
                System.out.println("Cerrando la conexión.");
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
            
        } else {
            System.out.println("❌ ¡Error! La conexión NO fue exitosa.");
            System.out.println("Revisa los mensajes de error en la consola y tus credenciales/URL.");
        }
    }
}
