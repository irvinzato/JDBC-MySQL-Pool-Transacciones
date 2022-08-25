package org.rivera.java.jdbc.service;

import org.rivera.java.jdbc.models.Categoria;
import org.rivera.java.jdbc.models.Producto;

import java.sql.SQLException;
import java.util.List;

//Como tengo dos tablas que están muy relacionadas puedo tener aquí los métodos de cada una de ellas
public interface Service {
  //Mismos métodos que Repositorio prácticamente
  List<Producto> findAllProducts() throws SQLException;
  Producto byIDProduct(Long id) throws SQLException;
  Producto saveProduct(Producto producto) throws SQLException;
  void deleteProduct(Long id) throws SQLException;

  List<Categoria> findAllCategories() throws SQLException;
  Categoria byIdCategory(Long id) throws SQLException;
  Categoria saveCategory(Categoria categoria) throws SQLException;
  void deleteCategory(Long id) throws SQLException;

  //Este método es la representación de todo lo que tenía en el MAIN
  void saveProductWithCategory(Producto producto, Categoria categoria) throws SQLException;
}
