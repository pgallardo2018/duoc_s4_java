package teatromoroventas;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;


class VentaEntradaPublic {
    public int numeroVenta;
    public String ubicacion;
    public int costoBase;
    public int descuentoAplicado;
    public int costoFinal;
    public String tipoCliente;
    public int porcentajeDescuento;

    public VentaEntradaPublic(int numeroVenta, String ubicacion, int costoBase, int descuentoAplicado, int costoFinal, String tipoCliente, int porcentajeDescuento) {
        this.numeroVenta = numeroVenta;
        this.ubicacion = ubicacion;
        this.costoBase = costoBase;
        this.descuentoAplicado = descuentoAplicado;
        this.costoFinal = costoFinal;
        this.tipoCliente = tipoCliente;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getResumenVenta() {
        // Accedemos directamente a las variables de instancia (numeroVenta, ubicacion, etc.)
        return "Venta #" + numeroVenta + " | Ubicación: " + ubicacion +
               " | Cliente: " + tipoCliente + " | Desc: $" + descuentoAplicado +
               " (" + porcentajeDescuento + "%)" + " | Final: $" + costoFinal;
    }
}

public class TeatroMoroVentas {

    final static int PRECIO_VIP = 30000;
    final static int PRECIO_PLATEA = 20000;
    final static int PRECIO_BALCON = 15000;
    final static int PORCENTAJE_DESCUENTO_ESTUDIANTE = 10;
    final static int PORCENTAJE_DESCUENTO_TERCERA_EDAD = 15;
    final static int EDAD_MAX_ESTUDIANTE = 25;
    final static int EDAD_MIN_TERCERA_EDAD = 65;

    private static int ingresosTotales = 0;
    private static int proximoNumeroVenta = 1;
    private static final String NOMBRE_TEATRO = "Teatro Moro";

    private static ArrayList<VentaEntradaPublic> listaVentas = new ArrayList<>();

    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        System.out.println("Bienvenido al Sistema de Ventas del " + NOMBRE_TEATRO + " (v.S7 - Variables Públicas)");
        boolean continuarSistema = true;

        do {
            mostrarMenu();
            int opcionSeleccionada = leerOpcionNumerica();

            switch (opcionSeleccionada) {
                case 1:
                    realizarVenta();
                    break;
                case 2:
                    visualizarResumenVentas();
                    break;
                case 3:
                    generarBoleta();
                    break;
                case 4:
                    mostrarIngresosTotales();
                    break;
                case 0:
                    continuarSistema = false;
                    System.out.println("\nSaliendo del programa...");
                    System.out.println("Gracias por su compra.");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }

            if (continuarSistema && opcionSeleccionada != 0) {
                pausaParaContinuar();
            }

        } while (continuarSistema);

        scanner.close();
        System.out.println("Sistema cerrado.");
    }


    public static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Vender Entrada");
        System.out.println("2. Visualizar Resumen de Ventas");
        System.out.println("3. Generar Boleta Individual");
        System.out.println("4. Mostrar Ingresos Totales");
        System.out.println("0. Salir");
        System.out.println("----------------------");
        System.out.print("Seleccione una opción: ");
    }

    public static void realizarVenta() {
        System.out.println("\n--- Nueva Venta de Entrada ---");

        String ubicacionSel = "";
        int costoBaseVenta = 0;
        int edadVenta = -1;
        boolean ubicacionOk = false;
        int descuentoVenta = 0;
        int porcDescVenta = 0;
        String tipoClienteVenta = "General";

        System.out.println("Ubicaciones: 1.VIP ($"+PRECIO_VIP+") 2.Platea ($"+PRECIO_PLATEA+") 3.Balcón ($"+PRECIO_BALCON+")");
        while (!ubicacionOk) {
            System.out.print("Seleccione ubicación (1-3): ");
            int opcionUbicacion = leerOpcionNumerica();
            switch (opcionUbicacion) {
                case 1: ubicacionSel = "VIP"; costoBaseVenta = PRECIO_VIP; ubicacionOk = true; break;
                case 2: ubicacionSel = "Platea"; costoBaseVenta = PRECIO_PLATEA; ubicacionOk = true; break;
                case 3: ubicacionSel = "Balcón"; costoBaseVenta = PRECIO_BALCON; ubicacionOk = true; break;
                default: System.out.println("Opción inválida."); break;
            }
        }

        System.out.print("Ingrese edad del cliente: ");
        edadVenta = leerOpcionNumerica();
        while (edadVenta < 0) {
             System.out.println("Edad no puede ser negativa.");
             System.out.print("Ingrese edad del cliente: ");
             edadVenta = leerOpcionNumerica();
        }

        if (edadVenta <= EDAD_MAX_ESTUDIANTE) {
            descuentoVenta = (costoBaseVenta * PORCENTAJE_DESCUENTO_ESTUDIANTE) / 100;
            tipoClienteVenta = "Estudiante";
            porcDescVenta = PORCENTAJE_DESCUENTO_ESTUDIANTE;
        } else if (edadVenta >= EDAD_MIN_TERCERA_EDAD) {
            descuentoVenta = (costoBaseVenta * PORCENTAJE_DESCUENTO_TERCERA_EDAD) / 100;
            tipoClienteVenta = "Tercera Edad";
            porcDescVenta = PORCENTAJE_DESCUENTO_TERCERA_EDAD;
        }

        int costoFinalVenta = costoBaseVenta - descuentoVenta;

        int numVenta = proximoNumeroVenta++;
        VentaEntradaPublic venta = new VentaEntradaPublic(numVenta, ubicacionSel, costoBaseVenta,
                                            descuentoVenta, costoFinalVenta, tipoClienteVenta, porcDescVenta);
        listaVentas.add(venta); // Agregamos el objeto a la lista

        ingresosTotales += costoFinalVenta;

        System.out.println("------------------------------------");
        System.out.println("¡VENTA #" + numVenta + " REGISTRADA!");
        System.out.println("Ubicación: " + ubicacionSel + " | Cliente: " + tipoClienteVenta + " | Final: $" + costoFinalVenta);
        System.out.println("------------------------------------");
    }

    public static void visualizarResumenVentas() {
        System.out.println("\n--- Resumen de Todas las Ventas ---");

        if (listaVentas.isEmpty()) {
            System.out.println("No se han realizado ventas aún.");
        } else {
            System.out.println("Mostrando " + listaVentas.size() + " venta(s) registrada(s):");
            System.out.println("--------------------------------------------------");
            for (VentaEntradaPublic venta : listaVentas) {
                System.out.println(venta.getResumenVenta());
            }
            System.out.println("--------------------------------------------------");
        }
    }

    public static void generarBoleta() {
        System.out.println("\n--- Generar Boleta Individual ---");

        if (listaVentas.isEmpty()) {
            System.out.println("No hay ventas para generar boletas.");
            return;
        }

        System.out.print("Ingrese el Número de Venta para la boleta: ");
        int numVentaBuscar = leerOpcionNumerica();

        if (numVentaBuscar <= 0) { System.out.println("Número de venta inválido."); return; }

        VentaEntradaPublic ventaParaBoleta = null;

        for (VentaEntradaPublic venta : listaVentas) {
            if (venta.numeroVenta == numVentaBuscar) {
                ventaParaBoleta = venta;
                break;
            }
        }

        if (ventaParaBoleta != null) {
            System.out.println("\n***********************************");
            System.out.println("         " + NOMBRE_TEATRO + "         ");
            System.out.println("-----------------------------------");
            System.out.println(" Venta #: " + ventaParaBoleta.numeroVenta); // Acceso directo
            System.out.println(" Ubicación: " + ventaParaBoleta.ubicacion); // Acceso directo
            System.out.println(" Costo Base: $" + ventaParaBoleta.costoBase); // Acceso directo
            System.out.println(" Descuento Aplicado: " + ventaParaBoleta.porcentajeDescuento + "%" + // Acceso directo
                               " ($" + ventaParaBoleta.descuentoAplicado + ")"); // Acceso directo
            System.out.println(" Costo Final: $" + ventaParaBoleta.costoFinal); // Acceso directo
            System.out.println("-----------------------------------");
            System.out.println(" Gracias por su visita al " + NOMBRE_TEATRO + " ");
            System.out.println("***********************************");
        } else {
            System.out.println("No se encontró una venta con el número " + numVentaBuscar + ".");
        }
    }

    public static void mostrarIngresosTotales() {
        System.out.println("\n--- Ingresos Totales Acumulados ---");
        System.out.println("Total Recaudado: $" + ingresosTotales);
        System.out.println("-----------------------------------");
    }

    public static int leerOpcionNumerica() {
        int numero = -1;
        try {
            numero = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println(" >> Error: Ingrese un valor numérico.");
        } finally {
            scanner.nextLine(); // Limpiar buffer
        }
        return numero;
    }

     public static void pausaParaContinuar() {
         System.out.print("\n(Presione Enter para continuar...)");
         scanner.nextLine();
     }

}