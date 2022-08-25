package org.rivera.java.jdbc.repositorio;

import org.rivera.java.jdbc.models.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositorioImpl implements Repositorio<Categoria>{
  private Connection conn;

  public CategoriaRepositorioImpl() {
  }
  //DEBO CONECTAR DE ALGÚN MODO, ASI QUE EL SET ES OTRA OPCIÓN
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  public CategoriaRepositorioImpl(Connection conn) {
    this.conn = conn;
  }

  private static Categoria createCategory(ResultSet rs) throws SQLException {
    Categoria c = new Categoria();
    c.setId( rs.getLong("id") );
    c.setName( rs.getString("nombre") );
    return c;
  }

  @Override
  public List<Categoria> findAll() throws SQLException {
    List<Categoria> categories = new ArrayList<>();
    try( Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM categorias")) {
      while( rs.next() ) {
        Categoria c = createCategory(rs);
        categories.add(c);
      }
    }
    return categories;
  }


  @Override
  public Categoria byId(Long id) throws SQLException {
    Categoria category = null;
    try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categorias AS c WHERE c.id=?")) {
      stmt.setLong(1, id);
      try( ResultSet rs = stmt.executeQuery() ) {
        if( rs.next() ) {
          category = createCategory( rs );
        }
      }
    }
    return category;
  }

  @Override
  public Categoria save(Categoria categoria) throws SQLException {
    String sql;
    if( categoria.getId() != null && categoria.getId() > 0 ) {
      sql = "UPDATE categorias SET nombre=? WHERE id=?";
    } else {
      sql = "INSERT INTO categorias(nombre) VALUES(?)";
    }
    //Además de hacer la sentencia necesito que regrese el "ID" generado
    try( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) ) {
      stmt.setString(1, categoria.getName());
      if( categoria.getId() != null && categoria.getId() > 0 ) {
        stmt.setLong(2, categoria.getId());
      }
      stmt.executeUpdate();
      //If solamente para el INSERT
      if( categoria.getId() == null ) {
        try( ResultSet rs = stmt.getGeneratedKeys() ) {
          if( rs.next() ) {
            categoria.setId( rs.getLong(1) );
          }
        }
      }
    }
    return categoria;
  }

  @Override
  public void delete(Long id) throws SQLException {
    try( PreparedStatement stmt = conn.prepareStatement("DELETE FROM categorias WHERE id=?") ) {
      stmt.setLong(1, id);
      stmt.executeUpdate();
    }
  }
}
