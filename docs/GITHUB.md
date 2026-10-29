# Flujo de trabajo en GitHub (guía para el grupo)

La rúbrica evalúa **evidencia real** en GitHub: nadie debe recibir crédito por aportes que no estén en el historial.
Este proyecto trae el código base completo; el grupo debe **repartirse el trabajo, revisarlo y defenderlo**.

## 1. Preparar el repositorio
```bash
git init
git add .
git commit -m "chore: estructura inicial del proyecto"
git branch -M main
git remote add origin <URL_DEL_REPOSITORIO>
git push -u origin main
```

## 2. Una rama feature por integrante
```bash
git checkout -b feature/<nombre>-arbol-general   # ejemplos: -abb-eliminacion, -pruebas, -analisis-readme
# ... trabajar, probar con scripts/probar.sh ...
git add <archivos>
git commit -m "feat(abb): elimina nodo con dos hijos usando sucesor inorden"
git push -u origin feature/<nombre>-arbol-general
```

## 3. Pull request revisada por otro integrante
1. Abrir la PR en GitHub hacia `main`.
2. Otro integrante revisa (Files changed), deja al menos un comentario y aprueba.
3. Fusionar solo después de la revisión.

## 4. Sugerencia de reparto (ajústenla)
| Integrante | Rama | Responsabilidad sugerida |
|---|---|---|
| 1 | feature/<n1>-arbol-general | ArbolGeneral + reglas de integridad |
| 2 | feature/<n2>-abb-basico | ABB: inserción, búsqueda, recorridos, métricas |
| 3 | feature/<n3>-abb-eliminacion | ABB: modificación y eliminación (3 casos) |
| 4 | feature/<n4>-pruebas | PruebasRunner y casos límite |
| 5 | feature/<n5>-integracion | Menú, auditoría, análisis, README |

## 5. Entrega
Enviar: URL del repositorio, hash del último commit (`git rev-parse HEAD`), ruta de pruebas
(`src/test/java/uta/pruebas/PruebasRunner.java`) y capturas de `scripts/probar.sh` y `scripts/ejecutar.sh --demo`.
