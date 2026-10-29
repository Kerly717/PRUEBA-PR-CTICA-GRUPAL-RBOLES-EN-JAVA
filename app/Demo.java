package uta.app;

import uta.abb.ArbolABB;
import uta.analisis.AnalisisBusqueda;
import uta.general.ArbolGeneral;
import uta.general.ConversorHijoHermano;
import uta.general.NodoHH;

import java.util.Arrays;
import java.util.List;

/** Ejecuta de forma no interactiva los casos de aceptación y muestra salidas trazables. */
public final class Demo {
    private Demo() {}

    private static int fallos = 0;

    private static void verificar(String caso, boolean ok, String detalle) {
        if (!ok) fallos++;
        System.out.println((ok ? "[CUMPLE] " : "[FALLA ] ") + caso + " -> " + detalle);
    }

    public static int ejecutar() {
        fallos = 0;
        ArbolGeneral g = DatosIniciales.jerarquiaInicial();
        System.out.println("=== Árbol general ===");
        System.out.print(g.imprimirJerarquia());
        verificar("Jerarquía inicial",
                g.contarNodos() == 9 && g.contarHojas() == 5 && g.altura() == 3
                        && g.grado("FISEI") == 2 && g.nivel("3B") == 3,
                "nodos=" + g.contarNodos() + ", hojas=" + g.contarHojas() + ", altura=" + g.altura()
                        + ", grado(FISEI)=" + g.grado("FISEI") + ", nivel(3B)=" + g.nivel("3B"));

        String antes = g.imprimirJerarquia();
        boolean rechazo = !g.eliminar("FISEI");
        verificar("Eliminar FISEI con descendencia", rechazo && antes.equals(g.imprimirJerarquia()),
                "operación rechazada, jerarquía sin cambios, causa: " + g.eliminarDetallado("FISEI"));
        verificar("Duplicado sin distinguir mayúsculas",
                g.agregarUnidadDetallado("UTA", " fisei ") == ArbolGeneral.Resultado.CODIGO_DUPLICADO,
                "agregar ' fisei ' -> " + g.agregarUnidadDetallado("UTA", " fisei "));
        verificar("Reubicación que crearía un ciclo", !g.reubicarUnidad("FISEI", "3A") && g.esJerarquiaValida(),
                "mover FISEI bajo 3A rechazado; jerarquía válida");

        NodoHH hh = ConversorHijoHermano.convertir(g);
        verificar("Transformación general-binario",
                hh.primerHijo.codigo.equals("FISEI")
                        && hh.primerHijo.hermanoDerecho.codigo.equals("FCHE")
                        && hh.primerHijo.hermanoDerecho.hermanoDerecho.codigo.equals("FCS"),
                "primer hijo de UTA=" + hh.primerHijo.codigo + ", hermano de FISEI="
                        + hh.primerHijo.hermanoDerecho.codigo + ", hermano de FCHE="
                        + hh.primerHijo.hermanoDerecho.hermanoDerecho.codigo);
        System.out.print(ConversorHijoHermano.describir(hh));
        System.out.println("Altura general = " + g.altura() + " | altura de la forma binaria = "
                + ConversorHijoHermano.alturaBinaria(hh) + " (no son iguales: no confundir)");

        System.out.println("\n=== ABB ===");
        ArbolABB abb = DatosIniciales.abbInicial();
        List<Integer> esperado12 = Arrays.asList(20, 30, 35, 40, 45, 50, 60, 65, 70, 75, 80, 85);
        verificar("ABB tras 12 inserciones", abb.inorden().equals(esperado12), "inorden=" + abb.inorden());
        System.out.println("Preorden : " + abb.preorden());
        System.out.println("Posorden : " + abb.posorden());
        System.out.print(abb.imprimirEstructura());

        boolean dup = !abb.insertar(new uta.abb.Estudiante(40, "Duplicado", 5.0));
        verificar("Insertar de nuevo 40", dup && abb.contarNodos() == 12,
                "rechazada=" + dup + ", conteo=" + abb.contarNodos());

        boolean mod = abb.modificar(35, "Hugo Mena Paz", 6.8);
        verificar("Modificar 35 sin cambiar clave", mod && abb.buscar(35).getNombre().equals("Hugo Mena Paz")
                        && abb.inorden().equals(esperado12) && abb.esABBValido(),
                abb.buscar(35).toString());

        boolean e = true;
        for (int clave : new int[]{20, 60, 70}) {               // hoja, un hijo, dos hijos
            e &= abb.eliminar(clave);
            System.out.println("Eliminar " + clave + " -> preorden " + abb.preorden());
        }
        List<Integer> esperado9 = Arrays.asList(30, 35, 40, 45, 50, 65, 75, 80, 85);
        verificar("Eliminar 20, 60 y 70", e && abb.inorden().equals(esperado9) && abb.contarNodos() == 9
                        && abb.esABBValido(),
                "inorden=" + abb.inorden() + ", nodos=" + abb.contarNodos());

        ArbolGeneral vacio = new ArbolGeneral();
        ArbolABB abbVacio = new ArbolABB();
        ArbolGeneral uno = new ArbolGeneral();
        uno.crearRaiz("UNICA");
        ArbolABB abbUno = new ArbolABB();
        abbUno.insertar(new uta.abb.Estudiante(1, "Solo", 10));
        verificar("Árboles vacíos y un nodo",
                vacio.altura() == -1 && abbVacio.altura() == -1 && uno.altura() == 0 && abbUno.altura() == 0
                        && abbVacio.minimo() == null && vacio.buscar("X") == null,
                "altura vacía=" + vacio.altura() + "/" + abbVacio.altura() + ", singleton=" + uno.altura() + "/" + abbUno.altura());

        System.out.println("\n" + Auditoria.generar(g, abb));
        System.out.println(AnalisisBusqueda.formatear(AnalisisBusqueda.analizar(127)));
        System.out.println(fallos == 0 ? "RESULTADO: todos los casos de aceptación CUMPLEN."
                : "RESULTADO: " + fallos + " caso(s) fallaron.");
        return fallos;
    }
}
