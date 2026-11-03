package ar.edu.unju.escmi.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EmfSingleton {

    private static final String PERSISTENCE_UNIT = "tp7_grupo6PU";

    private static EntityManagerFactory emf = null;

    private EmfSingleton() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
