package uta.pruebas;

import uta.abb.ArbolABB;
import uta.abb.Estudiante;
import uta.analisis.AnalisisBusqueda;
import uta.app.DatosIniciales;
import uta.general.ArbolGeneral;
import uta.general.ConversorHijoHermano;
import uta.general.NodoHH;

import java.util.Arrays;
import java.util.List;

/**
 * Ejecutable de pruebas con aserciones propias (no requiere JUnit ni la bandera -ea).
 * Termina con código 1 si alguna prueba falla.
 */
public class PruebasRunner {
    private static int total = 0, fallidas = 0;

    private interface Prueba { void ejecutar(); }

    private static void prueba(String nombre, Prueba p) {
        total++;
        try {
            p.ejecutar();
            System.out.println("[OK]    " + nombre);
        } catch (Throwable t) {
            fallidas++;
            System.out.println("[FALLA] " + nombre + " -> " + t);
        }
    }

    private static void verificar(boolean cond, String msg) {
        if (!cond) throw new AssertionError(msg);
    }

    private static void igual(Object esperado, Object actual, String msg) {
        if (!esperado.equals(actual)) throw new AssertionError(msg + ": esperado=" + esperado + " actual=" + actual);
    }

    private static List<Integer> lista(Integer... v) { return Arrays.asList(v); }

    public static void main(String[] args) {
        // ---------------- Árbol general
        prueba("01 Jerarquía inicial válida (9 nodos, 5 hojas, altura 3, grado FISEI 2, nivel 3B 3)", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            igual(9, g.contarNodos(), "nodos");
            igual(5, g.contarHojas(), "hojas");
            igual(3, g.altura(), "altura");
            igual(2, g.grado("FISEI"), "grado FISEI");
            igual(3, g.nivel("3B"), "nivel 3B");
            verificar(g.esJerarquiaValida(), "debe ser válida");
        });
        prueba("02 Código duplicado rechazado", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            verificar(!g.agregarUnidad("UTA", "TI"), "duplicado debe rechazarse");
            verificar(!g.agregarUnidad("FCS", "fisei"), "duplicado (otra capitalización) debe rechazarse");
            igual(9, g.contarNodos(), "sin cambios");
        });
        prueba("03 Padre inexistente / datos nulos o vacíos rechazados", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            verificar(!g.agregarUnidad("NOEXISTE", "NUEVA"), "padre inexistente");
            verificar(!g.agregarUnidad(null, "NUEVA"), "padre nulo");
            verificar(!g.agregarUnidad("UTA", "  "), "código vacío");
            igual(9, g.contarNodos(), "sin cambios");
        });
        prueba("04 Jerarquía inválida: ciclo rechazado al reubicar", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            verificar(!g.reubicarUnidad("FISEI", "3A"), "mover FISEI bajo su descendiente crea ciclo");
            verificar(!g.reubicarUnidad("FISEI", "FISEI"), "mover bajo sí mismo");
            verificar(!g.reubicarUnidad("UTA", "FCS"), "la raíz no se mueve");
            verificar(g.esJerarquiaValida(), "sigue válida");
            verificar(g.reubicarUnidad("4A", "SOFTWARE"), "reubicación legítima");
            igual(0, g.grado("TI"), "TI queda sin hijos");
            igual(3, g.grado("SOFTWARE"), "SOFTWARE gana un hijo");
        });
        prueba("05 Eliminar FISEI con descendencia: rechazo y jerarquía sin cambios", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            String antes = g.imprimirJerarquia();
            verificar(!g.eliminar("FISEI"), "debe rechazarse");
            igual(antes, g.imprimirJerarquia(), "jerarquía");
        });
        prueba("06 Eliminar hoja permitido; raíz e inexistente rechazados", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            verificar(g.eliminar("FCS"), "hoja se elimina");
            verificar(g.buscar("FCS") == null, "ya no existe");
            verificar(!g.eliminar("UTA"), "raíz no se elimina");
            verificar(!g.eliminar("XYZ"), "inexistente");
            igual(8, g.contarNodos(), "nodos");
        });
        prueba("07 Árbol general vacío: sin excepciones y métricas coherentes", () -> {
            ArbolGeneral g = new ArbolGeneral();
            igual(-1, g.altura(), "altura vacía");
            igual(0, g.contarNodos(), "nodos");
            igual(0, g.contarHojas(), "hojas");
            verificar(g.buscar("A") == null, "buscar");
            igual("", g.imprimirJerarquia(), "impresión");
            verificar(!g.agregarUnidad("A", "B"), "no hay padre");
            verificar(!g.eliminar("A"), "eliminar en vacío");
            verificar(ConversorHijoHermano.convertir(g) == null, "conversión de vacío");
            verificar(g.esJerarquiaValida(), "vacío es válido");
        });
        prueba("08 Raíz única (singleton): altura 0, 1 nodo, 1 hoja", () -> {
            ArbolGeneral g = new ArbolGeneral();
            verificar(g.crearRaiz("UNICA"), "crear raíz");
            verificar(!g.crearRaiz("OTRA"), "segunda raíz rechazada");
            igual(0, g.altura(), "altura");
            igual(1, g.contarNodos(), "nodos");
            igual(1, g.contarHojas(), "hojas");
            igual(0, g.nivel("UNICA"), "nivel raíz");
            igual(0, g.grado("UNICA"), "grado");
        });
        prueba("09 Transformación hijo-izquierdo / hermano-derecho y reconstrucción", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            NodoHH raiz = ConversorHijoHermano.convertir(g);
            igual("UTA", raiz.codigo, "raíz");
            verificar(raiz.hermanoDerecho == null, "la raíz no tiene hermano");
            igual("FISEI", raiz.primerHijo.codigo, "primer hijo de UTA");
            igual("FCHE", raiz.primerHijo.hermanoDerecho.codigo, "hermano de FISEI");
            igual("FCS", raiz.primerHijo.hermanoDerecho.hermanoDerecho.codigo, "hermano de FCHE");
            ArbolGeneral r = ConversorHijoHermano.reconstruir(raiz);
            igual(g.preorden(), r.preorden(), "equivalencia estructural");
            igual(g.imprimirJerarquia(), r.imprimirJerarquia(), "jerarquía idéntica");
            verificar(ConversorHijoHermano.alturaBinaria(raiz) != g.altura(), "alturas distintas");
        });
        // ---------------- ABB
        prueba("10 ABB tras 12 inserciones: inorden esperado, invariante válido", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            igual(lista(20, 30, 35, 40, 45, 50, 60, 65, 70, 75, 80, 85), a.inorden(), "inorden");
            igual(lista(50, 30, 20, 40, 35, 45, 70, 60, 65, 80, 75, 85), a.preorden(), "preorden");
            igual(lista(20, 35, 45, 40, 30, 65, 60, 75, 85, 80, 70, 50), a.posorden(), "posorden");
            verificar(a.esABBValido(), "invariante");
        });
        prueba("11 Insertar duplicado 40: rechazado y conteo preservado (12)", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            verificar(!a.insertar(new Estudiante(40, "Otro", 5)), "duplicado");
            verificar(!a.insertar(null), "nulo");
            igual(12, a.contarNodos(), "conteo");
        });
        prueba("12 Modificar 35 sin cambiar matrícula; datos inválidos rechazados", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            List<Integer> antes = a.inorden();
            verificar(a.modificar(35, "Nuevo Nombre", 9.9), "modificación válida");
            igual("Nuevo Nombre", a.buscar(35).getNombre(), "nombre");
            igual(9.9, a.buscar(35).getPromedio(), "promedio");
            igual(antes, a.inorden(), "la clave no cambia");
            verificar(!a.modificar(35, "X", 11), "promedio > 10");
            verificar(!a.modificar(35, "X", -0.1), "promedio < 0");
            verificar(!a.modificar(35, " ", 5), "nombre vacío");
            verificar(!a.modificar(999, "X", 5), "matrícula inexistente");
            igual("Nuevo Nombre", a.buscar(35).getNombre(), "sin cambios tras rechazos");
        });
        prueba("13 Eliminar 20, 60 y 70: inorden esperado y 9 nodos", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            List<Integer> antes = a.inorden();
            verificar(a.eliminar(20), "hoja");
            verificar(a.eliminar(60), "un hijo");
            verificar(a.eliminar(70), "dos hijos");
            igual(lista(30, 35, 40, 45, 50, 65, 75, 80, 85), a.inorden(), "inorden después");
            igual(9, a.contarNodos(), "nodos");
            verificar(a.esABBValido(), "invariante");
            verificar(!antes.equals(a.inorden()), "inorden cambió respecto de antes");
        });
        prueba("14 Eliminación con dos hijos usa el sucesor inorden (70 -> 75)", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            a.eliminar(70);
            verificar(a.buscar(70) == null, "70 ya no existe");
            igual(lista(50, 30, 20, 40, 35, 45, 75, 60, 65, 80, 85), a.preorden(), "preorden tras eliminar 70");
            verificar(!a.eliminar(70), "segunda eliminación falla");
            verificar(a.eliminar(50), "eliminar la raíz con dos hijos");
            verificar(a.esABBValido(), "invariante");
        });
        prueba("15 ABB vacío: sin excepciones y métricas coherentes", () -> {
            ArbolABB a = new ArbolABB();
            igual(-1, a.altura(), "altura");
            igual(0, a.contarNodos(), "nodos");
            igual(0, a.contarHojas(), "hojas");
            verificar(a.minimo() == null && a.maximo() == null, "min/max");
            igual(0.0, a.promedioGeneral(), "promedio");
            verificar(a.buscar(1) == null, "buscar");
            verificar(!a.eliminar(1), "eliminar");
            verificar(a.inorden().isEmpty() && a.preorden().isEmpty() && a.posorden().isEmpty(), "recorridos");
            verificar(a.esABBValido(), "invariante en vacío");
        });
        prueba("16 ABB con un solo nodo: altura 0, hoja, min = max", () -> {
            ArbolABB a = new ArbolABB();
            a.insertar(new Estudiante(7, "Solo", 8));
            igual(0, a.altura(), "altura");
            igual(1, a.contarHojas(), "hojas");
            verificar(a.minimo() == a.maximo(), "min = max");
            verificar(a.eliminar(7) && a.estaVacio(), "eliminar la única matrícula");
        });
        prueba("17 Consistencia del promedio antes y después de eliminar", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            igual(true, Math.abs(a.promedioGeneral() - 94.5 / 12) < 1e-9, "promedio inicial = 7.875");
            a.eliminar(20); a.eliminar(60); a.eliminar(70);   // 6.5 + 8.0 + 9.0 = 23.5
            igual(true, Math.abs(a.promedioGeneral() - (94.5 - 23.5) / 9) < 1e-9, "promedio tras eliminar");
            double suma = 0;
            for (Estudiante e : a.inordenEstudiantes()) suma += e.getPromedio();
            igual(true, Math.abs(a.promedioGeneral() - suma / a.contarNodos()) < 1e-9, "coincide con suma manual");
        });
        prueba("18 Mínimo, máximo, altura y hojas del ABB inicial", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            igual(20, a.minimo().getMatricula(), "mínimo");
            igual(85, a.maximo().getMatricula(), "máximo");
            igual(3, a.altura(), "altura");
            igual(6, a.contarHojas(), "hojas (20, 35, 45, 65, 75, 85)");
        });
        prueba("19 Sin datos codificados: otras matrículas (el docente puede cambiarlas en vivo)", () -> {
            ArbolABB a = new ArbolABB();
            int[] claves = {15, 8, 22, 3, 11, 19, 30};
            for (int k : claves) verificar(a.insertar(new Estudiante(k, "E" + k, k % 11)), "insertar " + k);
            igual(lista(3, 8, 11, 15, 19, 22, 30), a.inorden(), "inorden");
            verificar(a.eliminar(15) && a.esABBValido(), "eliminar raíz");
            igual(19, a.preorden().get(0), "nueva raíz = sucesor inorden");
        });
        prueba("20 Estudiante inválido lanza IllegalArgumentException", () -> {
            boolean lanzo = false;
            try { new Estudiante(1, "X", 10.5); } catch (IllegalArgumentException e) { lanzo = true; }
            verificar(lanzo, "promedio fuera de rango");
            lanzo = false;
            try { new Estudiante(1, null, 5); } catch (IllegalArgumentException e) { lanzo = true; }
            verificar(lanzo, "nombre nulo");
        });
        // ---------------- Análisis
        prueba("21 Búsqueda: cadena O(n) vs balanceado O(log n)", () -> {
            AnalisisBusqueda.Reporte r = AnalisisBusqueda.analizar(127);
            igual(126, r.alturaCadena(), "altura cadena");
            igual(6, r.alturaBalanceado(), "altura balanceado");
            igual(127, r.existenteCadena(), "comparaciones existente en cadena");
            igual(127, r.inexistenteCadena(), "comparaciones inexistente en cadena");
            verificar(r.existenteBalanceado() <= 7 && r.inexistenteBalanceado() <= 7, "balanceado <= h+1");
            verificar(r.promedioCadena() > 10 * r.promedioBalanceado(), "diferencia sustancial");
            ArbolABB cadena = AnalisisBusqueda.construir(AnalisisBusqueda.ordenAscendente(127));
            igual(127, cadena.contarNodos(), "mismo tamaño");
            igual(127, AnalisisBusqueda.construir(AnalisisBusqueda.ordenBalanceado(127)).contarNodos(), "mismo tamaño");
        });

        // ---------------- Casos adicionales (auditoría)
        prueba("22 Causa del rechazo al agregar y eliminar unidades", () -> {
            ArbolGeneral vacio = new ArbolGeneral();
            igual(ArbolGeneral.Resultado.ARBOL_VACIO, vacio.agregarUnidadDetallado("UTA", "X"), "árbol vacío");
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            igual(ArbolGeneral.Resultado.PADRE_INEXISTENTE, g.agregarUnidadDetallado("NOEXISTE", "X"), "padre");
            igual(ArbolGeneral.Resultado.CODIGO_DUPLICADO, g.agregarUnidadDetallado("FCS", "TI"), "duplicado");
            igual(ArbolGeneral.Resultado.CODIGO_VACIO, g.agregarUnidadDetallado("UTA", " "), "vacío");
            igual(ArbolGeneral.Resultado.TIENE_HIJOS, g.eliminarDetallado("FISEI"), "con hijos");
            igual(ArbolGeneral.Resultado.RAIZ_PROTEGIDA, g.eliminarDetallado("uta"), "raíz");
            igual(ArbolGeneral.Resultado.NO_EXISTE, g.eliminarDetallado("XYZ"), "inexistente");
            igual(ArbolGeneral.Resultado.OK, g.eliminarDetallado("4a"), "hoja en minúsculas");
            igual(8, g.contarNodos(), "solo se eliminó 4A");
        });
        prueba("23 Unicidad sin distinguir mayúsculas ni espacios", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            verificar(!g.agregarUnidad(" uta ", " FISEI "), "espacios");
            verificar(!g.agregarUnidad("uta", "Software"), "mayúsculas mezcladas");
            verificar(g.buscar("  software ") != null, "búsqueda normalizada");
            verificar(g.agregarUnidad("ti", "4b") && g.nivel("4B") == 3, "alta válida normalizada");
        });
        prueba("24 Jerarquía impresa con sangría exacta", () -> igual(
            "UTA\n  FISEI\n    SOFTWARE\n      3A\n      3B\n    TI\n      4A\n  FCHE\n  FCS\n",
            DatosIniciales.jerarquiaInicial().imprimirJerarquia(), "jerarquía"));
        prueba("25 Conversión completa: SOFTWARE -> 3A -> 3B, hermano TI -> 4A", () -> {
            NodoHH sw = ConversorHijoHermano.convertir(DatosIniciales.jerarquiaInicial()).primerHijo.primerHijo;
            igual("SOFTWARE", sw.codigo, "primer hijo de FISEI");
            igual("3A", sw.primerHijo.codigo, "primer hijo de SOFTWARE");
            igual("3B", sw.primerHijo.hermanoDerecho.codigo, "hermano de 3A");
            igual("TI", sw.hermanoDerecho.codigo, "hermano de SOFTWARE");
            igual("4A", sw.hermanoDerecho.primerHijo.codigo, "hijo de TI");
            verificar(sw.primerHijo.hermanoDerecho.hermanoDerecho == null, "3B es el último hermano");
        });
        prueba("26 Forma exacta del ABB tras eliminar 20, 60 y 70 (preorden en cada paso)", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            a.eliminar(20);
            igual(lista(50, 30, 40, 35, 45, 70, 60, 65, 80, 75, 85), a.preorden(), "tras 20 (hoja)");
            a.eliminar(60);
            igual(lista(50, 30, 40, 35, 45, 70, 65, 80, 75, 85), a.preorden(), "tras 60 (65 sube)");
            a.eliminar(70);
            igual(lista(50, 30, 40, 35, 45, 75, 65, 80, 85), a.preorden(), "tras 70 (sucesor 75)");
            igual(30, a.minimo().getMatricula(), "mínimo final");
            igual(85, a.maximo().getMatricula(), "máximo final");
            igual(4, a.contarHojas(), "hojas finales (35, 45, 65, 85)");
            igual(3, a.altura(), "altura final");
        });
        prueba("27 Promedio tras modificar 35 y matrícula no positiva rechazada", () -> {
            ArbolABB a = DatosIniciales.abbInicial();
            verificar(a.modificar(35, "Hugo Mena Paz", 9.0), "modificar");
            verificar(Math.abs(a.promedioGeneral() - (94.5 - 6.0 + 9.0) / 12) < 1e-9, "promedio recalculado");
            boolean lanzo = false;
            try { new Estudiante(0, "X", 5); } catch (IllegalArgumentException e) { lanzo = true; }
            verificar(lanzo, "matrícula 0");
            lanzo = false;
            try { new Estudiante(-5, "X", 5); } catch (IllegalArgumentException e) { lanzo = true; }
            verificar(lanzo, "matrícula negativa");
        });
        prueba("28 Búsqueda con 15 claves: cadena 15 comparaciones, equilibrado 4", () -> {
            ArbolABB cadena = AnalisisBusqueda.construir(AnalisisBusqueda.ordenAscendente(15));
            ArbolABB eq = AnalisisBusqueda.construir(AnalisisBusqueda.ordenBalanceado(15));
            igual(14, cadena.altura(), "altura cadena");
            igual(3, eq.altura(), "altura equilibrado");
            igual(15, cadena.buscarContando(15).comparaciones(), "cadena existente");
            igual(15, cadena.buscarContando(16).comparaciones(), "cadena inexistente");
            igual(4, eq.buscarContando(15).comparaciones(), "equilibrado existente");
            igual(4, eq.buscarContando(16).comparaciones(), "equilibrado inexistente");
            igual(1, eq.buscarContando(8).comparaciones(), "clave en la raíz");
        });
        prueba("29 Orden de inserción por punto medio y ascendente (arreglos)", () -> {
            int[] b = AnalisisBusqueda.ordenBalanceado(7);
            int[] c = AnalisisBusqueda.ordenAscendente(5);
            verificar(Arrays.equals(new int[]{4, 2, 1, 3, 6, 5, 7}, b), "punto medio: " + Arrays.toString(b));
            verificar(Arrays.equals(new int[]{1, 2, 3, 4, 5}, c), "ascendente: " + Arrays.toString(c));
        });
        prueba("30 Validación de jerarquía con más de 16 unidades y tras reubicar", () -> {
            ArbolGeneral g = DatosIniciales.jerarquiaInicial();
            for (int i = 1; i <= 30; i++) verificar(g.agregarUnidad("FCHE", "C" + i), "agregar C" + i);
            igual(39, g.contarNodos(), "nodos");
            verificar(g.esJerarquiaValida(), "válida con 39 unidades");
            verificar(g.reubicarUnidad("TI", "FCS") && g.esJerarquiaValida(), "válida tras reubicar TI");
            igual(1, g.grado("FISEI"), "FISEI pierde a TI");
            igual(1, g.grado("FCS"), "FCS gana a TI");
            igual(3, g.nivel("4A"), "4A conserva su nivel (UTA > FCS > TI > 4A)");
        });

        System.out.println("\nPruebas ejecutadas: " + total + " | exitosas: " + (total - fallidas) + " | fallidas: " + fallidas);
        System.exit(fallidas == 0 ? 0 : 1);
    }
}
