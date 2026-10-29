package uta.app;

import uta.abb.ArbolABB;
import uta.abb.Estudiante;
import uta.general.ArbolGeneral;

import java.util.Locale;

/** Reporte integrado de auditoría sobre ambos árboles. */
public final class Auditoria {
    private Auditoria() {}

    public static String generar(ArbolGeneral general, ArbolABB abb) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== AUDITORÍA JERÁRQUICA Y ACADÉMICA UTA =====\n");
        sb.append("-- Jerarquía académica (árbol general) --\n");
        sb.append("Total de unidades : ").append(general.contarNodos()).append('\n');
        sb.append("Unidades hoja     : ").append(general.contarHojas()).append('\n');
        sb.append("Altura (aristas)  : ").append(general.altura()).append('\n');
        sb.append("Jerarquía válida  : ").append(general.esJerarquiaValida() ? "SÍ" : "NO").append('\n');
        sb.append("-- Matrícula (ABB) --\n");
        Estudiante min = abb.minimo();
        Estudiante max = abb.maximo();
        sb.append("Total estudiantes : ").append(abb.contarNodos()).append('\n');
        sb.append("Matrícula menor   : ").append(min == null ? "(vacío)" : min.toString()).append('\n');
        sb.append("Matrícula mayor   : ").append(max == null ? "(vacío)" : max.toString()).append('\n');
        sb.append("Promedio general  : ")
          .append(abb.estaVacio() ? "(sin datos)" : String.format(Locale.US, "%.3f", abb.promedioGeneral())).append('\n');
        sb.append("Altura ABB        : ").append(abb.altura()).append('\n');
        sb.append("Invariante ABB    : ").append(abb.esABBValido() ? "VÁLIDO" : "VIOLADO").append('\n');
        return sb.toString();
    }
}
