CREATE OR REPLACE FUNCTION count_enrollments(p_id_alumno INT)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
total INT;
BEGIN
SELECT COUNT(*) INTO total
FROM matricula
WHERE matricula.id_alumno = p_id_alumno;

RETURN total;
END;
$$;
@@
