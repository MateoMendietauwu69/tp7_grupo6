package ar.edu.unju.escmi.main;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.dao.imp.ClienteDaoImp;
import ar.edu.unju.escmi.dao.imp.FacturaDaoImp;
import ar.edu.unju.escmi.dao.imp.ProductoDaoImp;
import ar.edu.unju.escmi.model.Cliente;
import ar.edu.unju.escmi.model.DetalleFactura;
import ar.edu.unju.escmi.model.Factura;
import ar.edu.unju.escmi.model.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final IClienteDao clienteDao = ClienteDaoImp.getInstance();
    private static final IProductoDao productoDao = ProductoDaoImp.getInstance();
    private static final IFacturaDao facturaDao = new FacturaDaoImp();

    public static void main(String[] args) {
        int opcion = 0;
        do {
            mostrarMenu();
            try {
                System.out.print("Ingrese su opción: ");
                opcion = Integer.parseInt(scanner.nextLine().trim());

                switch (opcion) {
                    case 1:
                        registrarAltaCliente();
                        break;
                    case 2:
                        registrarAltaProducto();
                        break;
                    case 3:
                        ventaDeProducto();
                        break;
                    case 4:
                        buscarYMostrarFactura();
                        break;
                    case 5:
                        eliminarFacturaLogica();
                        break;
                    case 6:
                        registrarBajaProducto();
                        break;
                    case 7:
                        modificarCliente();
                        break;
                    case 8:
                        registrarModificacionPrecio();
                        break;
                    case 9:
                        eliminarClienteLogico();
                        break;
                    case 10:
                        mostrarTodasLasFacturas();
                        break;
                    case 11:
                        mostrarTodosLosClientes();
                        break;
                    case 12:
                        mostrarFacturasMayoresA900k();
                        break;
                    case 13:
                        System.out.println("\n Saliendo...");
                        break;
                    default:
                        System.out.println("\n>>> Opción no válida. Por favor, ingrese un número del 1 al 13.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n>>> Error de ingreso: Debe ingresar un número entero válido.");
            } catch (Exception e) {
                System.out.println("\n>>> Ocurrió un error inesperado: " + e.getMessage());
            }
        } while (opcion != 13);

        scanner.close();
        EmfSingleton.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=======================================================");
        System.out.println("                    MENU DE OPCIONES");
        System.out.println("=======================================================");
        System.out.println("1 - Alta de cliente.");
        System.out.println("2 - Alta de producto.");
        System.out.println("3 - Realizar la venta de productos (Alta de una nueva factura).");
        System.out.println("4 - Buscar una factura ingresando su número de factura y mostrar todos sus datos.");
        System.out.println("5 - Eliminar una factura.");
        System.out.println("6 - Eliminar un producto.");
        System.out.println("7 - Modificar datos de cliente.");
        System.out.println("8 - Modificar precio de producto.");
        System.out.println("9 - Eliminar cliente.");
        System.out.println("10 - Mostrar todas las facturas.");
        System.out.println("11 - Mostrar todos los clientes.");
        System.out.println("12 - Mostrar las facturas que superen el total de $900.000");
        System.out.println("13 - Salir.");
        System.out.println("=======================================================");
    }

    private static void registrarAltaCliente() {
        System.out.println("\n--- Alta de Cliente ---");
        String nombre = pedirString("Ingrese el nombre del cliente: ");
        String apellido = pedirString("Ingrese el apellido del cliente: ");
        String domicilio = pedirString("Ingrese el domicilio del cliente: ");
        int dni = pedirInt("Ingrese el DNI del cliente: ");

        Cliente cliente = new Cliente(nombre, apellido, domicilio, dni);
        clienteDao.guardarCliente(cliente);
    }

    private static void registrarAltaProducto() {
        System.out.println("\n--- Alta de Producto ---");
        String descripcion = pedirString("Ingrese la descripción del producto: ");
        double precio = pedirDouble("Ingrese el precio unitario: ");

        Producto producto = new Producto(descripcion, precio);
        productoDao.guardarProducto(producto);
    }

    private static void ventaDeProducto() {
        System.out.println("\n--- Nueva Venta (Factura) ---");
        long id = pedirLong("Ingrese ID del cliente: ");
        Cliente cliente = clienteDao.buscarClientePorId(id);

        if (cliente == null || !cliente.isEstado()) {
            System.out.println("No se encontró un cliente activo con id: " + id);
            return;
        }
        System.out.println("Cliente seleccionado: " + cliente.getNombre() + " " + cliente.getApellido());

        List<DetalleFactura> detalles = new ArrayList<>();
        boolean seguir = true;

        do {
            long productoId = pedirLong("Ingrese id del producto: ");
            Producto producto = productoDao.buscarProductoPorId(productoId);

            if (producto == null || !producto.isEstado()) {
                System.out.println("No se encontró un producto activo con el id: " + productoId);
            } else {
                System.out.println("Producto seleccionado: " + producto.getDescripcion()
                        + " | Precio: " + producto.getPrecioUnitario());
                int cantidad = pedirInt("Ingrese cantidad a comprar: ");
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor a 0.");
                } else {
                    detalles.add(new DetalleFactura(producto, cantidad));
                    System.out.println("Producto agregado correctamente.");
                }
            }

            String resp = pedirString("¿Desea ingresar otro producto? (S/N): ");
            if (resp.equalsIgnoreCase("N")) seguir = false;
        } while (seguir);

        if (detalles.isEmpty()) {
            System.out.println("No se puede registrar una venta sin productos.");
            return;
        }

        Factura factura = new Factura(cliente, detalles);
        facturaDao.guardar(factura);

        System.out.println("\n=======================================================");
        System.out.println("            ¡VENTA REGISTRADA CON ÉXITO!");
        System.out.println("=======================================================");
        System.out.println(factura);
    }

    private static void buscarYMostrarFactura() {
        long id = pedirLong("Ingrese el ID de la factura a buscar: ");
        Factura factura = facturaDao.buscarporid(id);
        if (factura != null) {
            System.out.println(factura);
        } else {
            System.out.println("No se encontró una factura con el ID: " + id);
        }
    }

    private static void eliminarFacturaLogica() {
        long id = pedirLong("Ingrese el ID de la factura a eliminar: ");
        facturaDao.eliminarLogicamente(id);
        System.out.println("Operación de eliminación lógica finalizada para la factura ID " + id + ".");
    }

    private static void registrarBajaProducto() {
        long id = pedirLong("Ingrese el ID del producto a eliminar: ");
        if (productoDao.eliminarProductoLogico(id)) {
            System.out.println("[ÉXITO] Producto dado de baja lógicamente.");
        } else {
            System.out.println("[AVISO] No se encontró un producto activo con ese ID.");
        }
    }

    private static void modificarCliente() {
        long id = pedirLong("Ingrese el ID del cliente a modificar: ");
        Cliente cliente = clienteDao.buscarClientePorId(id);

        if (cliente == null) {
            System.out.println("[AVISO] No se encontró un cliente con ese ID.");
            return;
        }

        cliente.setNombre(pedirString("Nuevo nombre (actual: " + cliente.getNombre() + "): "));
        cliente.setApellido(pedirString("Nuevo apellido (actual: " + cliente.getApellido() + "): "));
        cliente.setDomicilio(pedirString("Nuevo domicilio (actual: " + cliente.getDomicilio() + "): "));
        cliente.setDni(pedirInt("Nuevo DNI (actual: " + cliente.getDni() + "): "));

        clienteDao.modificarCliente(cliente);
    }

    private static void registrarModificacionPrecio() {
        long id = pedirLong("Ingrese el ID del producto a modificar: ");
        double nuevoPrecio = pedirDouble("Ingrese el nuevo precio unitario: ");

        if (productoDao.modificarPrecioProducto(id, nuevoPrecio)) {
            System.out.println("[ÉXITO] Precio modificado correctamente.");
        } else {
            System.out.println("[AVISO] No se pudo actualizar. Verifique el ID o si el producto está activo.");
        }
    }

    private static void eliminarClienteLogico() {
        long id = pedirLong("Ingrese el ID del cliente a eliminar: ");
        clienteDao.eliminarCliente(id);
    }

    private static void mostrarTodasLasFacturas() {
        List<Factura> facturas = facturaDao.listarTodas();
        if (facturas.isEmpty()) {
            System.out.println("[AVISO] No hay facturas para mostrar.");
        } else {
            System.out.println("\n--- Todas las Facturas ---");
            facturas.forEach(System.out::println);
        }
    }

    private static void mostrarTodosLosClientes() {
        List<Cliente> clientes = clienteDao.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("[AVISO] No hay clientes para mostrar.");
        } else {
            System.out.println("\n--- Clientes Activos ---");
            clientes.forEach(System.out::println);
        }
    }

    private static void mostrarFacturasMayoresA900k() {
        List<Factura> facturas = facturaDao.listarFacturasMayoresA(900000);
        if (facturas.isEmpty()) {
            System.out.println("[AVISO] No hay facturas que superen los $900.000.");
        } else {
            System.out.println("\n--- Facturas con total mayor a $900.000 ---");
            facturas.forEach(System.out::println);
        }
    }

    private static long pedirLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">>> Error: Ingrese un número entero válido.");
            }
        }
    }

    private static int pedirInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">>> Error: Ingrese un número entero válido.");
            }
        }
    }

    private static double pedirDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">>> Error: Ingrese un número decimal válido.");
            }
        }
    }

    private static String pedirString(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
}
