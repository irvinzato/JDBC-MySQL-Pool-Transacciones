package org.rivera.java.jdbc.service;

import org.rivera.java.jdbc.models.Categoria;
import org.rivera.java.jdbc.models.Producto;
import org.rivera.java.jdbc.repositorio.CategoriaRepositorioImpl;
import org.rivera.java.jdbc.repositorio.ProductoRepositorioImplForanea;
import org.rivera.java.jdbc.repositorio.Repositorio;
import org.rivera.java.jdbc.util.ConexionBaseDatos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CatalogService implements Service{
  private Repositorio<Producto> productRepository;
  private Repositorio<Categoria> categoryRepository;

  public CatalogService() {
    this.productRepository = new ProductoRepositorioImplForanea();
    this.categoryRepository = new CategoriaRepositorioImpl();
  }

  //Los métodos que son de lectura es solo llamarlos
  @Override
  public List<Producto> findAllProducts() throws SQLException {
    //En cada método necesito que vaya a buscar una conexión al POOL de conexiones
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      productRepository.setConn(conn);
      return productRepository.findAll();
    }
  }

  @Override
  public Producto byIDProduct(Long id) throws SQLException {
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      productRepository.setConn(conn);
      return productRepository.byId(id);
    }
  }

  //Los métodos que son de escritura en DB deben implementar el "commit"-"rollback"
  @Override
  public Producto saveProduct(Producto producto) throws SQLException {
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      productRepository.setConn(conn);
      if( conn.getAutoCommit() ) {  //Aquí inicia una conexión con transacciones
        conn.setAutoCommit(false);
      }
      Producto newProduct = null;
      try{
        newProduct = productRepository.save(producto);
        conn.commit();
      }catch(SQLException e) {
        conn.rollback();
        e.printStackTrace();
      }
        return newProduct;
    }
  }

  @Override
  public void deleteProduct(Long id) throws SQLException {
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      productRepository.setConn(conn);
      if( conn.getAutoCommit() ) {
        conn.setAutoCommit(false);
      }
      try{
        productRepository.delete(id);
        conn.commit();
      }catch(SQLException e) {
        conn.rollback();
        e.printStackTrace();
      }
    }
  }

  @Override
  public List<Categoria> findAllCategories() throws SQLException {
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      categoryRepository.setConn(conn);
      return categoryRepository.findAll();
    }
  }

  @Override
  public Categoria byIdCategory(Long id) throws SQLException {
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      categoryRepository.setConn(conn);
      return categoryRepository.byId(id);
    }
  }

  @Override
  public Categoria saveCategory(Categoria categoria) throws SQLException {
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      categoryRepository.setConn(conn);
      if( conn.getAutoCommit() ) {
        conn.setAutoCommit(false);
      }
      Categoria newCategory = null;
      try{
        newCategory = categoryRepository.save(categoria);
        conn.commit();
      }catch(SQLException e) {
        conn.rollback();
        e.printStackTrace();
      }
      return newCategory;
    }
  }

  @Override
  public void deleteCategory(Long id) throws SQLException {
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      categoryRepository.setConn(conn);
      if( conn.getAutoCommit() ) {
        conn.setAutoCommit(false);
      }
      try{
        categoryRepository.delete(id);
        conn.commit();
      }catch(SQLException e) {
        conn.rollback();
        e.printStackTrace();
      }
    }
  }

  @Override
  public void saveProductWithCategory(Producto producto, Categoria categoria) throws SQLException {
    //Aquí van ambas conexiones porque interactúan los 2
    try(Connection conn = ConexionBaseDatos.getConnection()) {
      productRepository.setConn(conn);
      categoryRepository.setConn(conn);
      if( conn.getAutoCommit() ) {
        conn.setAutoCommit(false);
      }
      try{
        Categoria newCategory = categoryRepository.save(categoria);
        producto.setCategoria(newCategory);
        productRepository.save(producto);
        conn.commit();
      }catch(SQLException e) {
        conn.rollback();
        e.printStackTrace();
      }
    }
  }
}
