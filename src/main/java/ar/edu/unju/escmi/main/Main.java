package ar.edu.unju.escmi.main;

import ar.edu.unju.escmi.dao.ProductoDao;
import ar.edu.unju.escmi.dao.imp.ClienteDaoImp;
import ar.edu.unju.escmi.dao.imp.FacturaDaoImp;
import ar.edu.unju.escmi.model.Producto;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.model.Factura;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.model.Cliente;
import ar.edu.unju.escmi.dao.IDetalleFacturaDao;
import ar.edu.unju.escmi.model.DetalleFactura;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private final IClienteDao clientedao;
    private final IFacturaDao facturadao;
    private final ProductoDao productodao;
    private final IDetalleFacturaDao detalledao;

    public static void main (String[] args) {
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
                        registrarAltaProducto(scanner);
                        break;
                    case 3:
                        VentaDeProducto();
                        break;
                    case 4:
                        long idFactura = pedirLong("Ingrese el ID de la factura a buscar: ");
                        Factura factura = buscarFacturaPorId(idFactura);
                        if (factura != null) {
                            System.out.println(factura);
                        } else {
                            System.out.println("No se encontró una factura con el ID: " + idFactura);
                        }
                        break;
                    case 5:
                        long idFacturaEliminar = pedirLong("Ingrese el ID de la factura a eliminar: ");
                        eliminarFacturaLogica(idFacturaEliminar);
                        System.out.println("Factura con ID " + idFacturaEliminar + " eliminada lógicamente.");
                        break;
                    case 6:
                        registrarBajaProducto(scanner);
                        break;
                    case 7:
                        modificarCliente();
                        break;
                    case 8:
                        registrarModificacionPrecio(scanner);
                        break;
                    case 9:
                        eliminarClienteLogico();
                        break;
                    case 10:
                        obtenerTodasLasFacturas();
                        break;
                    case 11:
                        mostrarTodosLosClientes();
                        break;
                    case 12:
                        obtenerFacturasMayoresA900k();
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
        	} finally {
        	}
        } while (opcion != 13);
        scanner.close();
    }

    private static void mostrarMenu() 
    {
        System.out.println("\n=======================================================");
        System.out.println("                    MENU DE OPCIONES");
        System.out.println("=======================================================");
        System.out.println("1 - Alta de cliente.");
        System.out.println("2 - Alta de producto.");
        System.out.println("3 - Realizar la venta de productos (Alta de una nueva factura).");
        System.out.println("4 - Buscar una factura ingresando su número de factura y mostrar todos sus datos");
        System.out.println("5 - Eliminar una factura.");
        System.out.println("6 - Eliminar un producto.");
        System.out.println("7 - Modificar datos de cliente.");
        System.out.println("8 - Modificar precio de producto.");
        System.out.println("9  -Eliminar cliente.");
        System.out.println("10 - Mostrar todas las facturas.");
        System.out.println("11 - Mostrar todos los clientes.");
        System.out.println("12 - Mostrar las facturas que superen el total de $900.000");
        System.out.println("13 - Salir.");
		System.out.println("=======================================================");
	}

    public static void registrarAltaCliente() 
    {
        System.out.println("Registrar Cliente");
        try {
            String nombre = pedirString("Ingrese el nombre del cliente: ");
            String apellido = pedirString("Ingrese el apellido del cliente: ");
            String domicilio = pedirString("Ingrese el domicilio del cliente: ");
            int dni = pedirInt("Ingrese el DNI del cliente: ");

            Cliente cliente = new Cliente(nombre, apellido, domicilio, dni);
            ClienteDaoImp.getInstance().guardarCliente(cliente);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Formato de número inválido. Intente nuevamente.");
        }
    }

    public static void modificarCliente()
    {
        long id = pedirLong("Ingrese el ID del cliente a modificar: ");
        Cliente cliente = ClienteDaoImp.getInstance().buscarClientePorId(id);

        if (cliente == null) {
            System.out.println("[AVISO] No se encontró un cliente con ese ID.");
            return;
        }

        String nuevoNombre = pedirString("Ingrese el nuevo nombre (actual: " + cliente.getNombre() + "): ");
        String nuevoApellido = pedirString("Ingrese el nuevo apellido (actual: " + cliente.getApellido() + "): ");
        String nuevoDomicilio = pedirString("Ingrese el nuevo domicilio (actual: " + cliente.getDomicilio() + "): ");
        int nuevoDni = pedirInt("Ingrese el nuevo DNI (actual: " + cliente.getDni() + "): ");

        cliente.setNombre(nuevoNombre);
        cliente.setApellido(nuevoApellido);
        cliente.setDomicilio(nuevoDomicilio);
        cliente.setDni(nuevoDni);

        ClienteDaoImp.getInstance().modificarCliente(cliente);
    }

    public static void eliminarClienteLogico() {
        long id = pedirLong("Ingrese el ID del cliente a eliminar: ");
        ClienteDaoImp.getInstance().eliminarCliente(id);
    }

    public static void mostrarTodosLosClientes() {
        List<Cliente> clientes = ClienteDaoImp.getInstance().listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("[AVISO] No hay clientes para mostrar.");
        } else {
            System.out.println("Lista de Clientes Activos:");
            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }
        }
    }

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

    public static Factura buscarFacturaPorId(Long id) {
        EntityManager em = getEntityManager();
        Factura factura = null;
        try {
            factura = em.find(Factura.class, id);
        } finally {
            em.close();
        }
        return factura;
    }

    public static void eliminarFacturaLogica(Long id) {
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

    public static List<Factura> obtenerTodasLasFacturas() {
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

    public static void VentaDeProducto() {
        
        long id = pedirLong("Ingrese ID del cliente");
        Cliente cliente = clientedao.buscarClientePorId(id);

        if(cliente == null) {
            System.out.print("No se encontró el cliente con id: " + id);
            return;
        }else System.out.print("Cliente seleccionado: " + cliente.getNombre());

        List<DetalleFactura> detalles = new ArrayList<>();
        boolean opcion = true;

        do {
            Long productoId = pedirLong("Ingrese id del producto:");
            Producto producto = productodao.buscarProductoPorId(productoId);
            
            if(producto == null || !producto.isEstado()) System.out.print("No se encontró el producto con el id: " + productoId);
            else {
                System.out.print("Producto seleccionado: " + producto.getDescripcion() + "Precio: " + producto.getPrecioUnitario());
                int cantidad = pedirInt("Ingrese cantidad a comprar:");
                if(cantidad <= 0) System.out.print("La cantidad debe ser mayor a 0"); 
                else {
                    DetalleFactura detalle = new DetalleFactura(producto, cantidad);
                    detalles.add(detalle);
                    System.out.print("Producto agregado correctamente");
                }
            }

            String resp = pedirString("Desea ingresar otro producto? (S/N):");
            if(resp.equalsIgnoreCase("N")) opcion = false;
        } while (opcion);

            if (detalles.isEmpty()) {
            System.out.println("No se puede registrar una venta sin productos.");
            return;
        }

        Factura factura = new Factura(cliente, detalles);
        facturadao.guardar(factura);

        System.out.println("\n=======================================================");
		System.out.println("       ¡VENTA REALIZADA CON ÉXITO (AHORA 20)!");
		System.out.println("=======================================================");
		System.out.println(factura);
    }

    private static long pedirLong(String mensaje) {
		long valor = 0;
		boolean valido = false;
		while (!valido) {
			try {
				System.out.print(mensaje);
				String entrada = scanner.nextLine().trim();
				valor = Long.parseLong(entrada);
				valido = true;
			} catch (NumberFormatException e) {
				System.out.println(">>> Error: Ingrese un número entero válido (sin letras ni caracteres especiales).");
			} finally {
				
			}
		}
		return valor;
	}

    private static int pedirInt(String mensaje) {
		int valor = 0;
		boolean valido = false;
		while (!valido) {
			try {
				System.out.print(mensaje);
				String entrada = scanner.nextLine().trim();
				valor = Integer.parseInt(entrada);
				valido = true;
			} catch (NumberFormatException e) {
				System.out.println(">>> Error: Ingrese un número entero válido.");
			} finally {
				
			}
		}
		return valor;
	}

    private static double pedirDouble(String mensaje) {
		double valor = 0.0;
		boolean valido = false;
		while (!valido) {
			try {
				System.out.print(mensaje);
				String entrada = scanner.nextLine().trim();
				valor = Double.parseDouble(entrada);
				valido = true;
			} catch (NumberFormatException e) {
				System.out.println(">>> Error: Ingrese un número decimal válido.");
			} finally {
				
			}
		}
		return valor;
	}

    private static String pedirString(String mensaje) {
		String texto = "";
		try {
			System.out.print(mensaje);
			texto = scanner.nextLine().trim();
		} catch (Exception e) {
			System.out.println(">>> Error en la lectura del texto: " + e.getMessage());
		} finally {
			
		}
		return texto;
	}
}