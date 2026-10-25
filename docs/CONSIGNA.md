# Consigna del parcial

[← Volver al README](../README.md)

Parcial de **Programación II** (INSPT-UTN, Prof. Miguel Silva Campos).

## Sistema de Gestión de Residencia Geriátrica

Desarrollar una aplicación en Java para gestionar la información de una residencia geriátrica, incluyendo residentes, enfermeros y habitaciones. El sistema debe permitir realizar operaciones CRUD sobre cada entidad y gestionar sus relaciones.

### Objetos

| Objeto | Datos |
|---|---|
| Residente | ID, DNI, nombre, apellido |
| Enfermero | ID, DNI, nombre, apellido |
| Habitación | Código, estado (ocupada / libre) |
| Reserva | Código de habitación, ID de residente |
| Asignación | Código de habitación, ID de enfermero |

### Relaciones

- Residentes ↔ Habitaciones: **uno a uno**.
- Enfermeros → Habitaciones: **uno a muchos**.

### Funcionalidades

Un menú principal donde un administrativo tenga control absoluto sobre el sistema:

- Ingresar, modificar, consultar o eliminar datos de residentes, enfermeros y habitaciones.
- Asignar residentes a habitaciones.
- Asignar enfermeros a habitaciones.
- Consultar:
  - El nombre del residente con su habitación asignada.
  - Los residentes atendidos por un enfermero.
  - El enfermero asignado a un residente.

Cada entidad debe tener su menú específico, con opciones CRUD y funcionalidades relacionadas.

Los ids y los códigos se generan automáticamente y de manera incremental. El usuario solo los ingresa para consultar.

---

## Dónde se cumple cada punto

| Punto de la consigna | Dónde está |
|---|---|
| CRUD de residentes y enfermeros | `VistaPersonas` → `PersonaController` → `PersonaServicio<T>` |
| CRUD de habitaciones | `VistaHabitaciones` → `HabitacionServicio`: alta, listado, consulta (`obtener`) y baja. No hay "modificar" porque el código es la identidad y el estado se calcula |
| Estado ocupada / libre | Se calcula a partir de las reservas (`HabitacionServicio.listar()`), no se guarda: así hay una sola fuente de verdad |
| Residente ↔ Habitación, uno a uno | `ReservaServicio.reservar()` |
| Enfermero → Habitaciones, uno a muchos | `AsignacionServicio.asignar()` |
| Las 3 consultas | `VistaReportes` → `ReporteServicio`. La 1 devuelve el residente con su habitación (`PersonaHabitacionDTO`) |
| Menú principal y un menú por entidad | `MenuPrincipal` + una `Vista` por tema |
| Ids y códigos automáticos e incrementales | `PersonaServicio.generarId()` (residentes desde 1001, enfermeros desde 2001) y `HabitacionServicio.generarNumero()` (desde HAB-101). El último número usado se guarda con `SecuenciaDAO` (`data/ultimo-*.txt`), así no se repite aunque se borre el registro |
