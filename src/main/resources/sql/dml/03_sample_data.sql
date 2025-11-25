-- Example data. Avoid conflicts thanks to Postgresql ON CONFLICT
INSERT INTO alumno (nif, nombre, email) VALUES
    ('12345678A', 'Juan Pérez', 'juan.perez@example.com'),
    ('87654321B', 'María García', 'maria.garcia@example.com'),
    ('11223344C', 'Carlos López', 'carlos.lopez@example.com')
ON CONFLICT (nif) DO NOTHING;

INSERT INTO modulo (codigo, nombre, horas) VALUES
    ('0485', 'Programación', 250),
    ('0373', 'Lenguajes de Marcas', 140),
    ('0487', 'Entornos de Desarrollo', 90)
ON CONFLICT (codigo) DO NOTHING;
@@