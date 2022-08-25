package org.rivera.java.jdbc.repositorio;

import org.rivera.java.jdbc.models.Categoria;
import org.rivera.java.jdbc.models.Producto;
import org.rivera.java.jdbc.util.ConexionBaseDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioImplForanea implements Repositorio<Producto>{
  //POOL DE CONEXIONES Y TRANSACCIONES
  private Connection conn;

  public ProductoRepositorioImplForanea() {
  }
  //DEBO CONECTAR DE ALGÚN MODO, ASI QUE EL SET ES OTRA OPCIÓN
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  public ProductoRepositorioImplForanea(Connection conn) {
    this.conn = conn;
  }

  private Producto createProduct(ResultSet rs) throws SQLException {
    Producto p = new Producto();
    p.setId( rs.getLong("id") );
    p.setName( rs.getString("nombre") );
    p.setPrice( rs.getInt("precio") );
    p.setRegisterDate( rs.getDate("fecha_registro") );
    p.setSku( rs.getString("sku") );
    Categoria category = new Categoria();
    category.setId( rs.getLong("categoria_id") );
    category.setName( rs.getString("categoria") ); //Es la columna "nombre de categoría" pero le di alias "categoria" al hacer la consulta
    p.setCategoria(category);
    return p;
  }
  //CRUD COMPLETO CON LLAVE FORÁNEA - POOL DE CONEXIONES Y TRANSACCIONES
  @Override
  public List<Producto> findAll() throws SQLException {
    List<Producto> products = new ArrayList<>();
    try( Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT p.*, c.nombre AS categoria FROM productos AS p " +
                 "INNER JOIN categorias AS c ON(p.categoria_id = c.id)")) {
      while( rs.next() ) {
        Producto p = createProduct( rs );
        products.add(p);
      }
    }
    return products;
  }

  @Override
  public Producto byId(Long id) throws SQLException {
    Producto product = null;
    try( PreparedStatement stmt = conn.prepareStatement("SELECT p.*, c.nombre AS categoria FROM productos AS p " +
                                  "INNER JOIN categorias AS c ON(p.categoria_id = c.id) WHERE p.id = ?" )) { //Debo ser explícito con la tabla y columna que me refiero
      stmt.setLong(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          product = createProduct(rs);
        }
      }
    }
    return product;
  }
//AHORA QUIERO QUE ME DEVUELVA EL PRODUCTO SOLO CUANDO ES UN INSERT
  @Override
  public Producto save(Producto producto) throws SQLException {
    String query;
    if ( producto.getId() != null && producto.getId() > 0 ) {
      query = "UPDATE productos SET nombre=?, precio=?, categoria_id=?, sku=? WHERE id=?";
    } else {
      query = "INSERT INTO productos(nombre, precio, categoria_id, sku, fecha_registro) VALUES(?, ?, ?, ?, ?)";
    }
    try( PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS) ) { //Utilizo el 2do parámetro
      stmt.setString(1, producto.getName());
      stmt.setLong(2, producto.getPrice());
      stmt.setLong(3, producto.getCategoria().getId());
      stmt.setString(4, producto.getSku());
      if ( producto.getId() != null && producto.getId() > 0 ) {
        stmt.setLong(5, producto.getId());
      } else {
        stmt.setDate(5, new Date(producto.getRegisterDate().getTime()));
      }
      stmt.executeUpdate();
      //Para obtener el último ID generado solo cuando es un INSERT
      if( producto.getId() == null ) {
        try( ResultSet rs = stmt.getGeneratedKeys() ) {
          if( rs.next() ) {
            producto.setId( rs.getLong(1) );
          }
        }
      }
      return producto;
    }
  }

  @Override
  public void delete(Long id) throws SQLException {
    try( PreparedStatement stmt = conn.prepareStatement("DELETE FROM productos WHERE id=?") ) {
      stmt.setLong(1, id);
      stmt.executeUpdate();
    }
  }
}
