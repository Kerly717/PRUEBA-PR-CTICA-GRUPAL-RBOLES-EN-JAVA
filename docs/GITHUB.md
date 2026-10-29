# Reparto del proyecto en GitHub — Grupo (5 integrantes)

Repositorio: https://github.com/Kerly717/PRUEBA-PR-CTICA-GRUPAL-RBOLES-EN-JAVA

Cada integrante sube **su carpeta** de este zip con **su propia cuenta**, en **su rama**, y abre **una pull request** que otro integrante revisa antes de fusionar. El orden importa: cada parte compila solo si las anteriores ya están en `main` (verificado etapa por etapa).

## 1. Quién hace qué

| Orden | Integrante | Carpeta del zip | Rama | Contenido | Revisor de su PR |
|---|---|---|---|---|---|
| A (paralelo) | Espinoza Reyes Kerly Margoth | `1-Kerly-Espinoza` | `feature/kerly-arbol-general` | Árbol general (`NodoGeneral`, `ArbolGeneral`), scripts, `.vscode`, `.gitignore`, `docs/GITHUB.md` | Gabriel |
| A (paralelo) | Jijón Viscaino Gabriel Sebastián | `2-Gabriel-Jijon` | `feature/gabriel-abb` | ABB (`Estudiante`, `NodoABB`, `ResultadoBusqueda`, `ArbolABB`) | Kerly |
| B | Gamboa Araujo Rommel Fabricio | `3-Rommel-Gamboa` | `feature/rommel-conversion-auditoria` | Conversión hijo-izquierdo/hermano-derecho (`NodoHH`, `ConversorHijoHermano`), `DatosIniciales`, `Auditoria`, `docs/diagramas.md` | Italo |
| C | Manobanda Puaquiza Italo Esteban | `4-Italo-Manobanda` | `feature/italo-analisis-menu` | Análisis de búsqueda (`AnalisisBusqueda`), `Demo`, menú `Main` | Jonathan |
| D | Tenorio Ronquillo Jonathan Sebastián | `5-Jonathan-Tenorio` | `feature/jonathan-pruebas-docs` | 30 pruebas (`PruebasRunner`), salidas, `CAMBIOS.md`, capturas, README final | Rommel |

- **A**: Kerly y Gabriel trabajan al mismo tiempo, después del commit inicial de Kerly en `main` (sección 2).
- **B** empieza cuando las dos PR de A estén fusionadas en `main`.
- **C** empieza cuando la de B esté fusionada; **D**, cuando la de C esté fusionada.

## 2. Preparación (una sola vez)

**Kerly (dueña del repositorio):**
1. GitHub → Settings → Collaborators → *Add people*: agregar a los 4 compañeros. Cada uno acepta la invitación desde su correo.
2. Si el repositorio está vacío, crear el primer commit en `main` con el `README.md` de su carpeta:
   ```bash
   git clone https://github.com/Kerly717/PRUEBA-PR-CTICA-GRUPAL-RBOLES-EN-JAVA.git
   cd PRUEBA-PR-CTICA-GRUPAL-RBOLES-EN-JAVA
   # copiar SOLO README.md desde 1-Kerly-Espinoza
   git add README.md
   git commit -m "chore: README inicial del proyecto"
   git branch -M main
   git push -u origin main
   ```
3. Recomendado: Settings → Branches → *Add rule* para `main` → marcar *Require a pull request before merging* y *Require approvals: 1*. Así GitHub obliga a la revisión.

**Todos (en su computadora):**
```bash
git config --global user.name "Nombre Apellido"
git config --global user.email "el-correo-de-su-cuenta-de-github"
```
El correo debe ser el de su cuenta; si no, GitHub no les atribuye los commits.

## 3. Pasos de cada integrante

```bash
# 1. Clonar (solo la primera vez) o actualizar main
git clone https://github.com/Kerly717/PRUEBA-PR-CTICA-GRUPAL-RBOLES-EN-JAVA.git
cd PRUEBA-PR-CTICA-GRUPAL-RBOLES-EN-JAVA
git checkout main
git pull

# 2. Crear su rama (use el nombre de la tabla)
git checkout -b feature/<nombre>-<tarea>

# 3. Copiar el contenido de SU carpeta del zip a la raíz del repositorio,
#    respetando las subcarpetas (src/main/java/uta/..., docs/, scripts/).
#    PowerShell, si el zip está descomprimido junto al repositorio:
#    Copy-Item -Path ..\reparto\<SU-CARPETA>\* -Destination . -Recurse -Force

# 4. Compilar antes de subir (ver sección 5)

# 5. Commits sustanciales (al menos 2, ver sección 4)
git add <archivos>
git commit -m "<mensaje>"

# 6. Subir la rama
git push -u origin feature/<nombre>-<tarea>
```

7. En GitHub aparece **Compare & pull request** → base `main` ← su rama. Título y descripción según la sección 6. En *Reviewers*, elegir a su revisor.
8. El revisor abre **Files changed**, deja al menos un comentario sobre el código y pulsa **Review changes → Approve**.
9. Después de la aprobación, el autor pulsa **Merge pull request** y avisa al siguiente integrante.

## 4. Commits sugeridos por integrante

Copie primero los archivos del primer commit, haga el commit y luego los del segundo.

| Integrante | Commit 1 | Commit 2 | Commit 3 |
|---|---|---|---|
| Kerly | `chore: scripts de compilación, .gitignore y configuración de VS Code` → `scripts/`, `.gitignore`, `.vscode/` | `feat(general): árbol general con unicidad, nivel, grado, altura, hojas y borrado seguro` → `NodoGeneral.java`, `ArbolGeneral.java` | `docs: guía de trabajo en GitHub` → `docs/GITHUB.md` |
| Gabriel | `feat(abb): estudiante con clave inmutable y validación de promedio` → `Estudiante.java`, `NodoABB.java` | `feat(abb): inserción, búsqueda con contador, recorridos, modificación y eliminación de 3 casos` → `ResultadoBusqueda.java`, `ArbolABB.java` | — |
| Rommel | `feat(general): conversión hijo-izquierdo/hermano-derecho y reconstrucción` → `NodoHH.java`, `ConversorHijoHermano.java` | `feat(app): datos iniciales y reporte de auditoría integrada` → `DatosIniciales.java`, `Auditoria.java` | `docs: diagramas de arquitectura, jerarquía, HI/HD y ABB` → `docs/diagramas.md` |
| Italo | `feat(analisis): comparación de búsqueda en ABB en cadena vs equilibrado` → `AnalisisBusqueda.java` | `feat(app): demo de casos de aceptación y menú de consola` → `Demo.java`, `Main.java` | — |
| Jonathan | `test: 30 casos automatizados con aserciones` → `src/test/.../PruebasRunner.java` | `docs: salidas de ejecución, registro de cambios y README final` → `docs/salida_*.txt`, `docs/CAMBIOS.md`, `docs/capturas/`, `README.md` | `docs: capturas de pruebas y demo` → imágenes en `docs/capturas/` |

## 5. Cómo comprobar que compila antes de subir

| Integrante | Comando (Windows; en Linux/macOS use `.sh`) | Resultado esperado |
|---|---|---|
| Kerly | `scripts\compilar.bat` | `Compilacion correcta.` |
| Gabriel | `mkdir out` y luego `javac -encoding UTF-8 -d out src\main\java\uta\abb\*.java` (los scripts llegan con la PR de Kerly) | Sin errores |
| Rommel | `scripts\compilar.bat` | `Compilacion correcta.` |
| Italo | `scripts\ejecutar.bat --demo` | `RESULTADO: todos los casos de aceptación CUMPLEN.` |
| Jonathan | `scripts\probar.bat` | `Pruebas ejecutadas: 30 \| exitosas: 30 \| fallidas: 0` |

Jonathan, con todo fusionado, toma las capturas de `scripts\probar.bat` y `scripts\ejecutar.bat --demo`, las guarda en `docs/capturas/` y regenera las salidas si cambiaron:
```bat
scripts\probar.bat > docs\salida_pruebas.txt
scripts\ejecutar.bat --demo > docs\salida_demo.txt
```

## 6. Plantilla de pull request

```text
Título: <igual al commit principal>

Qué agrega:
- <archivos y métodos>

Cómo se probó:
- <comando de la sección 5 y resultado>

Revisor: @<usuario-del-revisor>
```

El revisor comenta algo concreto del código, por ejemplo el caso base de un método o un caso límite, no solo "ok".

## 7. Defensa individual (2–3 minutos)

| Integrante | Operación | Caso base | Prueba de fallo | Complejidad |
|---|---|---|---|---|
| Kerly | `ArbolGeneral.eliminarDetallado` (borrado seguro) | `buscar(null)` devuelve `null` | 05 y 22: eliminar FISEI → `TIENE_HIJOS` | O(n) |
| Gabriel | `ArbolABB.eliminar` con dos hijos (sucesor inorden) | `nodo == null` devuelve `null` | 14: eliminar 70 → 75 ocupa su lugar | O(h) |
| Rommel | `ConversorHijoHermano.convertir` | nodo sin hijos: `primerHijo = null` | 09 y 25: UTA → FISEI → FCHE → FCS | O(n) |
| Italo | `AnalisisBusqueda.medio` y `ArbolABB.buscarContando` | `ini > fin` | 28: 15 claves, 15 vs 4 comparaciones | O(n) vs O(log n) |
| Jonathan | `PruebasRunner` y consistencia del promedio | ABB vacío: promedio 0 | 26: preorden exacto tras cada eliminación | O(n) por recorrido |

## 8. Entrega al docente

- URL: https://github.com/Kerly717/PRUEBA-PR-CTICA-GRUPAL-RBOLES-EN-JAVA
- Hash del último commit: tras la última fusión, `git checkout main`, `git pull` y `git rev-parse HEAD`. Se envía en la entrega; el README no puede contener su propio hash.
- Ruta de pruebas: `src/test/java/uta/pruebas/PruebasRunner.java`
- Capturas: `docs/capturas/`
- Enlaces a las 5 PR revisadas (pestaña *Pull requests → Closed*).
