package ar.edu.unju.escmi.dao.imp;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.model.Producto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class ProductoDaoImp implements IProductoDao {

    private final EntityManagerFactory emf = EmfSingleton.getEntityManagerFactory();
    private static ProductoDaoImp instancia;

    private ProductoDaoImp() {
    }

    public static synchronized ProductoDaoImp getInstance() {
        if (instancia == null) {
            instancia = new ProductoDaoImp();
        }
        return instancia;
    }

    @Override
    public void guardarProducto(Producto producto) {
        EntityManager em = emf.createEntityManager();
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

    @Override
    public Producto buscarProductoPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Producto> listarProductosActivos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p WHERE p.estado = true", Producto.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean eliminarProductoLogico(Long id) {
        EntityManager em = emf.createEntityManager();
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

    @Override
    public boolean modificarPrecioProducto(Long id, double nuevoPrecio) {
        EntityManager em = emf.createEntityManager();
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
