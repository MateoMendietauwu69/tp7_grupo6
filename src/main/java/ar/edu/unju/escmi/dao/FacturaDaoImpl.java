package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.entities.Factura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class FacturaDaoImpl {

    // Método de soporte para obtener el EntityManager
    private EntityManager getEntityManager() {
        return EmfSingleton.getInstance().getEntityManagerFactory().createEntityManager();
    }
}