package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.DetalleFactura;
import java.util.List;

public interface IDetalleFacturaDao {
    
    void guardar(DetalleFactura detalle);

    DetalleFactura buscarporid(long id);

    List<DetalleFactura> listarporfactura(long facturaId);
}
