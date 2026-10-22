package ar.edu.unju.escmi.model;

import javax.persistence.*;

/**
 * Entidad JPA que representa un Cliente.
 * Mapea la tabla "clientes" en la base de datos MySQL.
 *
 * Patrón: Entity (JPA)
 * Integrante responsable: Integrante 1
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    // ─── Atributos / Mapeo JPA ─────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "domicilio", length = 200)
    private String domicilio;

    @Column(name = "dni", nullable = false, unique = true)
    private int dni;

    @Column(name = "estado")
    private boolean estado;

    // ─── Constructores ─────────────────────────────────────────────────────

    /** Constructor vacío requerido por JPA */
    public Cliente() {
        this.estado = true; // activo por defecto
    }

    /**
     * Constructor con todos los atributos.
     *
     * @param nombre    nombre del cliente
     * @param apellido  apellido del cliente
     * @param domicilio domicilio del cliente
     * @param dni       DNI del cliente
     */
    public Cliente(String nombre, String apellido, String domicilio, int dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.domicilio = domicilio;
        this.dni = dni;
        this.estado = true;
    }

    // ─── Getters y Setters ─────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // ─── toString ──────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", dni=" + dni +
                ", estado=" + estado +
                '}';
    }
}
