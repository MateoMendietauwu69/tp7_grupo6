package main.java.ar.edu.unju.escmi.modelo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "precio_unitario", nullable = false)
    private double precioUnitario;

    @Column(name = "estado", nullable = false)
    private boolean estado;

    public Producto() {
        this.estado = true; 
    }

    public Producto(String descripcion, double precioUnitario) {
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.estado = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Producto [ID=" + id + ", Descripción=" + descripcion + 
               ", Precio Unitario=$" + precioUnitario + ", Estado=" + (estado ? "Activo" : "Inactivo") + "]";
    }
}