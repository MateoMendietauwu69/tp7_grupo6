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