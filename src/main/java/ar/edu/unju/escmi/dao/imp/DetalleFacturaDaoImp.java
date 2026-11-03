package ar.edu.unju.escmi.dao.imp;

import ar.edu.unju.escmi.dao.IDetalleFacturaDao;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.model.DetalleFactura;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class DetalleFacturaDaoImp implements IDetalleFacturaDao {
    
    private EntityManagerFactory emf = EmfSingleton.getEntityManagerFactory();

    @Override 
    public void guardar(DetalleFactura detalle) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(detalle);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            System.err.println("✘ Error al guardar los detalles: " + e.getMessage());
        }finally {
            em.close();
        }
    }

    @Override 
    public DetalleFactura buscarporid(long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(DetalleFactura.class, id);
        }finally {
            em.close();
        }
    }

    @Override 
    public List<DetalleFactura> listarporfactura(long facturaId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                "SELECT d FROM DetalleFactura d " +
                "JOIN FETCH d.producto " +
                "WHERE d.factura.id = :id",
                DetalleFactura.class
            ).setParameter("id", facturaId).getResultList();
        }finally {
            em.close();
        }
    }
}
