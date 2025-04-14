/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package teatromoroventas;
import java.util.Scanner; // Necesitamos esto para leer lo que escribe el usuario
import java.util.ArrayList; // Para almacenar las entradas vendidas

public class TeatroMoroVentas {
    
    // Variables estáticas (para estadísticas globales)
    private static int totalEntradasVendidas = 0;
    private static int totalIngresos = 0;
    private static int totalDescuentosAplicados = 0;
    
    // Clase interna para representar una entrada
    static class Entrada {
        // Variables de instancia (información persistente)
        private int numeroEntrada;
        private String ubicacion;
        private int precioBase;
        private int descuentoAplicado;
        private int precioFinal;
        private String tipoCliente; // "Estudiante", "Tercera Edad" o "General"
        
        // Constructor
        public Entrada(int numeroEntrada, String ubicacion, int precioBase, int descuentoAplicado, String tipoCliente) {
            this.numeroEntrada = numeroEntrada;
            this.ubicacion = ubicacion;
            this.precioBase = precioBase;
            this.descuentoAplicado = descuentoAplicado;
            this.precioFinal = precioBase - descuentoAplicado;
            this.tipoCliente = tipoCliente;
        }
        
        // Método para mostrar información de la entrada
        public void mostrarInformacion() {
            System.out.println("\n------ Información de Entrada #" + numeroEntrada + " ------");
            System.out.println("Ubicación: Zona " + ubicacion);
            System.out.println("Tipo de cliente: " + tipoCliente);
            System.out.println("Precio base: $" + precioBase);
            System.out.println("Descuento aplicado: $" + descuentoAplicado);
            System.out.println("Precio final: $" + precioFinal);
            System.out.println("---------------------------------------");
        }
        
        // Getters
        public int getNumeroEntrada() { return numeroEntrada; }
        public String getUbicacion() { return ubicacion; }
        public String getTipoCliente() { return tipoCliente; }
    }

    public static void main(String[] args) {
        // Variables de instancia (almacenadas a nivel de clase)
        String nombreTeatro = "Teatro Moro";
        int capacidadSala = 500;
        int entradasDisponibles = 500;
        ArrayList<Entrada> entradasVendidas = new ArrayList<>();

        // Constantes con valores ENTEROS para precios y porcentajes
        final int PRECIO_ZONA_A = 25000;
        final int PRECIO_ZONA_B = 20000;
        final int PRECIO_ZONA_C = 15000;
        
        // Los descuentos ahora son enteros (10 para 10%, 15 para 15%)
        final int PORCENTAJE_DESCUENTO_ESTUDIANTE = 10; // Representa 10%
        final int PORCENTAJE_DESCUENTO_TERCERA_EDAD = 15; // Representa 15%
        
        // Edades siguen siendo enteros
        final int EDAD_MAX_ESTUDIANTE = 25;
        final int EDAD_MIN_TERCERA_EDAD = 65;

        Scanner scanner = new Scanner(System.in);
        boolean continuarPrograma = true;

        System.out.println("Bienvenido al sistema de venta de entradas del " + nombreTeatro);
        System.out.println("Capacidad de la sala: " + capacidadSala);
        System.out.println("Entradas disponibles: " + entradasDisponibles);

        do {
            // Variables locales para el menú
            String[] opcionesMenu = {
                "1. Venta de entradas", 
                "2. Promociones", 
                "3. Búsqueda de entradas", 
                "4. Eliminación de entradas",
                "5. Mostrar estadísticas",
                "6. Salir"
            };
            
            System.out.println("\n------ MENÚ PRINCIPAL ------");
            for (String opcionTexto : opcionesMenu) {
                System.out.println(opcionTexto);
            }
            System.out.print("Seleccione una opción (1-6): ");

            int opcionSeleccionada = 0;
            if (scanner.hasNextInt()) {
                opcionSeleccionada = scanner.nextInt();
            } else {
                System.out.println("Error: Debe ingresar un número.");
                scanner.next();
                continue;
            }
            scanner.nextLine();

            switch (opcionSeleccionada) {
                case 1: // Venta de entradas
                    if (entradasDisponibles <= 0) {
                        System.out.println("Lo sentimos, no hay entradas disponibles.");
                        break;
                    }
                    
                    System.out.println("\n------ Plano del Teatro ------");
                    System.out.println("Zona A - Precio: $" + PRECIO_ZONA_A);
                    System.out.println("Zona B - Precio: $" + PRECIO_ZONA_B);
                    System.out.println("Zona C - Precio: $" + PRECIO_ZONA_C);

                    // Variables locales para la venta
                    String ubicacion = "";
                    int precioBase = 0;
                    boolean ubicacionValida = false;

                    while (!ubicacionValida) {
                        System.out.print("Ingrese la ubicación deseada (A, B o C): ");
                        ubicacion = scanner.nextLine().trim().toUpperCase();

                        if (ubicacion.equals("A")) {
                            precioBase = PRECIO_ZONA_A;
                            ubicacionValida = true;
                        } else if (ubicacion.equals("B")) {
                            precioBase = PRECIO_ZONA_B;
                            ubicacionValida = true;
                        } else if (ubicacion.equals("C")) {
                            precioBase = PRECIO_ZONA_C;
                            ubicacionValida = true;
                        } else {
                            System.out.println("Error: Ubicación inválida. Por favor, ingrese A, B o C.");
                        }
                    }

                    // Opciones de tipo de cliente
                    System.out.println("\n------ Tipo de Cliente ------");
                    System.out.println("1. Estudiante (Descuento del 10%)");
                    System.out.println("2. Tercera Edad (Descuento del 15%)");
                    System.out.println("3. Público General (Sin descuento)");
                    System.out.print("Seleccione el tipo de cliente (1-3): ");
                    
                    int tipoCliente = 0;
                    if (scanner.hasNextInt()) {
                        tipoCliente = scanner.nextInt();
                        scanner.nextLine();
                    } else {
                        System.out.println("Error: Debe ingresar un número.");
                        scanner.next();
                        scanner.nextLine();
                        continue;
                    }

                    // Calcular descuento y precio final como enteros
                    int descuento = 0;
                    String tipoDescuento = "General";

                    if (tipoCliente == 1) {
                        descuento = (precioBase * PORCENTAJE_DESCUENTO_ESTUDIANTE) / 100;
                        tipoDescuento = "Estudiante";
                    } else if (tipoCliente == 2) {
                        descuento = (precioBase * PORCENTAJE_DESCUENTO_TERCERA_EDAD) / 100;
                        tipoDescuento = "Tercera Edad";
                    } else if (tipoCliente != 3) {
                        System.out.println("Opción inválida. Se aplicará tarifa de Público General.");
                    }

                    int precioFinal = precioBase - descuento;

                    // Registrar la entrada vendida
                    totalEntradasVendidas++;
                    int numeroEntrada = totalEntradasVendidas;
                    Entrada nuevaEntrada = new Entrada(numeroEntrada, ubicacion, precioBase, descuento, tipoDescuento);
                    entradasVendidas.add(nuevaEntrada);
                    entradasDisponibles--;
                    
                    // Actualizar estadísticas
                    totalIngresos += precioFinal;
                    totalDescuentosAplicados += descuento;

                    // Mostrar resumen de la compra
                    System.out.println("\n------ Resumen de la Compra ------");
                    System.out.println("Número de entrada: " + numeroEntrada);
                    System.out.println("Ubicación del asiento: Zona " + ubicacion);
                    System.out.println("Tipo de cliente: " + tipoDescuento);
                    System.out.println("Precio base de la entrada: $" + precioBase);
                    System.out.println("Descuento aplicado: $" + descuento);
                    System.out.println("Precio final a pagar: $" + precioFinal);
                    System.out.println("----------------------------------");
                    break;
                    
                case 2: // Promociones
                    System.out.println("\n------ PROMOCIONES DISPONIBLES ------");
                    System.out.println("1. Compra de 2 entradas: 5% de descuento adicional");
                    System.out.println("2. Compra de 3 entradas o más: 10% de descuento adicional");
                    System.out.println("3. Día de la semana (Martes y Miércoles): 20% de descuento");
                    System.out.println("4. Volver al menú principal");
                    System.out.print("Seleccione una promoción para más información (1-4): ");
                    
                    int opcionPromocion = 0;
                    if (scanner.hasNextInt()) {
                        opcionPromocion = scanner.nextInt();
                        scanner.nextLine();
                    } else {
                        System.out.println("Error: Debe ingresar un número.");
                        scanner.next();
                        scanner.nextLine();
                        break;
                    }
                    
                    switch (opcionPromocion) {
                        case 1:
                            System.out.println("\nPromoción de 2 entradas:");
                            System.out.println("Al comprar exactamente 2 entradas en una misma transacción, ");
                            System.out.println("obtienes un 5% de descuento adicional sobre el precio final.");
                            System.out.println("Esta promoción es acumulable con descuentos de estudiante o tercera edad.");
                            break;
                        case 2:
                            System.out.println("\nPromoción de 3 o más entradas:");
                            System.out.println("Al comprar 3 o más entradas en una misma transacción, ");
                            System.out.println("obtienes un 10% de descuento adicional sobre el precio final.");
                            System.out.println("Esta promoción es acumulable con descuentos de estudiante o tercera edad.");
                            break;
                        case 3:
                            System.out.println("\nPromoción de día de semana:");
                            System.out.println("Todos los martes y miércoles, obtienes un 20% de descuento ");
                            System.out.println("sobre el precio base de cualquier entrada.");
                            System.out.println("Esta promoción NO es acumulable con otros descuentos.");
                            break;
                        case 4:
                            // Volver al menú principal
                            break;
                        default:
                            System.out.println("Opción inválida. Volviendo al menú principal.");
                            break;
                    }
                    break;
                    
                case 3: // Búsqueda de entradas
                    if (entradasVendidas.isEmpty()) {
                        System.out.println("No hay entradas vendidas para buscar.");
                        break;
                    }
                    
                    System.out.println("\n------ BÚSQUEDA DE ENTRADAS ------");
                    System.out.println("1. Buscar por número de entrada");
                    System.out.println("2. Buscar por ubicación");
                    System.out.println("3. Buscar por tipo de cliente");
                    System.out.println("4. Volver al menú principal");
                    System.out.print("Seleccione una opción de búsqueda (1-4): ");
                    
                    int opcionBusqueda = 0;
                    if (scanner.hasNextInt()) {
                        opcionBusqueda = scanner.nextInt();
                        scanner.nextLine();
                    } else {
                        System.out.println("Error: Debe ingresar un número.");
                        scanner.next();
                        scanner.nextLine();
                        break;
                    }
                    
                    switch (opcionBusqueda) {
                        case 1: // Buscar por número
                            System.out.print("Ingrese el número de entrada a buscar: ");
                            if (scanner.hasNextInt()) {
                                int numeroBuscado = scanner.nextInt();
                                scanner.nextLine();
                                
                                boolean encontrada = false;
                                for (Entrada e : entradasVendidas) {
                                    if (e.getNumeroEntrada() == numeroBuscado) {
                                        e.mostrarInformacion();
                                        encontrada = true;
                                        break;
                                    }
                                }
                                
                                if (!encontrada) {
                                    System.out.println("No se encontró ninguna entrada con el número " + numeroBuscado);
                                }
                            } else {
                                System.out.println("Error: Debe ingresar un número válido.");
                                scanner.next();
                                scanner.nextLine();
                            }
                            break;
                            
                        case 2: // Buscar por ubicación
                            System.out.print("Ingrese la ubicación a buscar (A, B o C): ");
                            String ubicacionBuscada = scanner.nextLine().trim().toUpperCase();
                            
                            if (ubicacionBuscada.equals("A") || ubicacionBuscada.equals("B") || ubicacionBuscada.equals("C")) {
                                boolean encontrada = false;
                                System.out.println("\nEntradas encontradas en la Zona " + ubicacionBuscada + ":");
                                
                                for (Entrada e : entradasVendidas) {
                                    if (e.getUbicacion().equals(ubicacionBuscada)) {
                                        e.mostrarInformacion();
                                        encontrada = true;
                                    }
                                }
                                
                                if (!encontrada) {
                                    System.out.println("No se encontraron entradas en la ubicación " + ubicacionBuscada);
                                }
                            } else {
                                System.out.println("Error: Ubicación inválida. Debe ser A, B o C.");
                            }
                            break;
                            
                        case 3: // Buscar por tipo de cliente
                            System.out.println("Seleccione el tipo de cliente a buscar:");
                            System.out.println("1. Estudiante");
                            System.out.println("2. Tercera Edad");
                            System.out.println("3. General");
                            System.out.print("Opción: ");
                            
                            if (scanner.hasNextInt()) {
                                int opcionTipo = scanner.nextInt();
                                scanner.nextLine();
                                
                                String tipoBuscado = "";
                                if (opcionTipo == 1) {
                                    tipoBuscado = "Estudiante";
                                } else if (opcionTipo == 2) {
                                    tipoBuscado = "Tercera Edad";
                                } else if (opcionTipo == 3) {
                                    tipoBuscado = "General";
                                } else {
                                    System.out.println("Opción inválida.");
                                    break;
                                }
                                
                                boolean encontrada = false;
                                System.out.println("\nEntradas vendidas a clientes tipo '" + tipoBuscado + "':");
                                
                                for (Entrada e : entradasVendidas) {
                                    if (e.getTipoCliente().equals(tipoBuscado)) {
                                        e.mostrarInformacion();
                                        encontrada = true;
                                    }
                                }
                                
                                if (!encontrada) {
                                    System.out.println("No se encontraron entradas para clientes tipo '" + tipoBuscado + "'");
                                }
                            } else {
                                System.out.println("Error: Debe ingresar un número válido.");
                                scanner.next();
                                scanner.nextLine();
                            }
                            break;
                            
                        case 4:
                            // Volver al menú principal
                            break;
                            
                        default:
                            System.out.println("Opción inválida.");
                            break;
                    }
                    break;
                    
                case 4: // Eliminación de entradas
                    if (entradasVendidas.isEmpty()) {
                        System.out.println("No hay entradas vendidas para eliminar.");
                        break;
                    }
                    
                    System.out.print("Ingrese el número de entrada a eliminar: ");
                    if (scanner.hasNextInt()) {
                        int numeroEliminar = scanner.nextInt();
                        scanner.nextLine();
                        
                        boolean eliminada = false;
                        for (int i = 0; i < entradasVendidas.size(); i++) {
                            if (entradasVendidas.get(i).getNumeroEntrada() == numeroEliminar) {
                                Entrada entradaEliminada = entradasVendidas.remove(i);
                                System.out.println("La entrada #" + numeroEliminar + " ha sido eliminada correctamente.");
                                
                                // Actualizar estadísticas
                                totalIngresos -= (entradaEliminada.precioBase - entradaEliminada.descuentoAplicado);
                                totalDescuentosAplicados -= entradaEliminada.descuentoAplicado;
                                entradasDisponibles++;
                                
                                eliminada = true;
                                break;
                            }
                        }
                        
                        if (!eliminada) {
                            System.out.println("No se encontró ninguna entrada con el número " + numeroEliminar);
                        }
                    } else {
                        System.out.println("Error: Debe ingresar un número válido.");
                        scanner.next();
                        scanner.nextLine();
                    }
                    break;
                    
                case 5: // Mostrar estadísticas
                    System.out.println("\n------ ESTADÍSTICAS DEL TEATRO ------");
                    System.out.println("Nombre del teatro: " + nombreTeatro);
                    System.out.println("Capacidad total: " + capacidadSala);
                    System.out.println("Entradas vendidas: " + (capacidadSala - entradasDisponibles));
                    System.out.println("Entradas disponibles: " + entradasDisponibles);
                    System.out.println("Total de ingresos: $" + totalIngresos);
                    System.out.println("Total de descuentos aplicados: $" + totalDescuentosAplicados);
                    System.out.println("-----------------------------------");
                    break;
                    
                case 6: // Salir
                    System.out.println("\nGracias por usar el sistema de ventas del " + nombreTeatro + ". ¡Hasta pronto!");
                    continuarPrograma = false;
                    break;
                    
                default:
                    System.out.println("Error: Opción no válida. Por favor, elija entre 1 y 6.");
                    break;
            }

        } while (continuarPrograma);

        scanner.close();
    }
}