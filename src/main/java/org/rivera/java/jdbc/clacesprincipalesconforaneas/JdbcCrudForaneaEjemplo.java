package org.rivera.java.jdbc.clacesprincipalesconforaneas;

import org.rivera.java.jdbc.models.Categoria;
import org.rivera.java.jdbc.models.Producto;
import org.rivera.java.jdbc.service.CatalogService;
import org.rivera.java.jdbc.service.Service;

import java.sql.SQLException;
import java.util.Date;


public class JdbcCrudForaneaEjemplo {
  public static void main(String[] args) throws SQLException {
    System.out.println("POOL DE CONEXIONES Y TRANSACCIONES(TODO O NADA)");
    System.out.println("PRIMERO CREO UNA NUEVA CATEGORÍA PARA POSTERIORMENTE UTILIZARLA");
    System.out.println("IMPLEMENTADO CON CAPA DE SERVICIO");

    Service service = new CatalogService();
    System.out.println("---- Búsqueda con todos los atributos de la tabla productos ----");
    service.findAllProducts().forEach(System.out::println);

    System.out.println("---- Creo categoría nueva que quiero insertar en producto ----");
    Categoria category = new Categoria();
    category.setName("Iluminación");

    System.out.println("---- Creación de nuevo producto y llamado a método que guarda producto con categoría ----");
    Producto product = new Producto();
    product.setName("Barra Led RGBW");
    product.setPrice(2000);
    product.setRegisterDate(new Date());
    product.setSku("abcdefgh12");

    service.saveProductWithCategory(product, category);

    System.out.println("Producto guardado con éxito: " + product.getId() + ", " + product.getName());
    service.findAllProducts().forEach(System.out::println);

  }
}
