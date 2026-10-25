package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Factura;
import java.util.List;

public interface IFacturaDao {
    
    void guardar(Factura factura);

    Factura buscarporid(long id);

    List<Factura> listarTodas();

    void eliminarLogicamente(long id);
}