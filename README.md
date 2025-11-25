# AAD_25_26-Acceso-a-datos-

## PRÁCTICA 2_2: Gestión de matrículas con Spring Boot, JdbcTemplate y PostgreSQL

## 1. Resumen de los cambios realizados

- Se reemplazó por completo el contenido anterior para ajustarse al uso de JdbcTemplate.
- Se eliminaron referencias a JDBC manual.
- Se reestructuró el documento para mayor claridad.
- Se añadieron secciones relacionadas con transacciones declarativas.

## 2. Ventajas de usar JdbcTemplate y DataSource

- Las operaciones CRUD se han implementado mediante JdbcTemplate, reduciendo código repetitivo.
- La gestión de transacciones se realiza mediante @Transactional, sin necesidad de commit ni rollback manuales.
- La gestión de conexiones se realiza automáticamente gracias a DataSource.
- El uso de consultas parametrizadas aporta seguridad frente a inyecciones.

## 3. Manejo de transacciones declarativas en Spring Boot

- Las transacciones se gestionan mediante @Transactional.
- Spring decide automáticamente cuándo abrir, confirmar o revertir la transacción.
- Si ocurre una excepción, se realiza rollback.