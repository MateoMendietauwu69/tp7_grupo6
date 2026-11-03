# TP7 - JPA: Persistencia de Objetos

**Programación Orientada a Objetos — Técnico en Informática**
**UNJu — Esc. de Minas Dr. Horacio Carrillo — San Salvador de Jujuy**

Aplicación de consola que permite registrar la venta de productos a un cliente mediante
facturas, persistiendo los datos en una base **MySQL** con **JPA / Hibernate**.

## Integrantes del Grupo 6

| Nombre | Usuario de GitHub |
|--------|-------------------|
| Erick Neuman | [@Erickscpp](https://github.com/Erickscpp) |
| Lautaro Mercado | [@IsoMercado](https://github.com/IsoMercado) |
| Mateo Mendieta | [@MateoMendietauwu69](https://github.com/MateoMendietauwu69) |
| Ignacio Jorqui | [@jorquig](https://github.com/jorquig) |
| David Palacios | [@DavidPalaciosCv](https://github.com/DavidPalaciosCv) |

## Tecnologías

- **Java 17** (o superior)
- **Maven**
- **JPA** (`jakarta.persistence-api` 3.2.0)
- **Hibernate** 5.6.7 (variante Jakarta: `hibernate-core-jakarta`)
- **MySQL** 8 (`mysql-connector-java` 8.0.30)

## Requisitos previos

1. **JDK 17 o superior** instalado.
2. **Maven** instalado.
3. **MySQL Server** en ejecución (puerto `3306` por defecto).
   - *MySQL Workbench* es opcional (cliente gráfico para ver las tablas).
4. La base de datos **se crea sola** la primera vez (la URL de conexión usa
   `createDatabaseIfNotExist=true`). Solo asegurate de que el usuario y la contraseña
   del [persistence.xml](src/main/resources/META-INF/persistence.xml) coincidan con los
   de tu instalación de MySQL (por defecto `root` / `root`).

## Cómo ejecutar

```bash
# Compilar
mvn clean compile

# Ejecutar la aplicación (menú de consola)
mvn exec:java
```

> También podés ejecutar la clase `ar.edu.unju.escmi.main.Main` directamente desde tu IDE
> (Eclipse / IntelliJ / VS Code).

## Menú de opciones

```
1  - Alta de cliente.
2  - Alta de producto.
3  - Realizar la venta de productos (alta de una nueva factura).
4  - Buscar una factura por su número y mostrar todos sus datos.
5  - Eliminar una factura (lógica).
6  - Eliminar un producto (lógica).
7  - Modificar datos de cliente.
8  - Modificar precio de producto.
9  - Eliminar cliente (lógica).
10 - Mostrar todas las facturas.
11 - Mostrar todos los clientes.
12 - Mostrar las facturas que superen el total de $900.000.
13 - Salir.
```

## Estructura del proyecto

```
src/main/java/ar/edu/unju/escmi/
├── config/        EmfSingleton (Singleton del EntityManagerFactory)
├── model/         Entidades JPA: Cliente, Producto, Factura, DetalleFactura
├── dao/           Interfaces DAO (IClienteDao, IProductoDao, IFacturaDao, IDetalleFacturaDao)
│   └── imp/       Implementaciones DAO
└── main/          Main (menú de opciones)

src/main/resources/META-INF/
└── persistence.xml   Unidad de persistencia "tp7_grupo6PU"
```

## Patrones aplicados

- **Singleton**: `EmfSingleton` (única instancia del `EntityManagerFactory`) y los DAO.
- **DAO** (Data Access Object): interfaces + implementaciones que encapsulan el acceso a datos.
- **Eliminación lógica**: mediante el atributo `estado (boolean)` en Cliente, Producto y Factura.
