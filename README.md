## PRÁCTICA  2_1: Gestión de matrículas con JDBC y PostgreSQL

## 1. Descripción general

Este proyecto implementa un sistema de gestión de matrículas usando JDBC puro.

- Tecnologías principales: Java, Spring Boot, Maven, PostgreSQL (ejecutado en Docker).
- Características: conexión a PostgreSQL vía `application.yml` y `PostgresqlDriver`, ejecución de scripts SQL al
  arranque, control manual de transacciones (begin/commit/rollback), uso de `Connection`, `PreparedStatement` y
  `ResultSet`, y llamadas a funciones almacenadas (por ejemplo `count_enrollments`).

## 2. Instrucciones de ejecución

Requisitos previos:

- Java 17+ instalado.
- Maven 3.8+.
- Docker Desktop (Windows) con soporte para `docker compose`.

1) Levantar PostgreSQL con Docker

- Nos posicionamos en la ubicación del proyecto donde está `docker-compose.yml`. (carpeta main/resources)
- Ejecutar:
    - `docker compose up -d`
- Comprobar contenedores:
    - `docker ps`
- Comprobar logs del contenedor DB:
    - `docker compose logs -f`

2) Verificar configuración

- Abrir `application.yml` para confirmar `spring.datasource.url`, `username` y `password`.
- Esperar a que PostgreSQL esté listo (ver logs del contenedor).

3) Compilar y ejecutar el proyecto

- Compilar:
    - `mvn clean package`
- Ejecutar desde Maven (arranca la aplicación y ejecuta `run()` si está implementado como `CommandLineRunner`):
    - `mvn spring-boot:run`
- Ejecutar desde IntelliJ IDEA:
    - Importar proyecto Maven, localizar la clase `main` (aplicación Spring Boot) y ejecutar "Run".
    - Al iniciar la aplicación, el método `run()` proporcionado por la aplicación (si existe) se ejecutará
      automáticamente y generará trazas en consola y escrituras en la base de datos (`student`, `module`, `enrollment`).

## 3. Evidencias de ejecución (ejemplos de trazas y consultas)

Trazas de consola (ejemplo):

```text
2025-11-25T12:35:09.259+01:00  INFO 18104 --- [           main] com.jramcon398.jrc.JrcApplication        : Starting JrcApplication using Java 21.0.8 with PID 18104 (C:\Users\User\IdeaProjects\AAD_25_26-Acceso-a-datos-\target\classes started by User in C:\Users\User\IdeaProjects\AAD_25_26-Acceso-a-datos-)
2025-11-25T12:35:09.261+01:00  INFO 18104 --- [           main] com.jramcon398.jrc.JrcApplication        : No active profile set, falling back to 1 default profile: "default"
2025-11-25T12:35:09.757+01:00  INFO 18104 --- [           main] c.j.jrc.config.PostgresqlDriver          : Initializing database...
2025-11-25T12:35:09.757+01:00  INFO 18104 --- [           main] c.j.jrc.config.PostgresqlDriver          : Database initialized successfully!
2025-11-25T12:35:09.910+01:00  INFO 18104 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2025-11-25T12:35:10.176+01:00  INFO 18104 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@44bd4b0a
2025-11-25T12:35:10.178+01:00  INFO 18104 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2025-11-25T12:35:10.306+01:00  INFO 18104 --- [           main] com.jramcon398.jrc.JrcApplication        : Started JrcApplication in 1.471 seconds (process running for 2.059)
2025-11-25T12:35:10.310+01:00  WARN 18104 --- [           main] c.jramcon398.jrc.utils.StudentValidator  : Student email 'miriam@g.educaand.es' seems invalid. Using anyway.
2025-11-25T12:35:10.311+01:00  INFO 18104 --- [           main] c.j.j.a.StudentManagementService         : Creating student: Student(id=null, nif=66280457T, name=Miriam, email=miriam@g.educaand.es, course=DAW, modules=[])
2025-11-25T12:35:10.311+01:00  WARN 18104 --- [           main] c.jramcon398.jrc.utils.StudentValidator  : Student email 'miriam@g.educaand.es' seems invalid. Using anyway.
2025-11-25T12:35:10.410+01:00  INFO 18104 --- [           main] c.j.jrc.repository.StudentRepository     : Student not found with NIF: 66280457T
2025-11-25T12:35:10.465+01:00  INFO 18104 --- [           main] c.j.jrc.repository.StudentRepository     : Successfully added student with ID 65: Student(id=65, nif=66280457T, name=Miriam, email=miriam@g.educaand.es, course=DAW, modules=[])
2025-11-25T12:35:10.465+01:00  INFO 18104 --- [           main] c.j.j.a.StudentManagementService         : Student created successfully: Student(id=65, nif=66280457T, name=Miriam, email=miriam@g.educaand.es, course=DAW, modules=[])
2025-11-25T12:35:10.466+01:00  INFO 18104 --- [           main] c.j.j.a.StudentManagementService         : Creating module: Module(id=null, code=0485, name=Programación, hours=250)
2025-11-25T12:35:10.518+01:00  INFO 18104 --- [           main] c.j.jrc.repository.ModuleRepository      : Found module by code: Module(id=10, code=0485, name=Programación, hours=250)
2025-11-25T12:35:10.518+01:00  WARN 18104 --- [           main] c.j.j.a.StudentManagementService         : Module with code 0485 already exists, returning existing module
2025-11-25T12:35:10.564+01:00  INFO 18104 --- [           main] c.j.jrc.repository.StudentRepository     : Found student: Student(id=65, nif=66280457T, name=Miriam, email=miriam@g.educaand.es, course=null, modules=[])
2025-11-25T12:35:10.568+01:00  INFO 18104 --- [           main] c.j.jrc.repository.ModuleRepository      : Found module: Module(id=10, code=0485, name=Programación, hours=250)
2025-11-25T12:35:10.576+01:00  INFO 18104 --- [           main] c.j.jrc.repository.EnrollmentRepository  : Enrollment created: Enrollment(date=2025-11-25, studentId=65, moduleId=10) (rows affected: 1)
2025-11-25T12:35:10.580+01:00  INFO 18104 --- [           main] c.j.j.a.StudentManagementService         : Successfully enrolled student 65 in module 10
2025-11-25T12:35:10.632+01:00  INFO 18104 --- [           main] c.j.jrc.repository.StudentRepository     : Student deleted: id=65 (rows affected: 1)
2025-11-25T12:35:10.632+01:00  INFO 18104 --- [           main] c.j.j.a.StudentManagementService         : Getting enrollment count for student 65
2025-11-25T12:35:10.688+01:00  INFO 18104 --- [           main] c.j.jrc.repository.EnrollmentRepository  : Total enrollments: 0 
2025-11-25T12:35:10.688+01:00  INFO 18104 --- [           main] c.j.j.a.StudentManagementService         : Student 65 has 0 enrollments
2025-11-25T12:35:10.693+01:00  INFO 18104 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2025-11-25T12:35:10.705+01:00  INFO 18104 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

Las pruebas del programa se realizan con el siguiente código (en la clase main del proyecto):

```java
    public void run(String... args) throws Exception {

    Student miriam = new Student(null, "66280457T", "Miriam", "miriam@g.educaand.es", "DAW");
    Module programacion = new Module(null, "0485", "Programación", 250);
    miriam = studentManagementService.createStudent(miriam);
    programacion = studentManagementService.createModule(programacion);
    studentManagementService.enrollStudentInModule(miriam.getId(), programacion.getId());
    studentRepository.delete(miriam.getId());
    studentManagementService.getEnrollmentCount(miriam.getId());

}
```

## 4. Conclusión personal

- Se ha usado JDBC puro con Connection, PreparedStatement y ResultSet.
- Se ha implementado operaciones CRUD y control manual de transacciones (begin/commit/rollback).
- Se han utilizado consultas parametrizadas para evitar SQL injection. (Con preparedStatement)
- Se han ejecutado funciones almacenadas desde Java (count_enrollments) a partir de CallableStatement.
- El proyecto se ha integrado con Spring Boot y PostgreSQL en Docker, usando Maven como gestor de paquetes.
- Se ha aprendido a manejar excepciones SQL y a gestionar recursos con try-with-resources.
- Se ha mejorado la comprensión de la interacción entre Java y bases de datos relacionales.

Todos estos conocimientos nos han permitido entender mejor cómo funciona el acceso a datos en aplicaciones Java,
consiguiendo una base sólida que nos permita desarrollar proyectos más complejos de cara al futuro.

