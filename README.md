# Sistema de Auditoría Jerárquica y Académica UTA

UTA · FISEI · Ingeniería de Software · Estructura de Datos — Prueba Práctica Grupal: Árboles en Java.

Integra un **árbol general** (jerarquía académica) y un **ABB** (matrícula de estudiantes) con menú de consola,
auditoría, análisis de búsqueda y pruebas automatizadas. Java 17+, sin `TreeMap`/`TreeSet` ni librerías externas;
`ArrayList` solo para los hijos del árbol general y los resultados de recorridos.

## Requisitos
- JDK 17 o superior (`javac -version`)
- Visual Studio Code con *Extension Pack for Java* (opcional)

## Cómo ejecutar
| Acción | Linux / macOS | Windows |
|---|---|---|
| Compilar | `scripts/compilar.sh` | `scripts\compilar.bat` |
| Pruebas | `scripts/probar.sh` | `scripts\probar.bat` |
| Menú interactivo | `scripts/ejecutar.sh` | `scripts\ejecutar.bat` |
| Casos de aceptación | `scripts/ejecutar.sh --demo` | `scripts\ejecutar.bat --demo` |

Las salidas de referencia están en `docs/salida_pruebas.txt` (30/30) y `docs/salida_demo.txt`. Los cambios posteriores a la auditoría están en [docs/CAMBIOS.md](docs/CAMBIOS.md).

**Capturas:** agregar en `docs/capturas/` las capturas de `probar` y `ejecutar --demo` tomadas en el equipo de un integrante.

## Estructura
```
src/main/java/uta/
  general/   NodoGeneral, ArbolGeneral, NodoHH, ConversorHijoHermano
  abb/       Estudiante, NodoABB, ArbolABB, ResultadoBusqueda
  analisis/  AnalisisBusqueda (cadena vs balanceado)
  app/       Main (menú), Auditoria, DatosIniciales, Demo
src/test/java/uta/pruebas/PruebasRunner.java   (30 pruebas con aserciones)
docs/        diagramas.md, GITHUB.md, salidas de ejecución
scripts/     compilar / probar / ejecutar (.sh y .bat)
```
Diagramas: ver [docs/diagramas.md](docs/diagramas.md).

## Convenciones
- **Altura en aristas**: árbol vacío = -1, un solo nodo = 0. **Nivel** de la raíz = 0.
- Códigos de unidad normalizados (`trim` + mayúsculas): `fisei`, ` FISEI ` y `FISEI` son la misma unidad.
- `agregarUnidadDetallado` y `eliminarDetallado` devuelven `ArbolGeneral.Resultado` con la causa del rechazo (`PADRE_INEXISTENTE`, `CODIGO_DUPLICADO`, `TIENE_HIJOS`, `RAIZ_PROTEGIDA`, `NO_EXISTE`, `ARBOL_VACIO`, `CODIGO_VACIO`); el menú la muestra.
- Matrícula: entero positivo; promedio entre 0 y 10.
- `ArrayList` solo en los hijos de `NodoGeneral` y en los resultados de recorridos (preorden, inorden, posorden). La validación de la jerarquía usa un arreglo propio y el análisis de búsqueda usa `int[]`.
- **Comparaciones** en la búsqueda del ABB = nodos cuya clave se comparó con la buscada.
- Promedio general = promedio aritmético de los promedios registrados (0.0 si el ABB está vacío).

## Casos de aceptación (verificados por `--demo` y por las pruebas)
| Caso | Resultado obtenido |
|---|---|
| Jerarquía inicial | 9 unidades, 5 hojas, altura 3, grado FISEI 2, nivel 3B 3 |
| ABB tras 12 inserciones | Inorden 20, 30, 35, 40, 45, 50, 60, 65, 70, 75, 80, 85 |
| Insertar de nuevo 40 | Rechazada; conteo 12 |
| Eliminar 20, 60 y 70 | Inorden 30, 35, 40, 45, 50, 65, 75, 80, 85; 9 nodos |
| Eliminar FISEI con descendencia | Rechazo; jerarquía sin cambios |
| Transformación general-binario | Primer hijo de UTA = FISEI → hermano FCHE → hermano FCS |
| Vacíos y un nodo | Sin excepciones; altura vacía -1, singleton 0 |
| Búsqueda ABB (n = 15) | Cadena: 15 comparaciones (existente 15 e inexistente 16); balanceado: 4 y 4. Alturas 14 y 3 |
| Búsqueda ABB (n = 127) | Cadena: 127 comparaciones (O(n)); balanceado: 7 (O(log n)) |

## Complejidad
| Operación | Costo |
|---|---|
| ABB insertar / buscar / eliminar / modificar | O(h): O(log n) si está balanceado, O(n) en cadena |
| ABB recorridos, altura, nodos, hojas, promedio, validar invariante | O(n) |
| ABB mínimo / máximo | O(h) |
| Árbol general buscar, nivel, grado | O(n) |
| Árbol general altura, nodos, hojas, imprimir, conversión | O(n) |
| Agregar unidad (valida unicidad y padre), eliminar hoja, reubicar | O(n) |
| Validar jerarquía (códigos únicos, sin ciclos) | O(n²) por la búsqueda lineal en el arreglo de códigos vistos |

El ABB **no se autoequilibra**: la diferencia entre ambos árboles del análisis proviene solo del orden de inserción.

## Respuestas orientadoras para la defensa
1. **Modificar la matrícula sin reposicionar**: la matrícula es la clave que define la posición; cambiarla in situ puede dejar claves mayores a la izquierda o menores a la derecha, rompiendo el invariante y la búsqueda. Por eso `modificar` solo cambia nombre y promedio.
2. **Dos hijos y sucesor inorden**: el sucesor (mínimo del subárbol derecho) es mayor que todo el subárbol izquierdo y menor o igual que el resto del derecho; al copiarlo al nodo y eliminarlo de su posición original (tiene a lo sumo un hijo) el orden se conserva.
3. **O(n) vs O(log n)**: la búsqueda cuesta O(h). Con inserciones ascendentes h = n-1; con inserciones por punto medio h ≈ log₂ n.
4. **Hijo-izquierdo/hermano-derecho**: se conserva la relación padre-hijo (vía el primer hijo) y el orden entre hermanos (vía el enlace derecho). Error a evitar: leer el enlace derecho como "hijo" o asumir que altura/nivel del binario coinciden con los del general (aquí 3 vs 4).
5. **Evidencia en GitHub**: ramas por integrante, commits propios, PR revisada, y diffs que muestran quién escribió y probó cada parte (ver `docs/GITHUB.md`).

## Tabla de contribuciones (completar con evidencia de GitHub)
| Integrante | Rama | Commits / PR | Parte implementada y probada | Operación a defender |
|---|---|---|---|---|
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

> Solo registren aportes que estén respaldados por commits propios en el repositorio.

**URL del repositorio:** por completar  
**Hash del último commit:** por completar  
**PR revisada:** por completar  
**Ruta de pruebas:** `src/test/java/uta/pruebas/PruebasRunner.java` (salida en `docs/salida_pruebas.txt`)
