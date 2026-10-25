package ar.edu.unju.escmi.dao.imp;

import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.model.Factura;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class FacturaDaoImp implements IFacturaDao {
    
    private EntityManagerFactory emf = EmfSingleton.getEntityManagerFactory();

    @Override
    public void guardar(Factura factura) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(factura);
            tx.commit();
        }catch(Exception e) {
            if(tx.isActive()) tx.rollback();
            System.err.println("✘ Error al guardar factura: " + e.getMessage());
        }finally {
            em.close();
        }
    }

    @Override
    public Factura buscarporid(long id) {
        EntityManager em = emf.createEntityManager();

        try {
            List<Factura> resultado = em.createQuery(
                "SELECT DISTINCT f FROM Factura f" +
                "LEFT JOIN FETCH f.detalles d" +
                "LEFT JOIN FETCH d.producto" +
                "JOIN FETCH f.cliente" +
                "WHERE f.id = :id",
                Factura.class
            )
            .setParameter("id", id)
            .getResultList();

            return resultado.isEmpty() ? null : resultado.get(0);
        }finally {
            em.close();
        }
    }

    @Override 
    public List<Factura> listarTodas() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                "SELECT DISTINCT f FROM Factura f" +
                "JOIN FETCH f.cliente" +
                "LEFT JOIN FETCH f.detalles" +
                "LEFT JOIN FETCH d.producto" +
                "ORDER BY f.id",
                Factura.class
            ).getResultList();
        }finally {
            em.close();
        }
    }

    @Override 
    public void eliminarLogicamente(long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Factura factura = em.find(Factura.class, id);
            if(factura == null) throw new IllegalArgumentException("No existe la factura indicada");
            factura.setestado(false);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            System.err.println("✘ Error al eliminar factura: " + e.getMessage());
        }finally {
            em.close();
        }
    }
}
