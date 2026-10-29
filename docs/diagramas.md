# Diagramas breves

## Arquitectura modular
```mermaid
flowchart LR
    Main[app.Main<br/>menú] --> Auditoria[app.Auditoria]
    Main --> Demo[app.Demo]
    Main --> AG[general.ArbolGeneral]
    Main --> ABB[abb.ArbolABB]
    Main --> AN[analisis.AnalisisBusqueda]
    AG --> NG[general.NodoGeneral]
    AG --> CHH[general.ConversorHijoHermano] --> HH[general.NodoHH]
    ABB --> NA[abb.NodoABB] --> E[abb.Estudiante]
    AN --> ABB
    Datos[app.DatosIniciales] --> AG
    Datos --> ABB
    Pruebas[pruebas.PruebasRunner] -.-> AG
    Pruebas -.-> ABB
```

## Árbol general inicial
```mermaid
flowchart TD
    UTA --> FISEI & FCHE & FCS
    FISEI --> SOFTWARE & TI
    SOFTWARE --> 3A & 3B
    TI --> 4A
```

## Forma hijo-izquierdo / hermano-derecho
```mermaid
flowchart TD
    UTA -- "hijo" --> FISEI
    FISEI -- "hijo" --> SOFTWARE
    FISEI -- "hermano" --> FCHE
    FCHE -- "hermano" --> FCS
    SOFTWARE -- "hijo" --> 3A
    SOFTWARE -- "hermano" --> TI
    3A -- "hermano" --> 3B
    TI -- "hijo" --> 4A
```

## ABB tras las 12 inserciones (altura 3, 6 hojas)
```mermaid
flowchart TD
    50 --> 30 & 70
    30 --> 20 & 40
    40 --> 35 & 45
    70 --> 60 & 80
    60 -- "der" --> 65
    80 --> 75 & 85
```

## ABB tras eliminar 20 (hoja), 60 (un hijo) y 70 (dos hijos, sucesor 75) — 9 nodos, altura 3, 4 hojas
```mermaid
flowchart TD
    50 --> 30 & 75
    30 -- "der" --> 40
    40 --> 35 & 45
    75 --> 65 & 80
    80 -- "der" --> 85
```

Preorden en cada paso: tras 20 `50, 30, 40, 35, 45, 70, 60, 65, 80, 75, 85`; tras 60 `50, 30, 40, 35, 45, 70, 65, 80, 75, 85`; tras 70 `50, 30, 40, 35, 45, 75, 65, 80, 85`.
