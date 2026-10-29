# Cambios tras la auditoría

Base: la versión con paquetes `uta.*`. Se incorporaron las mejoras de la otra versión revisada y se corrigieron las fallas encontradas. Cada bloque puede ir en una rama `feature` con su propia pull request.

| # | Cambio | Archivos | Motivo |
|---|---|---|---|
| 1 | El menú terminaba en bucle infinito al agotarse la entrada; ahora sale limpio | `app/Main.java` | Falla comprobada (el programa no terminaba) |
| 2 | `esJerarquiaValida` ya no usa `ArrayList`: arreglo propio de códigos vistos, recursivo; termina aunque exista un ciclo | `general/ArbolGeneral.java` | Condición 1 de la guía: `ArrayList` solo para hijos y recorridos |
| 3 | `AnalisisBusqueda` genera los órdenes de inserción en `int[]` (el punto medio sigue siendo recursivo) | `analisis/AnalisisBusqueda.java` | Misma condición |
| 4 | `agregarUnidadDetallado` / `eliminarDetallado` con `Resultado` que explica la causa; el menú la muestra | `general/ArbolGeneral.java`, `app/Main.java` | Evidencia de jerarquía inválida y mensajes claros en la defensa |
| 5 | Opción 16 del menú: reubicar unidad (rechaza ciclos) | `app/Main.java` | Permite demostrar en vivo la regla "sin ciclos" |
| 6 | La matrícula debe ser positiva | `abb/Estudiante.java` | Validación de datos de entrada |
| 7 | Datos iniciales en arreglos tipados en lugar de `Object[][]` con conversiones | `app/DatosIniciales.java` | Legibilidad y seguridad de tipos |
| 8 | Demo: causa del rechazo de FISEI, duplicado en minúsculas, ciclo al reubicar y preorden tras cada eliminación | `app/Demo.java` | Salidas trazables |
| 9 | 9 pruebas nuevas (22–30): causas de rechazo, mayúsculas/espacios, sangría exacta, conversión completa, forma exacta tras cada eliminación, promedio tras modificar, matrícula no positiva, 15 claves (15 vs 4 comparaciones), órdenes de inserción, validación con más de 16 unidades | `PruebasRunner.java` | Aserciones exactas que detectan errores de forma |
| 10 | README, diagramas del ABB inicial y final, ruta de pruebas y espacio para capturas | `README.md`, `docs/` | Documentación exigida |
