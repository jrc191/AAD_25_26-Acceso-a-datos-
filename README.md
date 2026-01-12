# AAD_25_26-Acceso-a-datos-

## PRÁCTICA 3_1: Mapeo Objeto-Relacional (ORM) con JPA y Spring Boot

Esta práctica representa la migración de la capa de persistencia de la aplicación: se ha sustituido el uso de SQL manual
y `JdbcTemplate` por un enfoque orientado a objetos utilizando **Spring Data JPA** e **Hibernate**.

## 1. Resumen de los cambios realizados

- **Migración a JPA:** Se ha reemplazado `spring-boot-starter-jdbc` por `spring-boot-starter-data-jpa` en el `pom.xml`.
- **Entidades:** Las clases del modelo (`Student`, `Module`, `Enrollment`) se han convertido en entidades JPA mediante
  anotaciones (`@Entity`, `@Table`, `@Id`, `@OneToMany`, etc.).
- **Repositorios:** Se han eliminado los DAO manuales y los `RowMapper`. Ahora se utilizan interfaces que extienden de
  `JpaRepository`.
- **Limpieza de código:** Se han eliminado las clases de utilidades manuales (`SQLQueries`, validadores manuales,
  constantes) en favor de las funcionalidades del framework.

## 2. Ventajas de usar JPA y Hibernate (vs JdbcTemplate)

- **Adiós al SQL manual:** Las operaciones CRUD básicas (`save`, `findAll`, `delete`) se generan
  automáticamente, eliminando la necesidad de escribir sentencias SQL repetitivas.
- **Mapeo Automático (ORM):** Hibernate se encarga de transformar automáticamente los registros de la base
  de datos en objetos Java y viceversa.
- **Validación Estándar:** Se utilizan anotaciones de `Jakarta Validation` (`@NotBlank`, `@Email`,
  `@NotNull`) directamente en las entidades para asegurar la integridad de los datos.
- **Gestión de Relaciones:** Las claves foráneas se gestionan como relaciones entre objetos Java (
  `List<Enrollment>`, `Student student`), simplificando la navegación entre datos.

## 3. Gestión de Transacciones y Persistencia

- **@Transactional:** Se sigue utilizando para delimitar unidades de trabajo atómicas. Si ocurre una
  excepción (ej. `RuntimeException`), Spring realiza un **rollback** automático, deshaciendo cualquier cambio en la base
  de datos para mantener la consistencia.
- **Contexto de Persistencia:** JPA optimiza el rendimiento manteniendo un contexto de persistencia que
  sincroniza los objetos con la base de datos solo cuando es necesario.

## 4. Consultas Personalizadas

Aunque JPA genera las consultas básicas, se han implementado consultas avanzadas de dos formas:

1. **Métodos derivados:** Como `findByName(String name)` (Spring genera el SQL automáticamente).
2. **JPQL y @Query:** Consultas personalizadas sobre objetos, por ejemplo:
   ```java
   @Query("SELECT e FROM Enrollment e WHERE e.finalGrade >= :grade");
   ```

## 5. Pruebas y Validación

- Se han adaptado las pruebas unitarias y de integración para trabajar con JPA y los repositorios.
- Se ha verificado que todas las operaciones CRUD y las consultas personalizadas funcionan correctamente.
- Todo se realiza en un fichero `TestRunner.java` que incluye pruebas para los repositorios.