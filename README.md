# TP7 - JPA Persistencia de Objetos

**POO - UNJu – Esc. de Minas Dr. Horacio Carrillo**  
**Fecha de presentación:** 21/10/26

## Descripción

Aplicación Java con persistencia JPA/Hibernate que permite registrar facturas de venta de productos a clientes, implementando el patrón DAO/Singleton.

## Integrantes del Grupo 6

| Nombre       | Usuario GitHub |
| ------------ | -------------- |
| Integrante 1 | @usuario1      |
| Integrante 2 | @usuario2      |
| Integrante 3 | @usuario3      |

> **Nota:** Reemplazar `@usuarioN` con los usuarios reales de GitHub de cada integrante.

## Tecnologías

- Java 17 | Maven
- JPA (Jakarta Persistence API 3.2.0)
- Hibernate 5.6.7 | MySQL 8

## Estructura del Proyecto

```
tp7_grupo6/
├── src/main/java/ar/edu/unju/escmi/
│   ├── config/         → EmfSingleton
│   ├── model/          → Cliente, Producto, Factura, DetalleFactura
│   ├── dao/            → Interfaces DAO
│   ├── dao/imp/        → Implementaciones DAO
│   └── main/           → Main (menú)
└── src/main/resources/META-INF/persistence.xml
```

## Ejecución

1. Crear la base de datos MySQL `tp7_grupo6`.
2. Configurar usuario/contraseña en `persistence.xml`.
3. Compilar con `mvn clean compile`.
4. Ejecutar la clase `Main`.
