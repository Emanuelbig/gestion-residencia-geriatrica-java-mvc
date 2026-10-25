# UML — Gestión de Residencia Geriátrica

[← Volver al README](../README.md)

## 1. Modelos

```mermaid
classDiagram
  direction TB
  class Persona {
    <<abstract>>
    -int id
    -String dni
    -String nombre
    -String apellido
    +getNombreCompleto() String
    +getRol()* String
    +normalizarNombre(String)$ String
    +equals(Object) boolean
    +hashCode() int
  }
  class Residente {
    +getRol() String
  }
  class Enfermero {
    +getRol() String
  }
  class Habitacion {
    -String codigo
    +normalizarCodigo(String)$ String
  }
  class Reserva {
    -int idResidente
    -String codigoHabitacion
  }
  class Asignacion {
    -int idEnfermero
    -String codigoHabitacion
  }
  Persona <|-- Residente
  Persona <|-- Enfermero
  Residente "1" -- "0..1" Reserva : ocupa
  Habitacion "1" -- "0..1" Reserva : tiene
  Enfermero "1" -- "0..*" Asignacion : atiende
  Habitacion "1" -- "0..1" Asignacion : tiene
```

`Habitacion` no tiene un campo `ocupada`: se calcula buscando si existe una `Reserva` con su código.

## 2. Persistencia (DAO)

```mermaid
classDiagram
  direction TB
  class GenericDAO~T, ID~ {
    <<interface>>
    +crear(T) void
    +listarTodos() List~T~
    +buscarPorId(ID) Optional~T~
    +actualizar(T) void
    +eliminar(ID) void
  }
  class ArchivoDAO~T, ID~ {
    <<abstract>>
    -Path archivo
    -List~T~ entidades
    #aLinea(T)* String
    #desdeLinea(String)* T
    #idDe(T)* ID
    -leerArchivo() List~T~
    -guardarArchivo() void
  }
  GenericDAO <|.. ArchivoDAO
  ArchivoDAO <|-- ResidenteDAOArchivo
  ArchivoDAO <|-- EnfermeroDAOArchivo
  ArchivoDAO <|-- HabitacionDAOArchivo
  ArchivoDAO <|-- ReservaDAOArchivo
  ArchivoDAO <|-- AsignacionDAOArchivo
  class SecuenciaDAO {
    <<interface>>
    +leerUltimo() int
    +guardarUltimo(int) void
  }
  SecuenciaDAO <|.. SecuenciaArchivoDAO
```

`SecuenciaDAO` guarda el último id o código usado (`data/ultimo-residente.txt`, `ultimo-enfermero.txt`, `ultima-habitacion.txt`), así no se repite aunque se borre el registro.

| DAO | `T` | `ID` | Línea en el archivo |
|---|---|---|---|
| `ResidenteDAOArchivo` | `Residente` | `Integer` | `1001,30111222,Juan,Perez` |
| `EnfermeroDAOArchivo` | `Enfermero` | `Integer` | `2001,28999111,Ana,Diaz` |
| `HabitacionDAOArchivo` | `Habitacion` | `String` | `HAB-101` |
| `ReservaDAOArchivo` | `Reserva` | `String` (código) | `1001,HAB-101` |
| `AsignacionDAOArchivo` | `Asignacion` | `String` (código) | `2001,HAB-101` |

## 3. Servicios

```mermaid
classDiagram
  direction TB
  class PersonaServicio~T extends Persona~ {
    <<abstract>>
    -GenericDAO dao
    -GenericDAO otrasPersonasDAO
    -SecuenciaDAO secuencia
    -int primerId
    +crear(PersonaDTO) void
    +listar() List~PersonaDTO~
    +obtener(int) PersonaDTO
    +modificar(int, PersonaDTO) void
    +eliminar(int) void
    #crearPersona(int, String, String, String)* T
    #estaEnUso(int)* boolean
    #mensajeEnUso(int)* String
    #nombreEntidad()* String
  }
  PersonaServicio <|-- ResidenteServicio
  PersonaServicio <|-- EnfermeroServicio
  class HabitacionServicio {
    +crear() String
    +listar() List~HabitacionDTO~
    +obtener(String) HabitacionDetalleDTO
    +eliminar(String) void
  }
  class ReservaServicio {
    +reservar(int, String) void
    +liberar(String) void
    +listar() List~PersonaHabitacionDTO~
    +residentesSinHabitacion() List~PersonaDTO~
    +habitacionesLibres() List~String~
  }
  class AsignacionServicio {
    +asignar(int, String) void
    +quitar(String) void
    +listar() List~PersonaHabitacionDTO~
    +habitacionesSinEnfermero() List~String~
  }
  class ReporteServicio {
    +habitacionDeResidente(int) PersonaHabitacionDTO
    +residentesDeEnfermero(int) List~PersonaDTO~
    +enfermeroDeResidente(int) Optional~PersonaDTO~
  }
```

Todos los servicios reciben sus `GenericDAO` por constructor y nunca hacen `new` de un DAO.

**DTO:** `PersonaDTO` (con `desde(Persona)` para armarlo desde la entidad) · `HabitacionDTO` (código y si está ocupada) · `HabitacionDetalleDTO` (código, residente y enfermero) · `PersonaHabitacionDTO` (una persona con el código de su habitación; se usa en las reservas, las asignaciones y la consulta 1).

## 4. Vista y controllers

```mermaid
classDiagram
  direction TB
  class Vista {
    <<interface>>
    +getTitulo() String
    +mostrar() void
  }
  class MenuPrincipal {
    -List~Vista~ vistas
    +registrar(Vista) void
    +iniciar() void
  }
  class PersonaController {
    <<interface>>
    +alta(PersonaDTO) void
    +listar() List~PersonaDTO~
    +buscar(int) PersonaDTO
    +modificar(int, PersonaDTO) void
    +baja(int) void
  }
  class Consola {
    +pedirTexto(String) String
    +pedirEntero(String) int
    +pedirConFormato(String, String, String) String
  }
  MenuPrincipal o-- Vista
  Vista <|.. VistaPersonas
  Vista <|.. VistaHabitaciones
  Vista <|.. VistaReservas
  Vista <|.. VistaAsignaciones
  Vista <|.. VistaReportes
  VistaPersonas --> PersonaController
  PersonaController <|.. ResidenteController
  PersonaController <|.. EnfermeroController
  VistaPersonas --> Consola
```

`VistaPersonas` usa la interfaz `PersonaController`, por eso la misma pantalla sirve para residentes y enfermeros. Cada controller recibe su servicio por constructor.

## 5. Excepciones

```mermaid
classDiagram
  direction TB
  RuntimeException <|-- GeriatricoException
  class GeriatricoException {
    <<abstract>>
  }
  GeriatricoException <|-- DatosInvalidosException
  GeriatricoException <|-- EntidadDuplicadaException
  GeriatricoException <|-- EntidadNoEncontradaException
  GeriatricoException <|-- OperacionNoPermitidaException
  GeriatricoException <|-- PersistenciaException
```

| Excepción | Cuándo se lanza |
|---|---|
| `DatosInvalidosException` | Un dato no tiene el formato correcto (DNI con letras, nombre con números) |
| `EntidadDuplicadaException` | DNI o código de habitación repetido |
| `EntidadNoEncontradaException` | Id o código que no existe |
| `OperacionNoPermitidaException` | Se viola una regla (habitación ocupada, borrar algo en uso) |
| `PersistenciaException` | Falla la lectura o escritura de los archivos |

Los servicios las lanzan y las vistas las atrapan como `GeriatricoException` para mostrar el mensaje.
