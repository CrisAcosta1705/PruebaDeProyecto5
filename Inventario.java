package Proyecto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Inventario {
    private List<Producto> productos;
    private List<Venta> historialVentas; 

    public Inventario() {
        productos = new ArrayList<>();
        historialVentas = new ArrayList<>(); 
    }
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void agregarProductoElectronico(ProductoElectronico productoElectronico) {
        productos.add(productoElectronico);
    }

    public void agregarProductoAlimenticio(ProductoAlimenticio productoAlimenticio) {
        productos.add(productoAlimenticio);
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("==== MENÚ DEL INVENTARIO ====");
            System.out.println("1. Registrar producto");
            System.out.println("2. Mostrar inventario");
            System.out.println("3. Informe del inventario");
            System.out.println("4. Realizar venta");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        registrarProducto();
                        break;
                    case 2:
                        mostrarInventario();
                        break;
                    case 3:
                        generarInformeInventario();
                        break;
                    case 4:
                        realizarVenta();
                        break;
                    case 5:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, seleccione nuevamente.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Debe ingresar un número válido. Inténtelo nuevamente.");
                scanner.nextLine();
                opcion = 0;
            }
        } while (opcion != 5);
    }

    public void registrarProducto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del producto: ");
        String marca = scanner.nextLine();
        System.out.print("Ingrese el precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Ingrese las unidades del producto: ");
        int unidades = scanner.nextInt();
        scanner.nextLine();

        System.out.println("1. Producto electrónico");
        System.out.println("2. Producto alimenticio");
        System.out.print("Seleccione el tipo de producto: ");
        int tipoProducto = scanner.nextInt();
        scanner.nextLine();

        Producto producto;

        switch (tipoProducto) {
            case 1:
                System.out.print("Ingrese el modelo del producto electrónico: ");
                String modelo = scanner.nextLine();
                producto = new ProductoElectronico(marca, precio, unidades, modelo);
                break;
            case 2:
                System.out.print("Ingrese la fecha de caducidad del producto alimenticio: ");
                String fechaCaducidad = scanner.nextLine();
                producto = new ProductoAlimenticio(marca, precio, unidades, fechaCaducidad);
                break;
            default:
                System.out.println("Opción inválida. No se pudo registrar el producto.");
                return;
        }

        agregarProducto(producto);
        System.out.println("Producto registrado exitosamente.");
    }

    public void mostrarInventario() {
        if (productos.isEmpty()) {
            System.out.println("El inventario está vacío.");
        } else {
            System.out.println("==== INVENTARIO ====");
            System.out.println("Nombre\t\tPrecio\t\tUnidades\t\tTipo\t\tModelo/Fecha Caducidad");
            System.out.println("------------------------------------------------------------------------------------");
            for (Producto producto : productos) {
                String tipoProducto = producto instanceof ProductoElectronico ? "Electrónico" : "Alimenticio";
                String modeloFechaCaducidad = producto instanceof ProductoElectronico ?
                        ((ProductoElectronico) producto).getModelo() :
                        ((ProductoAlimenticio) producto).getFechaCaducidad();

                System.out.printf("%-17s%-17s%-18s%-24s%-24s\n",
                        producto.getNombre(), producto.getPrecio(), producto.getUnidades(), tipoProducto, modeloFechaCaducidad);
            }
            System.out.println("------------------------------------------------------------------------------------");
        }
    }


    public void generarInformeInventario() {
        if (productos.isEmpty()) {
            System.out.println("El inventario está vacío.");
        } else {
            System.out.println("==== INFORME DE INVENTARIO ====");
            int totalProductos = 0;
            double valorTotalInventario = 0.0;

            for (Producto producto : productos) {
                int unidades = producto.getUnidades();
                double precio = producto.getPrecio();
                double valorInventario = unidades * precio;

                System.out.println("Nombre del producto: " + producto.getNombre());
                System.out.println("Unidades: " + unidades);
                System.out.println("Precio: $" + precio);
                System.out.println("Valor del inventario: $" + valorInventario);
                System.out.println("----------------------------------------");

                totalProductos += unidades;
                valorTotalInventario += valorInventario;
            }

            System.out.println("Total de productos en el inventario: " + totalProductos);
            System.out.println("Valor total del inventario: $" + valorTotalInventario);
            System.out.println("----------------------------------------");

            if (!historialVentas.isEmpty()) { // Verificar si hay ventas en el historial
                System.out.println("==== HISTORIAL DE VENTAS ====");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (Venta venta : historialVentas) {
                    System.out.println("Fecha de venta: " + venta.getFechaVenta().format(formatter));
                    System.out.println("Producto vendido: " + venta.getNombre());
                    System.out.println("Cantidad vendida: " + venta.getCantidad());
                    System.out.println("----------------------------------------");
                }
            } else {
                System.out.println("No hay ventas registradas en el historial.");
            }
        }
    }

    public void realizarVenta() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==== REALIZAR VENTA ====");

        if (productos.isEmpty()) {
            System.out.println("No hay productos en el inventario para realizar la venta.");
            return;
        }

        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine(); 

        Producto productoEncontrado = null;
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                productoEncontrado = producto;
                break;
            }
        }

        if (productoEncontrado == null) {
            System.out.println("Producto no encontrado en el inventario.");
            return;
        }

        System.out.print("Ingrese la cantidad a vender: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        if (cantidad > 0 && cantidad <= productoEncontrado.getUnidades()) {
            productoEncontrado.setUnidades(productoEncontrado.getUnidades() - cantidad);
            System.out.println("Venta realizada exitosamente.");

            LocalDate fechaVenta = LocalDate.now();
            
            double gananciasVenta = 0;
            Venta venta = new Venta(nombre, fechaVenta, cantidad, gananciasVenta);
            historialVentas.add(venta); 
        } else {
            System.out.println("Cantidad inválida. La venta no se puede realizar.");
        }
    }
}