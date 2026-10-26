package ar.edu.unju.escmi.dao.imp;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.model.Cliente;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;


public class ClienteDaoImp implements IClienteDao {

    private EntityManagerFactory emf = EmfSingleton.getEntityManagerFactory();
    private static ClienteDaoImp instance;

    private ClienteDaoImp() {
    }

    public static ClienteDaoImp getInstance() {
        if (instance == null) {
            instance = new ClienteDaoImp();
        }
        return instance;
    }

    @Override
    public void guardarCliente(Cliente cliente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            System.out
                    .println("✔ Cliente guardado correctamente: " + cliente.getNombre() + " " + cliente.getApellido());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("✘ Error al guardar cliente: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente buscarClientePorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Cliente c = em.find(Cliente.class, id);
            return c;
        } catch (Exception e) {
            System.err.println("✘ Error al buscar cliente: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void modificarCliente(Cliente cliente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
            System.out.println("✔ Cliente modificado correctamente.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("✘ Error al modificar cliente: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    @Override
    public void eliminarCliente(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Cliente c = em.find(Cliente.class, id);
            if (c != null) {
                c.setEstado(false);
                em.merge(c);
                System.out.println("✔ Cliente desactivado (eliminación lógica).");
            } else {
                System.out.println("⚠ No se encontró cliente con id: " + id);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("✘ Error al eliminar cliente: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> listarClientes() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Cliente> query = em.createQuery(
                    "SELECT c FROM Cliente c WHERE c.estado = true ORDER BY c.apellido",
                    Cliente.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("✘ Error al listar clientes: " + e.getMessage());
            return List.of();
        } finally {
            em.close();
        }
    }
}
