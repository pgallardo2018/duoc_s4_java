package teatromoroventas;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;


class Cliente {
    public int idCliente;
    public int edad;
    public String tipoCliente;

    public Cliente(int idCliente, int edad, String tipoCliente) {
        this.idCliente = idCliente;
        this.edad = edad;
        this.tipoCliente = tipoCliente;
    }
}


class Asiento {
    public int numeroAsiento;
    public String estado;
    public int idReferencia;

    public Asiento(int numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
        this.estado = "Disponible";
        this.idReferencia = 0;
    }
}


class Venta {
    public int idVenta;
    public int idAsiento;
    public int idCliente;
    public int costoBase;
    public int descuentoAplicado;
    public int costoFinal;
    public int porcentajeDescuento;
    public String ubicacion;

    public Venta(int idVenta, int idAsiento, String ubicacion, int idCliente, int costoBase, int descuentoAplicado, int costoFinal, int porcentajeDescuento) {
        this.idVenta = idVenta;
        this.idAsiento = idAsiento;
        this.ubicacion = ubicacion;
        this.idCliente = idCliente;
        this.costoBase = costoBase;
        this.descuentoAplicado = descuentoAplicado;
        this.costoFinal = costoFinal;
        this.porcentajeDescuento = porcentajeDescuento;
    }
}

class Reserva {
    public int idReserva;
    public int idAsiento;
    public int idCliente;

    public Reserva(int idReserva, int idAsiento, int idCliente) {
        this.idReserva = idReserva;
        this.idAsiento = idAsiento;
        this.idCliente = idCliente;
    }
}


class Descuento {
    public String tipo;
    public int porcentaje;

    public Descuento(String tipo, int porcentaje) {
        this.tipo = tipo;
        this.porcentaje = porcentaje;
    }
}



public class TeatroMoroVentas {


    final static int PRECIO_VIP = 30000;
    final static int PRECIO_PLATEA = 20000;
    final static int PRECIO_BALCON = 15000;
    final static int EDAD_MAX_ESTUDIANTE = 25;
    final static int EDAD_MIN_TERCERA_EDAD = 65;
    final static int TOTAL_ASIENTOS = 5;
    final static int MAX_VENTAS = 10;
    final static int MAX_CLIENTES = 10;

    private static int ingresosTotales = 0;
    private static int proximoIdVenta = 1;
    private static int proximoIdCliente = 1;
    private static int proximoIdReserva = 1;
    private static final String NOMBRE_TEATRO = "Teatro Moro";

    private static Asiento[] arregloAsientos = new Asiento[TOTAL_ASIENTOS];
    private static Venta[] arregloVentas = new Venta[MAX_VENTAS];
    private static Cliente[] arregloClientes = new Cliente[MAX_CLIENTES];
    private static int contadorVentas = 0;
    private static int contadorClientes = 0;

    private static ArrayList<Reserva> listaReservas = new ArrayList<>();
    private static ArrayList<Descuento> listaDescuentos = new ArrayList<>();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarSistema(); // Prepara arreglos y listas iniciales
        System.out.println("Bienvenido al Sistema Optimizado del " + NOMBRE_TEATRO + " (v.S8)");
        boolean continuarSistema = true;

        do {
            mostrarMenu();
            int opcion = leerOpcionNumerica();

            switch (opcion) {
                case 1: venderEntrada(); break;
                case 2: reservarAsiento(); break; // Nueva opción para separar reserva
                case 3: cancelarReserva(); break; // Nueva opción
                case 4: visualizarAsientos(); break;
                case 5: visualizarVentas(); break;
                case 6: generarBoleta(); break;
                case 7: mostrarIngresosTotales(); break;
                case 8: eliminarVenta(); break; // Nueva opción - CRUD Arreglo
                case 9: actualizarClienteVenta(); break; // Nueva opción - CRUD Arreglo
                case 0:
                    continuarSistema = false;
                    System.out.println("\nSaliendo del sistema...");
                    System.out.println("Gracias por usar el sistema del " + NOMBRE_TEATRO + ".");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
            if (continuarSistema && opcion != 0) {
                pausaParaContinuar();
            }
        } while (continuarSistema);

        scanner.close();
        System.out.println("Sistema cerrado.");
    }

    public static void inicializarSistema() {
        for (int i = 0; i < TOTAL_ASIENTOS; i++) {
            arregloAsientos[i] = new Asiento(i + 1); // Asientos numerados 1 a TOTAL_ASIENTOS
        }

        listaDescuentos.add(new Descuento("Estudiante", 10));
        listaDescuentos.add(new Descuento("Tercera Edad", 15));

    }

    public static void mostrarMenu() {
        System.out.println("\n--- MENÚ TEATRO MORO (S8) ---");
        System.out.println("1. Vender Entrada Directa");
        System.out.println("2. Reservar Asiento");
        System.out.println("3. Cancelar Reserva");
        System.out.println("4. Visualizar Estado Asientos");
        System.out.println("5. Visualizar Resumen de Ventas");
        System.out.println("6. Generar Boleta (por Nº Venta)");
        System.out.println("7. Mostrar Ingresos Totales");
        System.out.println("--- Gestión Avanzada ---");
        System.out.println("8. Eliminar Venta (por Nº Venta)");
        System.out.println("9. Actualizar Edad Cliente (por Nº Venta)");
        System.out.println("---------------------------");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }
    public static void venderEntrada() {
        System.out.println("\n--- Venta Directa de Entrada ---");
        visualizarAsientos();

        if (contadorVentas >= MAX_VENTAS || contadorClientes >= MAX_CLIENTES) {
            System.out.println("Error: Se ha alcanzado la capacidad máxima de ventas o clientes.");
            return;
        }

        System.out.print("Seleccione número de asiento a comprar (1-" + TOTAL_ASIENTOS + "): ");
        int numAsiento = leerOpcionNumerica();

        if (numAsiento < 1 || numAsiento > TOTAL_ASIENTOS) {
            System.out.println("Error: Número de asiento inválido.");
            return;
        }
        int indiceAsiento = numAsiento - 1;
        if (!arregloAsientos[indiceAsiento].estado.equals("Disponible")) {
            System.out.println("Error: El asiento " + numAsiento + " no está disponible (Estado: " + arregloAsientos[indiceAsiento].estado + ").");
            return;
        }

        System.out.print("Ingrese edad del cliente: ");
        int edad = leerOpcionNumerica();
        if (edad < 0) { System.out.println("Edad inválida."); return; }

        String tipoCliente = "General";
        int porcDesc = 0;
        if (edad <= EDAD_MAX_ESTUDIANTE) { tipoCliente = "Estudiante"; porcDesc = 10; }
        else if (edad >= EDAD_MIN_TERCERA_EDAD) { tipoCliente = "Tercera Edad"; porcDesc = 15; }

        String ubicacion = "Platea";
        int costoBase = PRECIO_PLATEA;

        int descuento = (costoBase * porcDesc) / 100;
        int costoFinal = costoBase - descuento;

        int idClienteNuevo = proximoIdCliente++;
        Cliente nuevoCliente = new Cliente(idClienteNuevo, edad, tipoCliente);
        int indiceCliente = -1;
        for(int i=0; i < MAX_CLIENTES; i++){
            if(arregloClientes[i] == null){
                indiceCliente = i;
                break;
            }
        }
        if(indiceCliente != -1){
             arregloClientes[indiceCliente] = nuevoCliente;
             contadorClientes++;
        } else {
             System.out.println("Error interno: No hay espacio para más clientes.");
             proximoIdCliente--; 
             return;
        }

        int idVentaNueva = proximoIdVenta++;
        Venta nuevaVenta = new Venta(idVentaNueva, numAsiento, ubicacion, idClienteNuevo, costoBase, descuento, costoFinal, porcDesc);
        int indiceVenta = -1;
        for(int i=0; i < MAX_VENTAS; i++){
            if(arregloVentas[i] == null){
                indiceVenta = i;
                break;
            }
        }
         if(indiceVenta != -1){
            arregloVentas[indiceVenta] = nuevaVenta;
            contadorVentas++;
        } else {
             System.out.println("Error interno: No hay espacio para más ventas."); // No debería pasar
             arregloClientes[indiceCliente] = null;
             contadorClientes--;
             proximoIdCliente--;
             proximoIdVenta--;
             return;
        }

        arregloAsientos[indiceAsiento].estado = "Vendido";
        arregloAsientos[indiceAsiento].idReferencia = idVentaNueva;

        ingresosTotales += costoFinal;

        System.out.println("------------------------------------");
        System.out.println("¡VENTA #" + idVentaNueva + " REGISTRADA EXITOSAMENTE!");
        System.out.println("Asiento: " + numAsiento + " | Cliente ID: " + idClienteNuevo + " | Costo Final: $" + costoFinal);
        System.out.println("------------------------------------");
    }

    public static void reservarAsiento() {
        System.out.println("\n--- Reservar Asiento ---");
        visualizarAsientos();

        System.out.print("Seleccione número de asiento a reservar (1-" + TOTAL_ASIENTOS + "): ");
        int numAsiento = leerOpcionNumerica();

        if (numAsiento < 1 || numAsiento > TOTAL_ASIENTOS) {
            System.out.println("Error: Número de asiento inválido.");
            return;
        }
        int indiceAsiento = numAsiento - 1;
        if (!arregloAsientos[indiceAsiento].estado.equals("Disponible")) {
            System.out.println("Error: El asiento " + numAsiento + " no está disponible.");
            return;
        }

        int idClienteParaReserva = 0;
        System.out.println("Nota: Las reservas son temporales y anónimas por ahora.");

        int idReservaNueva = proximoIdReserva++;
        Reserva nuevaReserva = new Reserva(idReservaNueva, numAsiento, idClienteParaReserva);
        listaReservas.add(nuevaReserva); 


        arregloAsientos[indiceAsiento].estado = "Reservado";
        arregloAsientos[indiceAsiento].idReferencia = idReservaNueva;

        System.out.println("¡Asiento " + numAsiento + " reservado con éxito! Número de Reserva: " + idReservaNueva);
    }


    public static void cancelarReserva() {
        System.out.println("\n--- Cancelar Reserva ---");
        if (listaReservas.isEmpty()) {
            System.out.println("No hay reservas activas para cancelar.");
            return;
        }

        System.out.println("Reservas activas:");
        for (Reserva r : listaReservas) {
            System.out.println("  Reserva #" + r.idReserva + " para Asiento " + r.idAsiento);
        }
        System.out.print("Ingrese el número de la reserva a cancelar: ");
        int idReservaCancelar = leerOpcionNumerica();

        Reserva reservaEncontrada = null;
        int indiceLista = -1;
        for (int i = 0; i < listaReservas.size(); i++) {
            if (listaReservas.get(i).idReserva == idReservaCancelar) {
                reservaEncontrada = listaReservas.get(i);
                indiceLista = i;
                break;
            }
        }

        if (reservaEncontrada != null) {
            int numAsientoLiberar = reservaEncontrada.idAsiento;
            int indiceAsiento = numAsientoLiberar - 1;


            if (arregloAsientos[indiceAsiento].estado.equals("Reservado") &&
                arregloAsientos[indiceAsiento].idReferencia == idReservaCancelar)
            {

                arregloAsientos[indiceAsiento].estado = "Disponible";
                arregloAsientos[indiceAsiento].idReferencia = 0;


                listaReservas.remove(indiceLista);

                System.out.println("Reserva #" + idReservaCancelar + " cancelada. Asiento " + numAsientoLiberar + " liberado.");
            } else {
                System.out.println("Error de consistencia: El asiento " + numAsientoLiberar + " no coincide con la reserva " + idReservaCancelar + ".");
            }
        } else {
            System.out.println("No se encontró una reserva activa con el número " + idReservaCancelar + ".");
        }
    }

    public static void visualizarAsientos() {
        System.out.println("\n--- Estado de Asientos ---");
        for (int i = 0; i < TOTAL_ASIENTOS; i++) {
            System.out.print("Asiento " + arregloAsientos[i].numeroAsiento + ": " + arregloAsientos[i].estado);
            if (!arregloAsientos[i].estado.equals("Disponible")) {
                System.out.print(" (Ref ID: " + arregloAsientos[i].idReferencia + ")");
            }
            System.out.println(); 
        }
        System.out.println("------------------------");
    }


    public static void visualizarVentas() {
        System.out.println("\n--- Resumen de Ventas Registradas ---");
        if (contadorVentas == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        System.out.println("Mostrando " + contadorVentas + " venta(s):");
        for (int i = 0; i < MAX_VENTAS; i++) {
            if (arregloVentas[i] != null) { // Muestra solo los no nulos
                Venta v = arregloVentas[i];
                 String tipoCli = "Desconocido";
                 for(int j=0; j<MAX_CLIENTES; j++){
                     if(arregloClientes[j] != null && arregloClientes[j].idCliente == v.idCliente){
                         tipoCli = arregloClientes[j].tipoCliente;
                         break;
                     }
                 }
                System.out.println(" Venta #" + v.idVenta + " | Asiento:" + v.idAsiento + " ("+v.ubicacion+")" +
                                   " | ClienteID:" + v.idCliente + " ("+tipoCli+")" +
                                   " | Final:$" + v.costoFinal);
            }
        }
        System.out.println("-------------------------------------");
    }

    public static void generarBoleta() {
        System.out.println("\n--- Generar Boleta ---");
        if (contadorVentas == 0) { System.out.println("No hay ventas."); return; }

        System.out.print("Ingrese el Número de Venta: ");
        int numVentaBuscar = leerOpcionNumerica();

        Venta ventaEncontrada = null;

        for (int i = 0; i < MAX_VENTAS; i++) {
            if (arregloVentas[i] != null && arregloVentas[i].idVenta == numVentaBuscar) {
                ventaEncontrada = arregloVentas[i];
                break;
            }
        }

        if (ventaEncontrada != null) {
            System.out.println("\n***********************************");
            System.out.println("         " + NOMBRE_TEATRO + "         ");
            System.out.println("-----------------------------------");
            System.out.println(" Venta #: " + ventaEncontrada.idVenta);
            System.out.println(" Asiento: " + ventaEncontrada.idAsiento + " (" + ventaEncontrada.ubicacion + ")");
            System.out.println(" Costo Base: $" + ventaEncontrada.costoBase);
            System.out.println(" Descuento Aplicado: " + ventaEncontrada.porcentajeDescuento + "%" +
                               " ($" + ventaEncontrada.descuentoAplicado + ")");
            System.out.println(" Costo Final: $" + ventaEncontrada.costoFinal);
            System.out.println("-----------------------------------");
            System.out.println(" Gracias por su visita al " + NOMBRE_TEATRO + " ");
            System.out.println("***********************************");
        } else {
            System.out.println("Venta #" + numVentaBuscar + " no encontrada.");
        }
    }

    public static void mostrarIngresosTotales() {
        System.out.println("\n--- Ingresos Totales ---");
        System.out.println("Total Recaudado: $" + ingresosTotales);
        System.out.println("----------------------");
    }


    public static void eliminarVenta() {
        System.out.println("\n--- Eliminar Venta ---");
        if (contadorVentas == 0) { System.out.println("No hay ventas para eliminar."); return; }

        System.out.print("Ingrese el Número de Venta a eliminar: ");
        int idVentaEliminar = leerOpcionNumerica();

        int indiceVenta = -1;
        Venta ventaAEliminar = null;


        for (int i = 0; i < MAX_VENTAS; i++) {
            if (arregloVentas[i] != null && arregloVentas[i].idVenta == idVentaEliminar) {
                indiceVenta = i;
                ventaAEliminar = arregloVentas[i];
                break;
            }
        }

        if (ventaAEliminar != null) {
            int numAsientoLiberar = ventaAEliminar.idAsiento;
            int indiceAsiento = numAsientoLiberar - 1;

            // Validar consistencia (que el asiento apunte a esta venta)
            if (arregloAsientos[indiceAsiento].estado.equals("Vendido") &&
                arregloAsientos[indiceAsiento].idReferencia == idVentaEliminar)
            {

                ingresosTotales -= ventaAEliminar.costoFinal;


                arregloAsientos[indiceAsiento].estado = "Disponible";
                arregloAsientos[indiceAsiento].idReferencia = 0;


                arregloVentas[indiceVenta] = null;
                contadorVentas--; // Decrementar contador



                System.out.println("Venta #" + idVentaEliminar + " eliminada. Asiento " + numAsientoLiberar + " liberado.");
                System.out.println("Ingresos totales ajustados.");

            } else {
                 System.out.println("Error de consistencia: El asiento " + numAsientoLiberar + " no corresponde a la venta " + idVentaEliminar + ".");
            }
        } else {
            System.out.println("Venta #" + idVentaEliminar + " no encontrada.");
        }
    }


    public static void actualizarClienteVenta() {
        System.out.println("\n--- Actualizar Edad Cliente y Recalcular Venta ---");
        if (contadorVentas == 0) { System.out.println("No hay ventas."); return; }

        System.out.print("Ingrese el Número de Venta a modificar: ");
        int idVentaModificar = leerOpcionNumerica();

        int indiceVenta = -1;
        Venta ventaAModificar = null;


        for (int i = 0; i < MAX_VENTAS; i++) {
            if (arregloVentas[i] != null && arregloVentas[i].idVenta == idVentaModificar) {
                indiceVenta = i;
                ventaAModificar = arregloVentas[i];
                break;
            }
        }

        if (ventaAModificar != null) {
            int idClienteAsociado = ventaAModificar.idCliente;
            int indiceCliente = -1;
            Cliente clienteAModificar = null;


            for (int i = 0; i < MAX_CLIENTES; i++) {
                if (arregloClientes[i] != null && arregloClientes[i].idCliente == idClienteAsociado) {
                    indiceCliente = i;
                    clienteAModificar = arregloClientes[i];
                    break;
                }
            }

            if (clienteAModificar != null) {
                System.out.println("Venta #" + idVentaModificar + " encontrada. Cliente ID: " + idClienteAsociado + " (Edad actual: " + clienteAModificar.edad + ")");
                System.out.print("Ingrese la NUEVA edad correcta del cliente: ");
                int nuevaEdad = leerOpcionNumerica();
                if (nuevaEdad < 0) { System.out.println("Edad inválida."); return; }


                int costoFinalAntiguo = ventaAModificar.costoFinal;


                String nuevoTipoCliente = "General";
                int nuevoPorcDesc = 0;
                if (nuevaEdad <= EDAD_MAX_ESTUDIANTE) { nuevoTipoCliente = "Estudiante"; nuevoPorcDesc = 10; }
                else if (nuevaEdad >= EDAD_MIN_TERCERA_EDAD) { nuevoTipoCliente = "Tercera Edad"; nuevoPorcDesc = 15; }

                int nuevoDescuento = (ventaAModificar.costoBase * nuevoPorcDesc) / 100;
                int nuevoCostoFinal = ventaAModificar.costoBase - nuevoDescuento;


                clienteAModificar.edad = nuevaEdad;
                clienteAModificar.tipoCliente = nuevoTipoCliente;

                ventaAModificar.descuentoAplicado = nuevoDescuento;
                ventaAModificar.costoFinal = nuevoCostoFinal;
                ventaAModificar.porcentajeDescuento = nuevoPorcDesc;

                ingresosTotales = ingresosTotales - costoFinalAntiguo + nuevoCostoFinal;

                System.out.println("Datos actualizados para Venta #" + idVentaModificar + " y Cliente ID " + idClienteAsociado + ".");
                System.out.println("Nuevo tipo: " + nuevoTipoCliente + ", Nuevo costo final: $" + nuevoCostoFinal);
                System.out.println("Ingresos totales ajustados.");

            } else {
                System.out.println("Error de consistencia: No se encontró el cliente ID " + idClienteAsociado + " asociado a la venta.");
            }
        } else {
            System.out.println("Venta #" + idVentaModificar + " no encontrada.");
        }
    }



    public static int leerOpcionNumerica() {
        int numero = -1;
        try {
            numero = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println(" >> Error: Ingrese un valor numérico.");
        } finally {
            scanner.nextLine();
        }
        return numero;
    }

     public static void pausaParaContinuar() {
         System.out.print("\n(Presione Enter para continuar...)");
         scanner.nextLine();
     }

}