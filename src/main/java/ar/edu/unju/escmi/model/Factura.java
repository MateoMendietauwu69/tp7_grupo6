package ar.edu.unju.escmi.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "Factura")

public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "domicilio", nullable = false)
    private String domicilio;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "estado", nullable = false)
    private boolean estado;

    public Factura() {
        this.fecha = LocalDate.now();
        this.estado = true;
        this.total = 0.0;
    }

    public Factura(Cliente cliente, List<DetalleFactura> detalles) {
        this();
        setcliente(cliente);
        setdomicilio(cliente.getDomicilio());
        this.detalles = new ArrayList<>(detalles);
        for(DetalleFactura detalle : this.detalles) {
            detalle.setfactura(this);
        }
        this.total = calculartotal();
    }

    public void setCliente(Cliente cliente) {
        if(cliente == null) throw new IllegalArgumentException("Debe seleccionar un cliente");
        this.cliente = cliente;
        this.domicilio = cliente.getDomicilio();
    }

    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
    }

    public LocalDate getfecha() {
        return fecha;
    }

    public void setfecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getcliente() {
        return cliente;
    }

    public void setcliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getdomicilio() {
        return domicilio;
    }

    public void setdomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public List<DetalleFactura> getdetalles() {
        return detalles;
    }

    public void setdetalles(List<DetalleFactura> detalles) {
        this.detalles.clear();
        if (detalles != null) {
            for (DetalleFactura detalle : detalles) {
                detalle.setfactura(this);
                this.detalles.add(detalle);
            }
        }
        this.total = calculartotal();
    }

    public double gettotal() {
        return total;
    }

    public void settotal(double total) {
        this.total = total;
    }

    public boolean getestado() {
        return estado;
    }

    public void setestado(boolean estado) {
        this.estado = estado;
    }

    public double calculartotal() {
        double total = 0.0;
        if(detalles != null){
            for(DetalleFactura detalle : detalles) {
                total += detalle.getsubtotal();
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return  "\n\n******************** Factura ********************"
                + "\nFecha: " + fecha + " ID de Factura: " + id
                + "\nCliente: " + cliente.getNombre() + " Domicilio: " + domicilio
                + "\n************ Detalles de la Factura *************"
                + "\n" + detalles.toString().replaceAll("\\[|\\]", "").replaceAll(", ", "") + "\n";
    }
}