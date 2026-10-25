package ar.edu.unju.escmi.model;

import javax.persistence.*;

@Entity
@Table(name = "detalle_factura")

public class DetalleFactura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column( name= "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    public DetalleFactura() {

    }

    public DetalleFactura(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = cantidad * producto.getPrecioUnitario();
    }

    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
    }

    public Producto getproducto() {
        return producto;
    }

    public void setproducto(Producto producto) {
        this.producto = producto;
    }

    public int getcantidad() {
        return cantidad;
    }

    public void setcantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getsubtotal() {
        return subtotal;
    }

    public void setsubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Factura getfactura() {
        return factura;
    }

    public void setfactura(Factura factura) {
        this.factura = factura;
    }

    @Override
    public String toString() {
        return "PRODUCTO: " + producto + "\nCANTIDAD: " + cantidad + " | IMPORTE: " + subtotal + "\n";
    }
}