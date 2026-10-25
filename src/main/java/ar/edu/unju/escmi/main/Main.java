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


        scanner.close();
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

    public void VentaDeProducto() {
        
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