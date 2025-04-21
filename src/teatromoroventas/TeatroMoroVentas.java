package teatromoroventas;

import java.util.Scanner;
import java.util.InputMismatchException; // Para manejar errores si se ingresa texto en lugar de número

/**
 * Sistema de Reserva y Venta de Entradas - Teatro Moro
 * Versión Semana 6: Adaptado para NO usar Arrays ni Listas, limitado a 5 asientos.
 * Incluye funcionalidades de Reserva, Compra (confirmación de reserva),
 * Modificación de Venta/Reserva e Impresión de Boleto.
 */
public class TeatroMoroVentas {

    // --- Constantes (Valores fijos para el programa) ---
    final static int PRECIO_ASIENTO = 20000; // Precio único por asiento para simplificar
    final static int PORCENTAJE_DESCUENTO_ESTUDIANTE = 10; // 10%
    final static int PORCENTAJE_DESCUENTO_TERCERA_EDAD = 15; // 15%
    final static int EDAD_MAX_ESTUDIANTE = 25;
    final static int EDAD_MIN_TERCERA_EDAD = 65;
    final static int TOTAL_ASIENTOS = 5; // Límite estricto de asientos según requerimiento

    // --- Variables Estáticas Globales (Requisito PDF: 3 o más) ---
    // Guardan información general del sistema o estadísticas acumuladas.
    private static int ingresosTotales = 0; // Dinero total recaudado por ventas confirmadas
    private static int entradasVendidasTotal = 0; // Número total de asientos actualmente vendidos
    private static int reservasActivas = 0; // Número total de asientos actualmente reservados (no confirmados)
    private static final String NOMBRE_TEATRO = "Teatro Moro"; // Nombre del teatro (constante)
    private static int proximoNumeroVenta = 1; // Contador para asignar ID únicos a las ventas
    private static int proximoNumeroReserva = 1; // Contador para asignar ID únicos a las reservas

    // --- "Variables de Instancia" simuladas con variables estáticas individuales ---
    // RESTRICCIÓN SEMANA 6: No usar Arrays ni Listas.
    // Para manejar el estado y detalle de cada uno de los 5 asientos, usamos variables separadas.
    // Aunque son 'static' (porque estamos en métodos static), conceptualmente representan
    // la información persistente de cada "objeto" asiento individual. (Requisito PDF: 4 o más)

    // Estado de cada asiento: "Disponible", "Reservado", "Vendido"
    private static String estadoAsiento1 = "Disponible";
    private static String estadoAsiento2 = "Disponible";
    private static String estadoAsiento3 = "Disponible";
    private static String estadoAsiento4 = "Disponible";
    private static String estadoAsiento5 = "Disponible";

    // Detalles almacenados para cada asiento (si no está disponible)
    // Se guarda como un String formateado con ';' como separador.
    // Formato si está Reservado: "Reservado;numeroReserva"
    // Formato si está Vendido: "Vendido;numeroVenta;tipoCliente;precioBase;descuento;precioFinal"
    private static String detalleAsiento1 = "";
    private static String detalleAsiento2 = "";
    private static String detalleAsiento3 = "";
    private static String detalleAsiento4 = "";
    private static String detalleAsiento5 = "";


    // --- Objeto Scanner (Para leer entrada del teclado) ---
    // Se declara static para poder usarlo desde todos los métodos static.
    private static Scanner scanner = new Scanner(System.in);

    // --- Método Principal (main) ---
    // Punto de inicio de la ejecución del programa.
    public static void main(String[] args) {
        System.out.println("Bienvenido al Sistema de Reservas y Ventas del " + NOMBRE_TEATRO + " (v.S6 - Sin Listas)");
        boolean continuarSistema = true; // Controla el bucle del menú principal

        // Bucle do-while para mostrar el menú repetidamente (Requisito PDF)
        do {
            mostrarMenu(); // Llama al método que imprime el menú
            int opcionSeleccionada = leerOpcionNumerica(); // Llama al método que lee la opción del usuario

            // Estructura switch para ejecutar la acción correspondiente a la opción
            switch (opcionSeleccionada) {
                case 1:
                    reservarEntrada(); // Llama a la función de reservar
                    break;
                case 2:
                    comprarEntrada(); // Llama a la función de comprar (confirmar reserva)
                    break;
                case 3:
                    modificarVentaReserva(); // Llama a la función de modificar
                    break;
                case 4:
                    imprimirBoleto(); // Llama a la función de imprimir boleto
                    break;
                case 5:
                    mostrarEstadoAsientos(); // Muestra el estado actual de los asientos
                    break;
                case 6:
                    mostrarEstadisticas(); // Muestra las estadísticas globales
                    break;
                case 0:
                    continuarSistema = false; // Cambia la condición para salir del bucle do-while
                    System.out.println("Saliendo del sistema de reservas...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, elija una opción del menú.");
            } // Fin del switch

            // Pausa después de cada acción (excepto al salir) para que el usuario vea el resultado
            if (continuarSistema && opcionSeleccionada != 0) {
                pausaParaContinuar(); // Llama a un método simple que espera un Enter
            }

        } while (continuarSistema); // El bucle continúa mientras 'continuarSistema' sea true

        scanner.close(); // Cierra el scanner para liberar recursos al final del programa
        System.out.println("Gracias por utilizar el sistema del " + NOMBRE_TEATRO + ". ¡Vuelva pronto!");
    } // Fin del método main

    // --- Métodos de Funcionalidades del Sistema ---

    /**
     * Imprime las opciones del menú principal en la consola.
     */
    public static void mostrarMenu() {
        System.out.println("\n===================================");
        System.out.println("   Menú Principal - " + NOMBRE_TEATRO);
        System.out.println("===================================");
        System.out.println("1. Reservar Asiento");
        System.out.println("2. Comprar Entrada (Confirmar Reserva)");
        System.out.println("3. Modificar / Cancelar Venta o Reserva");
        System.out.println("4. Imprimir Boleto (Venta Confirmada)");
        System.out.println("-----------------------------------");
        System.out.println("5. Ver Estado Actual de Asientos");
        System.out.println("6. Ver Estadísticas de Ventas");
        System.out.println("-----------------------------------");
        System.out.println("0. Salir del Sistema");
        System.out.println("===================================");
        System.out.print("Ingrese su opción: ");
    }

    /**
     * Muestra el estado ('Disponible', 'Reservado', 'Vendido') de cada uno de los 5 asientos,
     * junto con un breve resumen del detalle si no está disponible.
     */
    public static void mostrarEstadoAsientos() {
        System.out.println("\n--- Estado Actual de los " + TOTAL_ASIENTOS + " Asientos ---");
        // Se accede a las variables estáticas de cada asiento directamente
        System.out.println("Asiento 1: " + estadoAsiento1 + (detalleAsiento1.isEmpty() ? "" : " (" + obtenerResumenDetalle(detalleAsiento1) + ")"));
        System.out.println("Asiento 2: " + estadoAsiento2 + (detalleAsiento2.isEmpty() ? "" : " (" + obtenerResumenDetalle(detalleAsiento2) + ")"));
        System.out.println("Asiento 3: " + estadoAsiento3 + (detalleAsiento3.isEmpty() ? "" : " (" + obtenerResumenDetalle(detalleAsiento3) + ")"));
        System.out.println("Asiento 4: " + estadoAsiento4 + (detalleAsiento4.isEmpty() ? "" : " (" + obtenerResumenDetalle(detalleAsiento4) + ")"));
        System.out.println("Asiento 5: " + estadoAsiento5 + (detalleAsiento5.isEmpty() ? "" : " (" + obtenerResumenDetalle(detalleAsiento5) + ")"));
        System.out.println("------------------------------------");
    }

    /**
     * Función auxiliar para obtener un resumen corto del detalle de un asiento.
     * Se usa en mostrarEstadoAsientos para no mostrar toda la cadena de detalle.
     * @param detalle El string completo del detalle (ej: "Reservado;1" o "Vendido;1;General...")
     * @return Una cadena corta como "Reserva #1" o "Venta #1", o "" si está vacío.
     */
    public static String obtenerResumenDetalle(String detalle) {
        if (detalle == null || detalle.isEmpty()) {
            return ""; // Si no hay detalle, devuelve vacío
        }
        // Divide el string por el separador ';'
        String[] partes = detalle.split(";");
        if (partes.length > 1) { // Asegurarse que haya al menos dos partes
            if (partes[0].equals("Reservado")) {
                return "Reserva #" + partes[1]; // Devuelve "Reserva #" seguido del número
            } else if (partes[0].equals("Vendido")) {
                return "Venta #" + partes[1]; // Devuelve "Venta #" seguido del número
            }
        }
        return ""; // Si el formato no es esperado, devuelve vacío
    }


    /**
     * Gestiona el proceso de reservar un asiento que esté disponible.
     */
    public static void reservarEntrada() {
        System.out.println("\n--- Reservar Asiento ---");
        mostrarEstadoAsientos(); // Muestra al usuario qué asientos están libres

        System.out.print("Seleccione el número del asiento a reservar (1-" + TOTAL_ASIENTOS + ") o 0 para volver: ");
        int asientoSeleccionado = leerOpcionNumerica();

        // Validar que el número de asiento esté en el rango correcto o sea 0 para cancelar
        if (asientoSeleccionado == 0) {
             System.out.println("Reserva cancelada por el usuario.");
             return; // Vuelve al menú
        }
        if (asientoSeleccionado < 1 || asientoSeleccionado > TOTAL_ASIENTOS) {
            System.out.println("Error: Número de asiento fuera de rango (1-" + TOTAL_ASIENTOS + ").");
            return; // Vuelve al menú
        }

        // Validar disponibilidad del asiento seleccionado (Requisito)
        String estadoActual = ""; // Variable local para guardar el estado del asiento elegido
        boolean disponible = false; // Variable local booleana

        // Usamos un switch para verificar el estado del asiento seleccionado
        switch (asientoSeleccionado) {
            case 1: estadoActual = estadoAsiento1; break;
            case 2: estadoActual = estadoAsiento2; break;
            case 3: estadoActual = estadoAsiento3; break;
            case 4: estadoActual = estadoAsiento4; break;
            case 5: estadoActual = estadoAsiento5; break;
        }
        disponible = estadoActual.equals("Disponible"); // Comprueba si el estado es "Disponible"

        // Si el asiento está disponible, proceder con la reserva
        if (disponible) {
            int numeroReservaActual = proximoNumeroReserva++; // Obtiene el siguiente número y lo incrementa
            String detalleReserva = "Reservado;" + numeroReservaActual; // Crea el string de detalle

            // Actualizar el estado y detalle del asiento específico usando otro switch
            switch (asientoSeleccionado) {
                case 1: estadoAsiento1 = "Reservado"; detalleAsiento1 = detalleReserva; break;
                case 2: estadoAsiento2 = "Reservado"; detalleAsiento2 = detalleReserva; break;
                case 3: estadoAsiento3 = "Reservado"; detalleAsiento3 = detalleReserva; break;
                case 4: estadoAsiento4 = "Reservado"; detalleAsiento4 = detalleReserva; break;
                case 5: estadoAsiento5 = "Reservado"; detalleAsiento5 = detalleReserva; break;
            }
            reservasActivas++; // Incrementa el contador de reservas activas
            System.out.println("------------------------------------");
            System.out.println("¡Asiento " + asientoSeleccionado + " reservado con éxito!");
            System.out.println("Su Número de Reserva es: " + numeroReservaActual);
            System.out.println("Recuerde este número para confirmar su compra (Opción 2 del menú).");
            System.out.println("------------------------------------");

        } else {
            // Si el asiento no estaba disponible
            System.out.println("Error: El asiento " + asientoSeleccionado + " no está disponible en este momento.");
            System.out.println("Se encuentra en estado: " + estadoActual);
        }
    } // Fin del método reservarEntrada

    /**
     * Gestiona la compra de una entrada, que implica confirmar una reserva existente.
     * Pide el número de reserva, calcula el precio final con descuentos y actualiza el estado.
     */
    public static void comprarEntrada() {
        System.out.println("\n--- Comprar Entrada (Confirmar una Reserva Existente) ---");
        // Verifica si hay reservas que confirmar
        if (reservasActivas == 0) {
            System.out.println("Actualmente no hay asientos reservados pendientes de confirmación.");
            System.out.println("Por favor, reserve un asiento primero (Opción 1).");
            return; // Salir si no hay reservas
        }

        System.out.print("Ingrese el Número de Reserva que desea confirmar: ");
        int numReservaBuscado = leerOpcionNumerica();

        if (numReservaBuscado <= 0) {
            System.out.println("Error: Número de reserva no válido.");
            return;
        }

        // Variables locales para buscar la reserva
        int asientoEncontrado = 0; // Guardará el número del asiento (1-5) si se encuentra la reserva
        String detalleReservaEncontrado = ""; // Guardará el string de detalle ("Reservado;numero")

        // Búsqueda manual en cada variable de detalle (sin arrays/listas)
        if (!detalleAsiento1.isEmpty() && detalleAsiento1.startsWith("Reservado;") && Integer.parseInt(detalleAsiento1.split(";")[1]) == numReservaBuscado) { asientoEncontrado = 1; detalleReservaEncontrado = detalleAsiento1;}
        else if (!detalleAsiento2.isEmpty() && detalleAsiento2.startsWith("Reservado;") && Integer.parseInt(detalleAsiento2.split(";")[1]) == numReservaBuscado) { asientoEncontrado = 2; detalleReservaEncontrado = detalleAsiento2;}
        else if (!detalleAsiento3.isEmpty() && detalleAsiento3.startsWith("Reservado;") && Integer.parseInt(detalleAsiento3.split(";")[1]) == numReservaBuscado) { asientoEncontrado = 3; detalleReservaEncontrado = detalleAsiento3;}
        else if (!detalleAsiento4.isEmpty() && detalleAsiento4.startsWith("Reservado;") && Integer.parseInt(detalleAsiento4.split(";")[1]) == numReservaBuscado) { asientoEncontrado = 4; detalleReservaEncontrado = detalleAsiento4;}
        else if (!detalleAsiento5.isEmpty() && detalleAsiento5.startsWith("Reservado;") && Integer.parseInt(detalleAsiento5.split(";")[1]) == numReservaBuscado) { asientoEncontrado = 5; detalleReservaEncontrado = detalleAsiento5;}


        // Si se encontró la reserva (asientoEncontrado > 0)
        if (asientoEncontrado > 0) {
            System.out.println("Reserva #" + numReservaBuscado + " encontrada. Corresponde al Asiento " + asientoEncontrado + ".");
            System.out.println("Para finalizar la compra, necesitamos la edad del cliente.");
            System.out.print("Ingrese la edad del cliente: ");
            int edadCliente = leerOpcionNumerica();

            if (edadCliente < 0) {
                System.out.println("Error: Edad inválida. La compra no puede continuar.");
                return; // Salir si la edad es inválida
            }

            // --- Variables Locales para el cálculo (Requisito PDF: 4 o más) ---
            int precioBaseLocal = PRECIO_ASIENTO;       // Precio base para este asiento
            int descuentoCalculadoLocal = 0;            // Descuento para este cliente
            String tipoClienteLocal = "General";        // Tipo de cliente según edad
            String estadoTemporalCompra = "Procesando"; // Ejemplo de variable local de estado

            // Calcular descuento basado en la edad ingresada
            if (edadCliente <= EDAD_MAX_ESTUDIANTE && edadCliente >=0) { // Es Estudiante
                descuentoCalculadoLocal = (precioBaseLocal * PORCENTAJE_DESCUENTO_ESTUDIANTE) / 100;
                tipoClienteLocal = "Estudiante";
            } else if (edadCliente >= EDAD_MIN_TERCERA_EDAD) { // Es Tercera Edad
                descuentoCalculadoLocal = (precioBaseLocal * PORCENTAJE_DESCUENTO_TERCERA_EDAD) / 100;
                tipoClienteLocal = "Tercera Edad";
            }
            // Si no cumple ninguna, el tipo es General y el descuento es 0 (ya inicializado)

            int precioFinalLocal = precioBaseLocal - descuentoCalculadoLocal; // Calcula el precio final
            int numeroVentaActual = proximoNumeroVenta++; // Obtiene el siguiente número de venta y lo incrementa

            // Crear el nuevo string de detalle para la VENTA confirmada
            // Formato: "Vendido;numeroVenta;tipoCliente;precioBase;descuento;precioFinal"
            String detalleVentaConfirmada = "Vendido;" + numeroVentaActual + ";" + tipoClienteLocal + ";" +
                                            precioBaseLocal + ";" + descuentoCalculadoLocal + ";" + precioFinalLocal;

            // Actualizar el estado y detalle del asiento correspondiente
             switch (asientoEncontrado) {
                case 1: estadoAsiento1 = "Vendido"; detalleAsiento1 = detalleVentaConfirmada; break;
                case 2: estadoAsiento2 = "Vendido"; detalleAsiento2 = detalleVentaConfirmada; break;
                case 3: estadoAsiento3 = "Vendido"; detalleAsiento3 = detalleVentaConfirmada; break;
                case 4: estadoAsiento4 = "Vendido"; detalleAsiento4 = detalleVentaConfirmada; break;
                case 5: estadoAsiento5 = "Vendido"; detalleAsiento5 = detalleVentaConfirmada; break;
            }

            // Actualizar las estadísticas globales
            ingresosTotales = ingresosTotales + precioFinalLocal; // Sumar al total recaudado
            entradasVendidasTotal++; // Incrementar el contador de vendidas
            reservasActivas--; // Decrementar el contador de reservas (se convirtió en venta)

            System.out.println("------------------------------------");
            System.out.println("¡Compra confirmada exitosamente!");
            System.out.println("Asiento: " + asientoEncontrado);
            System.out.println("Número de Venta: " + numeroVentaActual);
            System.out.println("Cliente: " + tipoClienteLocal);
            System.out.println("Precio Final: $" + precioFinalLocal);
            System.out.println("------------------------------------");
            System.out.println("Puede imprimir su boleto usando la opción 4 del menú.");

        } else {
            // Si no se encontró el número de reserva ingresado
            System.out.println("Error: El número de reserva " + numReservaBuscado + " no fue encontrado o ya fue confirmado.");
            System.out.println("Por favor, verifique el número o reserve un asiento (Opción 1).");
        }
    } // Fin del método comprarEntrada

    /**
     * Permite al usuario modificar una venta existente (cancelándola o cambiando tipo de cliente)
     * o cancelar una reserva activa.
     */
    public static void modificarVentaReserva() {
        System.out.println("\n--- Modificar / Cancelar Venta o Reserva ---");
        mostrarEstadoAsientos(); // Muestra el estado para facilitar la elección
        System.out.print("Ingrese el número del asiento (1-" + TOTAL_ASIENTOS + ") que desea gestionar o 0 para volver: ");
        int asientoModificar = leerOpcionNumerica();

        // Validar asiento
         if (asientoModificar == 0) {
             System.out.println("Operación cancelada.");
             return;
         }
         if (asientoModificar < 1 || asientoModificar > TOTAL_ASIENTOS) {
            System.out.println("Error: Número de asiento inválido.");
            return;
        }

        // Obtener estado y detalle actual del asiento elegido
        String estadoActual = "";
        String detalleActual = "";
        switch (asientoModificar) {
            case 1: estadoActual = estadoAsiento1; detalleActual = detalleAsiento1; break;
            case 2: estadoActual = estadoAsiento2; detalleActual = detalleAsiento2; break;
            case 3: estadoActual = estadoAsiento3; detalleActual = detalleAsiento3; break;
            case 4: estadoActual = estadoAsiento4; detalleActual = detalleAsiento4; break;
            case 5: estadoActual = estadoAsiento5; detalleActual = detalleAsiento5; break;
        }

        // Actuar según el estado del asiento
        switch (estadoActual) {
            case "Disponible":
                System.out.println("El asiento " + asientoModificar + " ya está Disponible. No hay nada que modificar o cancelar.");
                break;

            case "Reservado":
                String[] datosReserva = detalleActual.split(";");
                System.out.println("El asiento " + asientoModificar + " tiene la Reserva #" + datosReserva[1] + ".");
                System.out.print("¿Está seguro que desea CANCELAR esta reserva? (S/N): ");
                String confirmaCancelReserva = scanner.nextLine().trim().toUpperCase();
                if (confirmaCancelReserva.equals("S")) {
                    // Cancelar la Reserva
                    switch (asientoModificar) { // Poner el asiento como Disponible y borrar detalle
                        case 1: estadoAsiento1 = "Disponible"; detalleAsiento1 = ""; break;
                        case 2: estadoAsiento2 = "Disponible"; detalleAsiento2 = ""; break;
                        case 3: estadoAsiento3 = "Disponible"; detalleAsiento3 = ""; break;
                        case 4: estadoAsiento4 = "Disponible"; detalleAsiento4 = ""; break;
                        case 5: estadoAsiento5 = "Disponible"; detalleAsiento5 = ""; break;
                    }
                    reservasActivas--; // Decrementar contador de reservas
                    System.out.println("Reserva #" + datosReserva[1] + " para el asiento " + asientoModificar + " ha sido cancelada.");
                } else {
                    System.out.println("Operación cancelada. La reserva se mantiene.");
                }
                break;

            case "Vendido":
                String[] datosVenta = detalleActual.split(";");
                System.out.println("El asiento " + asientoModificar + " corresponde a la Venta #" + datosVenta[1] + ".");
                System.out.println("¿Qué desea hacer?");
                System.out.println("  1. Cancelar COMPLETAMENTE esta venta (libera asiento, resta ingresos).");
                System.out.println("  2. Modificar tipo de cliente (recalcular descuento y precio).");
                System.out.println("  0. No hacer nada.");
                System.out.print("Seleccione una opción: ");
                int opcionModVenta = leerOpcionNumerica();

                if (opcionModVenta == 1) { // Cancelar Venta
                    System.out.print("¿Está seguro que desea CANCELAR la Venta #" + datosVenta[1] + "? (S/N): ");
                    String confirmaCancelVenta = scanner.nextLine().trim().toUpperCase();
                    if (confirmaCancelVenta.equals("S")) {
                        int precioARestar = Integer.parseInt(datosVenta[5]); // Obtener precio final de los datos guardados
                        // Poner asiento como Disponible y borrar detalle
                        switch (asientoModificar) {
                            case 1: estadoAsiento1 = "Disponible"; detalleAsiento1 = ""; break;
                            case 2: estadoAsiento2 = "Disponible"; detalleAsiento2 = ""; break;
                            case 3: estadoAsiento3 = "Disponible"; detalleAsiento3 = ""; break;
                            case 4: estadoAsiento4 = "Disponible"; detalleAsiento4 = ""; break;
                            case 5: estadoAsiento5 = "Disponible"; detalleAsiento5 = ""; break;
                        }
                        // Revertir estadísticas
                        ingresosTotales = ingresosTotales - precioARestar;
                        entradasVendidasTotal--;
                        System.out.println("Venta #" + datosVenta[1] + " cancelada. El asiento " + asientoModificar + " ahora está Disponible.");
                    } else {
                        System.out.println("Cancelación de venta abortada.");
                    }
                } else if (opcionModVenta == 2) { // Modificar Tipo Cliente
                    System.out.println("Modificando Venta #" + datosVenta[1] + " para asiento " + asientoModificar);
                    System.out.print("Ingrese la NUEVA edad correcta del cliente: ");
                    int nuevaEdad = leerOpcionNumerica();
                    if (nuevaEdad < 0) {
                        System.out.println("Edad inválida. No se puede modificar.");
                        return;
                    }

                    // Obtener datos originales de la venta
                    int numVentaOriginal = Integer.parseInt(datosVenta[1]);
                    int precioBaseOriginal = Integer.parseInt(datosVenta[3]);
                    int precioAntiguo = Integer.parseInt(datosVenta[5]);

                    // Recalcular descuento y precio con la nueva edad
                    int nuevoDescuento = 0;
                    String nuevoTipoCliente = "General";
                    if (nuevaEdad <= EDAD_MAX_ESTUDIANTE && nuevaEdad >= 0) {
                        nuevoDescuento = (precioBaseOriginal * PORCENTAJE_DESCUENTO_ESTUDIANTE) / 100;
                        nuevoTipoCliente = "Estudiante";
                    } else if (nuevaEdad >= EDAD_MIN_TERCERA_EDAD) {
                        nuevoDescuento = (precioBaseOriginal * PORCENTAJE_DESCUENTO_TERCERA_EDAD) / 100;
                        nuevoTipoCliente = "Tercera Edad";
                    }
                    int nuevoPrecioFinal = precioBaseOriginal - nuevoDescuento;

                    // Crear el nuevo string de detalle actualizado
                    String nuevoDetalleVenta = "Vendido;" + numVentaOriginal + ";" + nuevoTipoCliente + ";" +
                                               precioBaseOriginal + ";" + nuevoDescuento + ";" + nuevoPrecioFinal;

                    // Actualizar el detalle del asiento correspondiente
                    switch (asientoModificar) {
                        case 1: detalleAsiento1 = nuevoDetalleVenta; break;
                        case 2: detalleAsiento2 = nuevoDetalleVenta; break;
                        case 3: detalleAsiento3 = nuevoDetalleVenta; break;
                        case 4: detalleAsiento4 = nuevoDetalleVenta; break;
                        case 5: detalleAsiento5 = nuevoDetalleVenta; break;
                    }

                    // Ajustar los ingresos totales: restar el precio antiguo y sumar el nuevo
                    ingresosTotales = ingresosTotales - precioAntiguo + nuevoPrecioFinal;

                    System.out.println("Venta #" + numVentaOriginal + " actualizada.");
                    System.out.println("Nuevo tipo cliente: " + nuevoTipoCliente);
                    System.out.println("Nuevo precio final: $" + nuevoPrecioFinal);
                    System.out.println("Los ingresos totales han sido ajustados.");

                } else { // Opción 0 o inválida
                    System.out.println("No se realizaron modificaciones en la venta.");
                }
                break; // Fin del case "Vendido"

            default: // Estado inesperado (no debería ocurrir)
                 System.out.println("Error: Estado desconocido para el asiento " + asientoModificar);
                 break;

        } // Fin del switch (estadoActual)
    } // Fin del método modificarVentaReserva


    /**
     * Imprime en formato de "boleto" los detalles de una venta confirmada,
     * buscando por el número de asiento.
     */
    public static void imprimirBoleto() {
        System.out.println("\n--- Imprimir Boleto de Venta Confirmada ---");
        System.out.print("Ingrese el número del asiento (1-" + TOTAL_ASIENTOS + ") para el cual desea imprimir el boleto: ");
        int asientoImprimir = leerOpcionNumerica();

        // Validar número de asiento
        if (asientoImprimir < 1 || asientoImprimir > TOTAL_ASIENTOS) {
            System.out.println("Error: Número de asiento inválido.");
            return;
        }

        // Obtener estado y detalle del asiento
        String estadoActual = "";
        String detalleActual = "";
        switch (asientoImprimir) {
            case 1: estadoActual = estadoAsiento1; detalleActual = detalleAsiento1; break;
            case 2: estadoActual = estadoAsiento2; detalleActual = detalleAsiento2; break;
            case 3: estadoActual = estadoAsiento3; detalleActual = detalleAsiento3; break;
            case 4: estadoActual = estadoAsiento4; detalleActual = detalleAsiento4; break;
            case 5: estadoActual = estadoAsiento5; detalleActual = detalleAsiento5; break;
        }


        // Solo imprimir si el asiento está "Vendido"
        if (estadoActual.equals("Vendido")) {

            // Separar los datos del string de detalle
            String[] datos = detalleActual.split(";");
            // Formato: "Vendido;numeroVenta;tipoCliente;precioBase;descuento;precioFinal"
            if (datos.length == 6) { // Verificar que tengamos todas las partes esperadas
                String numeroVenta = datos[1];
                String tipoCliente = datos[2];
                String precioBaseStr = datos[3];
                String descuentoStr = datos[4];
                String precioFinalStr = datos[5];

                // Imprimir el boleto con formato
                System.out.println("\n*****************************************");
                System.out.println("* BOLETO ELECTRÓNICO           *");
                System.out.println("* " + NOMBRE_TEATRO.toUpperCase() + "              *");
                System.out.println("*****************************************");
                // Usamos String.format para alinear un poco la salida
                System.out.println("* Asiento Adquirido:        " + String.format("%-10s", asientoImprimir));
                System.out.println("* Número de Venta:          " + String.format("%-10s", numeroVenta));
                System.out.println("* Tipo de Cliente:         " + String.format("%-10s", tipoCliente));
                System.out.println("*---------------------------------------*");
                System.out.println("* Precio Base:             $" + String.format("%-10s", precioBaseStr));
                System.out.println("* Descuento Aplicado:      $" + String.format("%-10s", descuentoStr));
                System.out.println("*=======================================*");
                System.out.println("* TOTAL A PAGAR:           $" + String.format("%-10s", precioFinalStr));
                System.out.println("*****************************************");
                System.out.println("* ¡Gracias por preferirnos!        *");
                System.out.println("*****************************************");

            } else {
                 System.out.println("Error interno: No se pudo procesar la información de la venta para este asiento.");
            }
        } else {
            // Si el asiento no está vendido
            System.out.println("Error: El asiento " + asientoImprimir + " no tiene una venta confirmada.");
            System.out.println("Estado actual: " + estadoActual + ".");
            System.out.println("Solo se pueden imprimir boletos para asientos 'Vendido'.");
        }
    } // Fin del método imprimirBoleto

     /**
      * Muestra las estadísticas globales del sistema: asientos vendidos, reservados,
      * disponibles e ingresos totales.
      */
    public static void mostrarEstadisticas() {
        System.out.println("\n--- Estadísticas Globales del Teatro ---");
        System.out.println("Total de Asientos en la Sala: " + TOTAL_ASIENTOS);
        System.out.println("------------------------------------");
        System.out.println("Asientos Actualmente Vendidos:  " + entradasVendidasTotal);
        System.out.println("Asientos Actualmente Reservados:" + reservasActivas);
        // Calcular disponibles restando vendidos y reservados del total
        int disponibles = TOTAL_ASIENTOS - entradasVendidasTotal - reservasActivas;
        System.out.println("Asientos Aún Disponibles:     " + disponibles);
        System.out.println("------------------------------------");
        System.out.println("Ingresos Totales por Ventas:   $" + ingresosTotales);
        System.out.println("====================================");
    }

    /**
     * Lee un número entero ingresado por el usuario desde la consola.
     * Incluye manejo básico para evitar que el programa se caiga si se ingresa texto.
     * @return El número entero leído, o -1 si la entrada no fue un entero válido.
     */
    public static int leerOpcionNumerica() {
        int numero = -1; // Valor por defecto en caso de error
        try {
            // Intenta leer el próximo entero de la entrada estándar
            numero = scanner.nextInt();
        } catch (InputMismatchException e) {
            // Si lo que se ingresó no era un entero, captura la excepción
            System.out.println(" >> Error: Se esperaba un número, pero ingresó otro tipo de dato.");
            // 'numero' se mantiene en -1
        } finally {
            // Esta parte se ejecuta SIEMPRE, haya habido excepción o no.
            // Es crucial para consumir el resto de la línea (incluyendo el 'Enter'
            // que el usuario presionó), preparando el scanner para la próxima lectura.
            scanner.nextLine();
        }
        return numero; // Devuelve el número leído o -1 si hubo error.
    }

     /**
      * Genera una pausa simple esperando que el usuario presione Enter.
      * Útil para que el usuario pueda leer los mensajes antes de volver al menú.
      */
     public static void pausaParaContinuar() {
         System.out.print("\n(Presione Enter para volver al menú principal)");
         scanner.nextLine(); // Simplemente espera a que el usuario presione Enter
     }

} // Fin de la clase TeatroMoroVentasS6SinDebug