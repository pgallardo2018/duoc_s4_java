/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package teatromoroventas;
import java.util.Scanner; // Necesitamos esto para leer lo que escribe el usuario

public class TeatroMoroVentas {

    public static void main(String[] args) {

        // Constantes con valores ENTEROS para precios y porcentajes
        final int PRECIO_ZONA_A = 25000; // Antes double
        final int PRECIO_ZONA_B = 20000; // Antes double
        final int PRECIO_ZONA_C = 15000; // Antes double
        // Los descuentos ahora son enteros (10 para 10%, 15 para 15%)
        final int PORCENTAJE_DESCUENTO_ESTUDIANTE = 10; // Representa 10%
        final int PORCENTAJE_DESCUENTO_TERCERA_EDAD = 15; // Representa 15%
        // Edades siguen siendo enteros
        final int EDAD_MAX_ESTUDIANTE = 25;
        final int EDAD_MIN_TERCERA_EDAD = 65;

        Scanner scanner = new Scanner(System.in);
        boolean continuarPrograma = true;

        System.out.println("Bienvenido al sistema de venta de entradas del Teatro Moro (Versión Enteros)");

        do {
            String[] opcionesMenu = {"1. Comprar entrada", "2. Salir"};
            System.out.println("\n------ MENÚ PRINCIPAL ------");
            for (String opcionTexto : opcionesMenu) {
                System.out.println(opcionTexto);
            }
            System.out.print("Seleccione una opción (1 o 2): ");

            int opcionSeleccionada = 0;
            if (scanner.hasNextInt()) {
                opcionSeleccionada = scanner.nextInt();
            } else {
                System.out.println("Error: Debe ingresar un número.");
                scanner.next();
                continue;
            }
            scanner.nextLine();

            if (opcionSeleccionada == 1) {
                System.out.println("\n------ Plano del Teatro ------");
                // Mostramos los precios (ahora son int)
                System.out.println("Zona A - Precio: $" + PRECIO_ZONA_A);
                System.out.println("Zona B - Precio: $" + PRECIO_ZONA_B);
                System.out.println("Zona C - Precio: $" + PRECIO_ZONA_C);

                String ubicacion = "";
                int precioBase = 0; // Ahora es int
                boolean ubicacionValida = false;

                while (!ubicacionValida) {
                    System.out.print("Ingrese la ubicación deseada (A, B o C): ");
                    ubicacion = scanner.nextLine().trim().toUpperCase();

                    if (ubicacion.equals("A")) {
                        precioBase = PRECIO_ZONA_A; // Asigna int
                        ubicacionValida = true;
                    } else if (ubicacion.equals("B")) {
                        precioBase = PRECIO_ZONA_B; // Asigna int
                        ubicacionValida = true;
                    } else if (ubicacion.equals("C")) {
                        precioBase = PRECIO_ZONA_C; // Asigna int
                        ubicacionValida = true;
                    } else {
                        System.out.println("Error: Ubicación inválida. Por favor, ingrese A, B o C.");
                    }
                }

                int edad = -1;
                boolean edadValida = false;

                while(!edadValida) {
                    System.out.print("Ingrese su edad: ");
                    if (scanner.hasNextInt()) {
                        edad = scanner.nextInt();
                        scanner.nextLine();
                        if (edad >= 0) {
                            edadValida = true;
                        } else {
                            System.out.println("Error: La edad no puede ser negativa.");
                        }
                    } else {
                        System.out.println("Error: Por favor, ingrese un número válido para la edad.");
                        scanner.nextLine();
                    }
                }

                // Calcular descuento y precio final como enteros
                int descuento = 0; // Ahora es int
                String tipoDescuento = "Ninguno";

                if (edad >= 0 && edad <= EDAD_MAX_ESTUDIANTE) {
                    // Cálculo de descuento con enteros: (base * porcentaje) / 100
                    // La división entera truncará los decimales. Ej: (15000 * 10) / 100 = 150000 / 100 = 1500
                    descuento = (precioBase * PORCENTAJE_DESCUENTO_ESTUDIANTE) / 100;
                    tipoDescuento = "Estudiante (" + PORCENTAJE_DESCUENTO_ESTUDIANTE + "%)"; // Mostramos el %
                } else if (edad >= EDAD_MIN_TERCERA_EDAD) {
                    // Cálculo de descuento con enteros
                    descuento = (precioBase * PORCENTAJE_DESCUENTO_TERCERA_EDAD) / 100;
                    tipoDescuento = "Tercera Edad (" + PORCENTAJE_DESCUENTO_TERCERA_EDAD + "%)"; // Mostramos el %
                }

                int precioFinal = precioBase - descuento; // Resta de enteros

                // Mostrar resumen de la compra (con valores enteros)
                System.out.println("\n------ Resumen de la Compra ------");
                System.out.println("Ubicación del asiento: Zona " + ubicacion);
                System.out.println("Precio base de la entrada: $" + precioBase); // Muestra int
                System.out.println("Descuento aplicado: $" + descuento + " (" + tipoDescuento + ")"); // Muestra int
                System.out.println("Precio final a pagar: $" + precioFinal); // Muestra int
                System.out.println("----------------------------------");

                System.out.print("\n¿Desea realizar otra compra? (escriba 's' para sí, cualquier otra cosa para no): ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s")) {
                    continuarPrograma = false;
                    System.out.println("\nGracias por usar el sistema. ¡Hasta pronto!");
                }

            } else if (opcionSeleccionada == 2) {
                System.out.println("\nGracias por usar el sistema. ¡Hasta pronto!");
                continuarPrograma = false;
            } else {
                System.out.println("Error: Opción no válida. Por favor, elija 1 o 2.");
            }

        } while (continuarPrograma);

        scanner.close();
    }
}