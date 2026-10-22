import main.java.ar.edu.unju.escmi.dao.ProductoDao;
import main.java.ar.edu.unju.escmi.modelo.Producto;
import java.util.Scanner;

public static void registrarAltaProducto(Scanner scanner) {
    try {
        System.out.print("Ingrese la descripción del producto: ");
        String descripcion = scanner.nextLine();
        System.out.print("Ingrese el precio unitario: ");
        double precio = Double.parseDouble(scanner.nextLine());

        Producto producto = new Producto(descripcion, precio);
        ProductoDao.getInstance().guardarProducto(producto);
    } catch (NumberFormatException e) {
        System.out.println("[ERROR] Formato de número inválido. Intente nuevamente.");
    }
}

public static void registrarBajaProducto(Scanner scanner) {
    try {
        System.out.print("Ingrese el ID del producto a eliminar: ");
        Long id = Long.parseLong(scanner.nextLine());
        boolean exito = ProductoDao.getInstance().eliminarProductoLogico(id);
        if (exito) {
            System.out.println("[ÉXITO] Producto dado de baja lógicamente.");
        } else {
            System.out.println("[AVISO] No se encontró un producto activo con ese ID.");
        }
    } catch (NumberFormatException e) {
        System.out.println("[ERROR] ID ingresado inválido.");
    }
}

public static void registrarModificacionPrecio(Scanner scanner) {
    try {
        System.out.print("Ingrese el ID del producto a modificar: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Ingrese el nuevo precio unitario: ");
        double nuevoPrecio = Double.parseDouble(scanner.nextLine());

        boolean exito = ProductoDao.getInstance().modificarPrecioProducto(id, nuevoPrecio);
        if (exito) {
            System.out.println("[ÉXITO] Precio modificado correctamente.");
        } else {
            System.out.println("[AVISO] No se pudo actualizar. Verifique el ID o si el producto está activo.");
        }
    } catch (NumberFormatException e) {
        System.out.println("[ERROR] Datos numéricos inválidos.");
    }
}


public Factura buscarFacturaPorId(Long id) {
    EntityManager em = getEntityManager();
    Factura factura = null;
    try {
        factura = em.find(Factura.class, id);
    } finally {
        em.close();
    }
    return factura;
}

public void eliminarFacturaLogica(Long id) {
    EntityManager em = getEntityManager();
    try {
        em.getTransaction().begin();
        Factura factura = em.find(Factura.class, id);
        if (factura != null) {
            factura.setEstado(false);
            em.merge(factura);
        }
        em.getTransaction().commit();
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw e;
    } finally {
        em.close();
    }
}

public List<Factura> obtenerTodasLasFacturas() {
    EntityManager em = getEntityManager();
    List<Factura> facturas = null;
    try {
        TypedQuery<Factura> query = em.createQuery("SELECT f FROM Factura f", Factura.class);
        facturas = query.getResultList();
    } finally {
        em.close();
    }
    return facturas;
}

public List<Factura> obtenerFacturasMayoresA900k() {
    EntityManager em = getEntityManager();
    List<Factura> facturas = null;
    try {
        TypedQuery<Factura> query = em.createQuery(
            "SELECT f FROM Factura f WHERE f.total > 900000", Factura.class
        );
        facturas = query.getResultList();
    } finally {
        em.close();
    }
    return facturas;
}
