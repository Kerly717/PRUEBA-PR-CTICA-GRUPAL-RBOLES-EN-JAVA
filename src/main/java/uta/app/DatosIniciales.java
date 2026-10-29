package uta.app;

import uta.abb.ArbolABB;
import uta.abb.Estudiante;
import uta.general.ArbolGeneral;

/** Datos de entrada obligatorios del caso. */
public final class DatosIniciales {
    private DatosIniciales() {}

    /** Matrículas en el orden de inserción exigido por la guía. */
    public static final int[] MATRICULAS = {50, 30, 70, 20, 40, 60, 80, 35, 45, 65, 75, 85};
    /** Nombres y promedios de ejemplo (editables); misma posición que MATRICULAS. */
    public static final String[] NOMBRES = {
        "Ana Morales", "Luis Paredes", "Carla Vega", "Diego Salazar", "Elena Ruiz", "Fabián Torres",
        "Gabriela León", "Hugo Mena", "Irene Castro", "Jorge Núñez", "Karen Ortiz", "Mateo Silva"
    };
    public static final double[] PROMEDIOS = {8.5, 7.0, 9.0, 6.5, 7.5, 8.0, 9.5, 6.0, 8.0, 7.0, 8.5, 9.0};

    /** UTA → FISEI, FCHE, FCS; FISEI → SOFTWARE, TI; SOFTWARE → 3A, 3B; TI → 4A. */
    public static ArbolGeneral jerarquiaInicial() {
        ArbolGeneral g = new ArbolGeneral();
        g.crearRaiz("UTA");
        g.agregarUnidad("UTA", "FISEI");
        g.agregarUnidad("UTA", "FCHE");
        g.agregarUnidad("UTA", "FCS");
        g.agregarUnidad("FISEI", "SOFTWARE");
        g.agregarUnidad("FISEI", "TI");
        g.agregarUnidad("SOFTWARE", "3A");
        g.agregarUnidad("SOFTWARE", "3B");
        g.agregarUnidad("TI", "4A");
        return g;
    }

    /** ABB con las 12 matrículas en el orden exigido. */
    public static ArbolABB abbInicial() {
        ArbolABB abb = new ArbolABB();
        for (int i = 0; i < MATRICULAS.length; i++) {
            abb.insertar(new Estudiante(MATRICULAS[i], NOMBRES[i], PROMEDIOS[i]));
        }
        return abb;
    }
}
