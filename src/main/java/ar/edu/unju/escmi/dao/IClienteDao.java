package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Cliente;
import java.util.List;

/**
 * Interfaz DAO para la entidad Cliente.
 * Define las operaciones CRUD y de consulta.
 *
 * Patrón: DAO (Data Access Object)
 */
public interface IClienteDao {

    /**
     * Persiste un nuevo cliente en la base de datos.
     * 
     * @param cliente el cliente a guardar
     */
    void guardarCliente(Cliente cliente);

    /**
     * Busca un cliente por su ID.
     * 
     * @param id identificador del cliente
     * @return el Cliente encontrado o null
     */
    Cliente buscarClientePorId(Long id);

    /**
     * Actualiza los datos de un cliente existente.
     * 
     * @param cliente el cliente con datos modificados
     */
    void modificarCliente(Cliente cliente);

    /**
     * Eliminación lógica: pone estado = false.
     * 
     * @param id identificador del cliente a desactivar
     */
    void eliminarCliente(Long id);

    /**
     * Retorna todos los clientes con estado = true.
     * 
     * @return lista de clientes activos
     */
    List<Cliente> listarClientes();
}
