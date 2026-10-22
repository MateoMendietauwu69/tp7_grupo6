package ar.edu.unju.escmi.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase Singleton para gestionar el EntityManagerFactory.
 * Garantiza una única instancia durante toda la ejecución de la aplicación.
 *
 * Patrón: Singleton (instancia única compartida)
 */
public class EmfSingleton {

    /** Nombre de la unidad de persistencia definida en persistence.xml */
    private static final String PERSISTENCE_UNIT = "tp7_grupo6PU";

    /** Instancia única del EntityManagerFactory */
    private static EntityManagerFactory emf = null;

    /** Constructor privado: impide instanciación externa */
    private EmfSingleton() {
    }

    /**
     * Retorna la instancia única del EntityManagerFactory.
     * Si no fue creada aún, la inicializa.
     *
     * @return EntityManagerFactory singleton
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return emf;
    }

    /**
     * Cierra el EntityManagerFactory liberando recursos.
     * Llamar al finalizar la aplicación.
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
