INSERT INTO alumno (nombre, email)
VALUES ('Laura Pérez', 'laura@centroeducativo.es'),
       ('Carlos Ruiz', 'carlos@centroeducativo.es'),
       ('Ana Torres', 'ana@centroeducativo.es');
INSERT INTO modulo (nombre, horas)
VALUES ('Programación', 120),
       ('Bases de Datos', 100),
       ('Entornos de Desarrollo', 90);
INSERT INTO matricula (id_alumno, id_modulo, fecha)
VALUES (1, 1, '2025-10-01'),
       (1, 2, '2025-10-02'),
       (2, 3, '2025-10-03'),
       (3, 1, '2025-10-04');

-- UPDATE: change module hours
UPDATE modulo
SET horas = 110
WHERE nombre = 'Bases de Datos';
-- DELETE: remove one enrollment
DELETE
FROM matricula
WHERE id_alumno = 3
  AND id_modulo = 1;