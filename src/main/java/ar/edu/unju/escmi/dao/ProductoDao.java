package main.java.ar.edu.unju.escmi.dao;

import main.java.ar.edu.unju.escmi.config.EmfSingleton;
import main.java.ar.edu.unju.escmi.modelo.Producto;

import javax.persistence.EntityManager;
import java.util.List;

public class ProductoDao {

    private static ProductoDao instancia;

    private ProductoDao() {}

    public static synchronized ProductoDao getInstance() {
        if (instancia == null) {
            instancia = new ProductoDao();
        }
        return instancia;
    }

    public void guardarProducto(Producto producto) {
        EntityManager em = EmfSingleton.getInstance().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
            System.out.println("[ÉXITO] Producto guardado correctamente en la base de datos.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("[ERROR] No se pudo guardar el producto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Producto buscarProductoPorId(Long id) {
        EntityManager em = EmfSingleton.getInstance().createEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public List<Producto> listarProductosActivos() {
        EntityManager em = EmfSingleton.getInstance().createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p WHERE p.estado = true", Producto.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // Opción 6 del menú: Eliminación lógica (cambiar estado a false)
    public boolean eliminarProductoLogico(Long id) {
        EntityManager em = EmfSingleton.getInstance().createEntityManager();
        try {
            em.getTransaction().begin();
            Producto producto = em.find(Producto.class, id);
            if (producto != null && producto.isEstado()) {
                producto.setEstado(false);
                em.merge(producto);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("[ERROR] Al eliminar el producto: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Opción 8 del menú: Modificar precio de producto
    public boolean modificarPrecioProducto(Long id, double nuevoPrecio) {
        EntityManager em = EmfSingleton.getInstance().createEntityManager();
        try {
            em.getTransaction().begin();
            Producto producto = em.find(Producto.class, id);
            if (producto != null && producto.isEstado()) {
                producto.setPrecioUnitario(nuevoPrecio);
                em.merge(producto);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("[ERROR] Al modificar el precio: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}