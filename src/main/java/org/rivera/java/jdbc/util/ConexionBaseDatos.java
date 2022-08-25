package org.rivera.java.jdbc.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
//Una sola conexión para toda la aplicación(Depende el tipo de aplicación)
public class ConexionBaseDatos {
  //Lo primero para POOL DE CONEXIONES es agregarlo en "pom", posteriormente hacer esta configuración
  private static String url = "jdbc:mysql://localhost:3306/java_curso";
  private static String username = "root";
  private static String password = "sasa";
  private static BasicDataSource pool;
//Ahora implemento la conexión SingleTon con "BasicDataSource". S-35
  public static BasicDataSource getInstance() throws SQLException {
    if( pool == null ) {
      pool = new BasicDataSource();
      pool.setUrl(url);
      pool.setUsername(username);
      pool.setPassword(password);
      pool.setInitialSize(3);
      pool.setMinIdle(3);
      pool.setMaxIdle(8);
      pool.setMaxTotal(8);
    }
    return pool;
  }

  //Método que uso para hacer conexión SingleTon con Pool de conexiones
  public static Connection getConnection() throws SQLException {
    return getInstance().getConnection();
  }

}
