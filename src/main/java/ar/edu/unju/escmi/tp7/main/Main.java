package ar.edu.unju.escmi.tp7.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import ar.edu.unju.escmi.tp7.dao.imp.*;
import ar.edu.unju.escmi.tp7.entities.*;

public class Main {
	
    private static EntityManager manager = Persistence.createEntityManagerFactory("myJpaUnit").createEntityManager();

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        boolean continuar = true;
        
        do {
            System.out.println("         SISTEMA DE VENTAS         ");
            System.out.println("___________________________________");
            System.out.println("1. Alta de cliente");
            System.out.println("2. Alta de producto");
            System.out.println("3. Vender productos");
            System.out.println("4. Buscar factura por ID");
            System.out.println("5. Eliminar una factura");
            System.out.println("6. Eliminar un producto");
            System.out.println("7. Modificar datos del cliente");
            System.out.println("8. Modificar precio del producto");
            System.out.println("9. Mostrar todas las facturas");
            System.out.println("10. Mostrar todos los clientes");
            System.out.println("11. Facturas mayores a $500,000");
            System.out.println("12. Salir");
            System.out.println("___________________________________");
            System.out.print("Elige una opción: ");
            
            while (!sc.hasNextInt()) {
                System.out.println("Opción no válida. Intenta de nuevo.");
                sc.next(); 
                System.out.print("Elige una opción: ");
            }
            
            String op = sc.nextLine();
            
            switch(op) {
                case "1":
                    AltaCliente(sc);
                    break;
                
                case "2":
                    AltaProducto(sc);
                    break;
                
                case "3":
                    VenderProducto(sc);
                    break;
                
                case "4":
                    System.out.print("\nIngrese el ID de la factura: ");
                    long id = sc.nextLong();
                    BuscarFactura(id);
                    break;
                
                case "5":
                    System.out.print("\nIngrese el ID de la factura para eliminar: ");
                    long idE = sc.nextLong();
                    EliminarFactura(idE);
                    break;
                
                case "6":
                    System.out.print("\nIngrese el ID del producto para eliminar: ");
                    long idP = sc.nextLong();
                    EliminarProducto(idP);
                    break;
                
                case "7":
                    ModificarCliente(sc);
                    break;
                
                case "8":
                    ModificarProducto(sc);
                    break;
                
                case "9":
                    MostrarFacturas();
                    break;
                
                case "10":
                    MostrarClientes();
                    break;
                
                case "11":
                    MostrarFacturasFiltradas();
                    break;
                
                case "12":
                    System.out.println("**********************************");
                    System.out.println("Saliendo del Sistema...");
                    continuar = false;
                    break;
                
                default:
                    System.out.println("\nOpción no válida. Intenta de nuevo.");
            }
            
        } while(continuar);
        
        sc.close();
    }
    
    
    public static void AltaCliente(Scanner sc) {
        ImpClienteDAO clienteDaoImp = new ImpClienteDAO(); 
        
        System.out.print("Ingrese ID del Cliente: ");
        long id = sc.nextLong();
        sc.nextLine();
        System.out.print("Ingrese nombre del cliente: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese apellido del cliente: ");
        String apellido = sc.nextLine();
        System.out.print("Ingrese domicilio del cliente: ");
        String domicilio = sc.nextLine();
        System.out.print("Ingrese DNI del cliente: ");
        int dni = sc.nextInt();
        
        Cliente cliente = new Cliente(id, nombre, apellido, domicilio, dni, true);  
        clienteDaoImp.guardarCliente(cliente);
        System.out.println("\n¡Cliente registrado con éxito!");
    }

    public static void AltaProducto(Scanner sc) {
        ImpProductoDAO productoDaoImp = new ImpProductoDAO();
        
        System.out.print("Ingrese el ID del producto: ");
        long id = sc.nextLong();  
        sc.nextLine(); 
        System.out.print("Ingrese descripción del producto: ");
        String descripcion = sc.nextLine();
        System.out.print("Ingrese precio unitario: ");
        double precioUnitario = sc.nextDouble();

        Producto producto = new Producto(id, descripcion, precioUnitario); 
        productoDaoImp.guardarProducto(producto);
        System.out.println("\n¡Producto registrado con éxito!");
    }
    
    public static void VenderProducto(Scanner sc) {
        ImpFacturaDAO facturaDaoImp = new ImpFacturaDAO();
        Factura factura = new Factura();
        factura.setFecha(LocalDate.now());

        System.out.print("Ingrese el ID del cliente: ");
        Long clienteId = sc.nextLong();

        ImpClienteDAO clienteDaoImp = new ImpClienteDAO();
        Cliente cliente = clienteDaoImp.buscarClientePorId(clienteId);

        if (cliente == null) {
            System.out.println("\n¡Cliente no encontrado!");
            return;
        }

        factura.setCliente(cliente);

        List<DetalleFactura> detalles = new ArrayList<>();
        String agregar = "";  
        boolean productosValidos = false; 

        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin(); 

            do {
                DetalleFactura detalle = new DetalleFactura();

                System.out.print("Ingrese el ID del producto: ");
                Long productoId = sc.nextLong();
                ImpProductoDAO ProductoDaoImp = new ImpProductoDAO();
                Producto producto = ProductoDaoImp.buscarProductoPorId(productoId);

                if (producto == null) {
                    System.out.println("¡Producto no encontrado! 😕");
                    continue;
                }
                detalle.setProducto(producto);

                System.out.print("Ingrese la cantidad: ");
                int cantidad = sc.nextInt();
                detalle.setCantidad(cantidad);

                detalle.calcularSubtotal();
                detalle.setFactura(factura);
                detalles.add(detalle);

                productosValidos = true; 

                System.out.print("¿Desea agregar otro producto? (s/n): ");
                agregar = sc.next(); 

            } while (agregar.equalsIgnoreCase("s")); 

            if (!productosValidos) {
                System.out.println("No se ha registrado ningún producto válido. Venta cancelada.");
                return; 
            }

            factura.setDetalles(detalles);
            factura.calcularTotal();

            facturaDaoImp.guardarFactura(factura);  

            transaction.commit();
            System.out.println("\n¡Venta registrada con éxito! Factura ID: " + factura.getId());

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();  // Rollback en caso de error
            }
            e.printStackTrace();
            System.out.println("Se produjo un error al procesar la venta. Intente nuevamente.");
        } finally {
            if (manager.isOpen()) {
                manager.close();  // Cerrar el EntityManager
            }
        }
    }

    
    public static void BuscarFactura(long numId) {
        ImpFacturaDAO factura = new ImpFacturaDAO();
        Factura fact = factura.buscarFacturaPorId(numId);
        fact.mostrarFactura();
    }
    
    public static void EliminarFactura(long numId) {
    	ImpFacturaDAO factura = new ImpFacturaDAO();
        Factura fact = factura.buscarFacturaPorId(numId);
        factura.eliminarFacturaLogicamente(fact);
        fact.setEstado(false);
        System.out.println("\n¡Factura eliminada con éxito! ");
    }
    
    public static void EliminarProducto(long idProd) {
        ImpProductoDAO producto = new ImpProductoDAO();
        Producto prod = producto.buscarProductoPorId(idProd);
        producto.eliminarProductoLogicamente(prod);
        prod.setEstado(false);
        System.out.println("\n¡Producto eliminado con éxito!");
    }
    
    public static void ModificarCliente(Scanner sc) {
    	ImpClienteDAO clienteDaoImp = new ImpClienteDAO();
        Cliente cliente = null;
        long idCliente;

        do {
            System.out.print("Ingrese el ID del cliente a modificar: ");
            idCliente = sc.nextLong();
            cliente = clienteDaoImp.buscarClientePorId(idCliente);

            if (cliente == null) {
                System.out.println("Cliente no encontrado. Intente nuevamente");
            }
        } while (cliente == null);

        sc.nextLine();

        System.out.print("Ingrese el nuevo nombre: ");
        cliente.setNombre(sc.nextLine());

        System.out.print("Ingrese el nuevo apellido: ");
        cliente.setApellido(sc.nextLine());

        System.out.print("Ingrese el nuevo domicilio: ");
        cliente.setDomicilio(sc.nextLine());

        System.out.print("Ingrese el nuevo DNI: ");
        cliente.setDni(sc.nextInt());

        clienteDaoImp.modificarCliente(cliente);
        System.out.println("\n¡Datos del cliente actualizados con éxito!");
    }

    public static void ModificarProducto(Scanner sc) {
    	ImpProductoDAO productoDaoImp = new ImpProductoDAO();
        Producto producto = null;
        long idProducto;

        do {
            System.out.print("Ingrese el ID del producto a modificar: ");
            idProducto = sc.nextLong();
            producto = productoDaoImp.buscarProductoPorId(idProducto);

            if (producto == null) {
                System.out.println("Producto no encontrado. Intente nuevamente");
            }
        } while (producto == null);

        System.out.print("Ingrese el nuevo precio del producto: ");
        double nuevoPrecio = sc.nextDouble();
        producto.setPrecioUnitario(nuevoPrecio);

        productoDaoImp.modificarPrecioProducto(producto);
        System.out.println("\n¡Precio del producto modificado con éxito!");
    }

    public static void MostrarFacturas() {
        try {
        	ImpFacturaDAO facturaDaoImp = new ImpFacturaDAO(); 
            System.out.println("\n** Listado de Facturas **");
            facturaDaoImp.mostrarTodasLasFacturas();
        } catch (Exception e) {
            System.out.println("Error al mostrar las Facturas");
            System.out.println(e.getMessage());
        }
    }

    public static void MostrarClientes() {
        try {
            ImpClienteDAO clienteDaoImp = new ImpClienteDAO(); 
            
            System.out.println("\n** Listado de Clientes **");
            
            clienteDaoImp.mostrarTodosLosClientes(); 
        } catch (Exception e) {
            System.out.println("Error al mostrar los Clientes");
            System.out.println(e.getMessage());
        }
    }


    public static void MostrarFacturasFiltradas() {
		 try {
			 	ImpFacturaDAO facturaDaoImp= new ImpFacturaDAO(); 
		        System.out.println("Lista de Facturas filtradas:");
				facturaDaoImp.mostrarFacturasFiltradas();
	        } catch (Exception e) {
	            System.out.println("Error al mostrar las facturas");
	            System.out.println(e.getMessage());
	        }
	}
    
}
