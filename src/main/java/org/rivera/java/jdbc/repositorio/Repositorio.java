package org.rivera.java.jdbc.repositorio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
//Generic para cualquier objeto
public interface Repositorio<T> {
  List<T> findAll() throws SQLException;
  T byId(Long id) throws SQLException;
  T save(T t) throws SQLException;
  void delete(Long id) throws SQLException;

  //Lo añado porque necesito que esté disponible en la interfaz no solo en las implementaciones
   void setConn(Connection conn);

}
