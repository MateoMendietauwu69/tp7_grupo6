package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Producto;
import java.util.List;

public interface IProductoDao {

    void guardarProducto(Producto producto);

    Producto buscarProductoPorId(Long id);

    List<Producto> listarProductosActivos();

    boolean eliminarProductoLogico(Long id);

    boolean modificarPrecioProducto(Long id, double nuevoPrecio);
}
