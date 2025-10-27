# AAD_25_26-Acceso-a-datos-

# 1. Qué es un conector y su papel en la aplicación.

- Un conector es un software que sirve de puente para conectar nuestra aplicación con una base de datos. Permite que la
  aplicación envíe consultas y reciba resultados de la base de datos de manera eficiente y segura.

# 2. Cómo has levantado el servicio PostgreSQL.

- Para levantar el servicio Postgres en Docker, puedes usar el siguiente comando: docker compose up -d (en la carpeta
  donde se encuentra el archivo docker-compose.yml).

# 3. DOCKER-COMPOSE.YML

![](img.png)

``` 
    version: '3.8'
    services:
      postgres:
        image: postgres:15
        container_name: postgres-db
        environment:
          POSTGRES_USER: admin
          POSTGRES_PASSWORD: admin
          POSTGRES_DB: prueba
        ports:
          - "5432:5432"
        volumes:
          - ./data:/var/lib/postgresql/data
```    

# 4. Cómo has conectado a la base de datos PostgreSQL.

- Para probar la conexión, creamos una nueva con un gestor como DBeaver o PgAdmin, utilizando los siguientes datos:

| HOST       | localhost |
|------------|-----------|
| PUERTO     | 5432      |
| USUARIO    | admin     |
| CONTRASEÑA | admin     |
| BBDD       | prueba    |

