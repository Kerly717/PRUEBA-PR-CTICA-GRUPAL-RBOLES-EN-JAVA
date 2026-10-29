package uta.app;

import uta.abb.ArbolABB;
import uta.abb.Estudiante;
import uta.abb.ResultadoBusqueda;
import uta.analisis.AnalisisBusqueda;
import uta.general.ArbolGeneral;
import uta.general.ConversorHijoHermano;

import java.util.Scanner;

/** Menú de consola. Use el argumento --demo para ejecutar los casos de aceptación sin interacción. */
public class Main {
    private static final Scanner IN = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--demo")) {
            System.exit(Demo.ejecutar() == 0 ? 0 : 1);
        }
        ArbolGeneral general = DatosIniciales.jerarquiaInicial();
        ArbolABB abb = DatosIniciales.abbInicial();
        int op;
        do {
            menu();
            op = leerEntero("Opción: ", -1);
            try {
                ejecutar(op, general, abb);
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (op != 0);
        System.out.println("Hasta luego.");
    }

    private static void menu() {
        System.out.println("\n===== SISTEMA DE AUDITORÍA UTA =====");
        System.out.println(" -- Árbol general --");
        System.out.println(" 1. Imprimir jerarquía        2. Agregar unidad        3. Buscar unidad");
        System.out.println(" 4. Eliminar unidad           5. Métricas de unidad    6. Conversión hijo-hermano");
        System.out.println(" -- ABB --");
        System.out.println(" 7. Insertar matrícula        8. Buscar matrícula      9. Recorridos");
        System.out.println("10. Modificar matrícula      11. Eliminar matrícula   12. Métricas del ABB");
        System.out.println(" -- Integración --");
        System.out.println("13. Auditoría                14. Análisis de búsqueda 15. Ejecutar casos de aceptación");
        System.out.println("16. Reubicar unidad (rechaza ciclos)");
        System.out.println(" 0. Salir");
    }

    private static void ejecutar(int op, ArbolGeneral g, ArbolABB abb) {
        switch (op) {
            case 1 -> System.out.print(g.imprimirJerarquia());
            case 2 -> {
                String padre = leerTexto("Código del padre: ");
                String codigo = leerTexto("Código de la nueva unidad: ");
                ArbolGeneral.Resultado r = g.agregarUnidadDetallado(padre, codigo);
                System.out.println(r.ok() ? "Unidad agregada." : "Rechazado: " + r.mensaje + ".");
            }
            case 3 -> {
                String c = leerTexto("Código a buscar: ");
                System.out.println(g.buscar(c) != null ? "Encontrada: " + g.buscar(c).getCodigo() : "No existe.");
            }
            case 4 -> {
                String c = leerTexto("Código a eliminar: ");
                ArbolGeneral.Resultado r = g.eliminarDetallado(c);
                System.out.println(r.ok() ? "Unidad eliminada." : "Rechazado: " + r.mensaje + ".");
            }
            case 5 -> {
                String c = leerTexto("Código: ");
                if (g.buscar(c) == null) System.out.println("No existe.");
                else System.out.println("Nivel=" + g.nivel(c) + ", grado=" + g.grado(c));
                System.out.println("Nodos=" + g.contarNodos() + ", hojas=" + g.contarHojas() + ", altura=" + g.altura());
            }
            case 6 -> {
                if (g.estaVacio()) System.out.println("Árbol vacío.");
                else System.out.print(ConversorHijoHermano.describir(ConversorHijoHermano.convertir(g)));
            }
            case 7 -> {
                int m = leerEntero("Matrícula: ", Integer.MIN_VALUE);
                String n = leerTexto("Nombre: ");
                double p = leerDouble("Promedio (0-10): ");
                System.out.println(abb.insertar(new Estudiante(m, n, p)) ? "Insertado."
                        : "Rechazado: matrícula duplicada.");
            }
            case 8 -> {
                int m = leerEntero("Matrícula: ", Integer.MIN_VALUE);
                ResultadoBusqueda r = abb.buscarContando(m);
                System.out.println((r.encontrado() ? "Encontrado: " + r.estudiante() : "No existe.")
                        + " (comparaciones: " + r.comparaciones() + ")");
            }
            case 9 -> {
                System.out.println("Preorden : " + abb.preorden());
                System.out.println("Inorden  : " + abb.inorden());
                System.out.println("Posorden : " + abb.posorden());
                System.out.print(abb.imprimirEstructura());
            }
            case 10 -> {
                int m = leerEntero("Matrícula (no se puede cambiar): ", Integer.MIN_VALUE);
                String n = leerTexto("Nuevo nombre: ");
                double p = leerDouble("Nuevo promedio (0-10): ");
                System.out.println(abb.modificar(m, n, p) ? "Modificado." : "Rechazado: no existe o datos inválidos.");
            }
            case 11 -> {
                int m = leerEntero("Matrícula a eliminar: ", Integer.MIN_VALUE);
                System.out.println(abb.eliminar(m) ? "Eliminado." : "No existe.");
            }
            case 12 -> {
                System.out.println("Nodos=" + abb.contarNodos() + ", hojas=" + abb.contarHojas() + ", altura=" + abb.altura());
                System.out.println("Mínimo=" + abb.minimo() + " | Máximo=" + abb.maximo());
                System.out.println("Promedio general=" + String.format(java.util.Locale.US, "%.3f", abb.promedioGeneral()));
            }
            case 13 -> System.out.print(Auditoria.generar(g, abb));
            case 14 -> {
                int n = leerEntero("n (1-5000, sugerido 127): ", 127);
                if (n < 1 || n > 5000) System.out.println("n fuera de rango.");
                else System.out.print(AnalisisBusqueda.formatear(AnalisisBusqueda.analizar(n)));
            }
            case 15 -> Demo.ejecutar();
            case 16 -> {
                String c = leerTexto("Unidad a mover: ");
                String destino = leerTexto("Nuevo padre: ");
                System.out.println(g.reubicarUnidad(c, destino) ? "Unidad reubicada."
                        : "Rechazado: no existe, es la raíz o el destino es la unidad o un descendiente (ciclo).");
            }
            case 0 -> { }
            default -> System.out.println("Opción inválida.");
        }
    }

    private static String leerTexto(String msg) {
        System.out.print(msg);
        if (!IN.hasNextLine()) {                 // fin de la entrada (Ctrl+D / Ctrl+Z o archivo agotado)
            System.out.println("\nFin de la entrada. Hasta luego.");
            System.exit(0);                      // evita repetir el menú indefinidamente
        }
        return IN.nextLine();
    }

    private static int leerEntero(String msg, int defecto) {
        try {
            return Integer.parseInt(leerTexto(msg).trim());
        } catch (NumberFormatException e) {
            if (defecto == Integer.MIN_VALUE) throw new IllegalArgumentException("Entero inválido.");
            return defecto;
        }
    }

    private static double leerDouble(String msg) {
        try {
            return Double.parseDouble(leerTexto(msg).trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número inválido.");
        }
    }
}
