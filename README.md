# AAD_25_26-Acceso-a-datos-

## Ventajas de usar `JdbcTemplate` y `DataSource` frente a JDBC manual

- Abstracción y simplicidad: `JdbcTemplate` reduce la gestión de recursos, cierre de conexiones y manejo de excepciones
  de manera manual, permitiendo enfocarse en la lógica SQL.
- Gestión automática de recursos: al usar un `DataSource` (p. ej. HikariCP) se reutilizan conexiones y se evitan fugas
  de recursos.
- Mejor manejo de excepciones: `JdbcTemplate` envuelve excepciones JDBC en una jerarquía consistente de Spring (
  `DataAccessException`), facilitando el manejo y testing.
- Rendimiento y escalabilidad: `DataSource` con pool proporciona conexiones precreadas, menor latencia y control de
  concurrencia.
- Legibilidad y mantenimiento: consultas y mapeos expresados de forma concisa ayudan a mantener el código más limpio y
  testeable.

## Manejo de transacciones declarativas en Spring Boot

- Concepto: las transacciones declarativas permiten delimitar unidades de trabajo mediante anotaciones (\@Transactional)
  sin manejar manualmente `Connection.commit()`/`rollback()`.
- Uso típico:
    - Anotar servicios o métodos con `@Transactional` (p. ej. en la capa `service`). Spring abre una transacción al
      entrar y la cierra (commit) al salir si no hay excepciones no controladas; realiza rollback si ocurre una
      excepción marcada para rollback.
    - Configuración mínima en Spring Boot: incluir dependencia `spring-boot-starter-jdbc` o
      `spring-boot-starter-data-jpa`; Spring configura el `PlatformTransactionManager` automáticamente cuando existe un
      `DataSource`.
- Buenas prácticas:
    - Anotar en la capa de servicio (no en repositorios) para agrupar varias operaciones en una sola transacción.
    - Evitar `@Transactional` en métodos `private` o llamadas internas del mismo bean (por proxies).
    - Definir el nivel de aislamiento/timeout si es necesario:
      `@Transactional(isolation = Isolation.READ_COMMITTED, timeout = 30)`.
    - Atrapar excepciones solo cuando se desee evitar rollback explícitamente; para evitar rollback en excepciones
      comprobadas usar `noRollbackFor` o relanzar como `RuntimeException`.
- Beneficios:
    - Consistencia y atomicidad automáticas para múltiples operaciones sobre la base de datos.
    - Menos código repetitivo y menor probabilidad de errores en commit/rollback manual.
    - Integración con manejo de excepciones y transacciones distribuidas (si es necesario).

---