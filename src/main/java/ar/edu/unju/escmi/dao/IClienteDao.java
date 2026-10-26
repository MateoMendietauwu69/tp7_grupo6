package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Cliente;
import java.util.List;

public interface IClienteDao {


    void guardarCliente(Cliente cliente);

    Cliente buscarClientePorId(Long id);

    void modificarCliente(Cliente cliente);

    void eliminarCliente(Long id);

    List<Cliente> listarClientes();
}
