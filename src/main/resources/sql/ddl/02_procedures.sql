CREATE OR REPLACE FUNCTION count_enrollments(student_id INT)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
total INT;
BEGIN
SELECT COUNT(*) INTO total
FROM enrollment
WHERE enrollment.student_id = student_id;

RETURN total;
END;
$$;
@@
