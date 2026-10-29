package uta.abb;

/** Registro de matrícula: clave inmutable (matrícula), nombre y promedio modificables. */
public class Estudiante {
    private final int matricula;
    private String nombre;
    private double promedio;

    public Estudiante(int matricula, String nombre, double promedio) {
        if (matricula <= 0) throw new IllegalArgumentException("La matrícula debe ser un entero positivo.");
        validar(nombre, promedio);
        this.matricula = matricula;
        this.nombre = nombre.trim();
        this.promedio = promedio;
    }

    static void validar(String nombre, double promedio) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (Double.isNaN(promedio) || promedio < 0.0 || promedio > 10.0) {
            throw new IllegalArgumentException("El promedio debe estar entre 0 y 10.");
        }
    }

    public int getMatricula() { return matricula; }
    public String getNombre() { return nombre; }
    public double getPromedio() { return promedio; }

    void actualizar(String nombre, double promedio) {
        validar(nombre, promedio);
        this.nombre = nombre.trim();
        this.promedio = promedio;
    }

    @Override
    public String toString() {
        return matricula + " - " + nombre + " (" + String.format(java.util.Locale.US, "%.2f", promedio) + ")";
    }
}
