-- INSERTA EL PRIMERA USUARIO ADMINISTRADOR Y LA CLAVE ES 1234
INSERT INTO usuario (id,alias,alta, clave, email,fecha_alta,fecha_baja,rol) 
SELECT uuid(),'admin', TRUE, '$2a$10$3Lk7luP4o8oEzM5yMNP6F.pu1j/aliZGTo0YBXLDAFoCo4UG3Mwou', 'admin@email.com', CURDATE(), NULL, 'ADMIN' 
WHERE NOT EXISTS (SELECT * FROM usuario WHERE alias='admin') ^;

-- BORRA EL TRIGGER admin_delete SI YA EXISTE
DROP TRIGGER IF EXISTS admin_delete ^; 

-- CREA EL TRIGGER (DISPARADOR) QUE SE ACTIVA CUANDO ALGUIEN QUIERE BORRAR EL ADMIN DE LA TABLA USUARIO
CREATE TRIGGER admin_delete
BEFORE DELETE
ON usuario
FOR EACH ROW
BEGIN
  IF OLD.alias = 'admin' THEN -- Abort when trying to remove this record
    CALL cannot_delete_error;  -- raise an error to prevent deleting from the table
  END IF;
END ^;

-- BORRA EL TRIGGER admin_update SI YA EXISTE
DROP TRIGGER IF EXISTS admin_update ^; 

-- CREA EL TRIGGER (DISPARADOR) QUE SE ACTIVA CUANDO ALGUIEN QUIERE ACTUALIZA EL ALIAS AL ADMIN DE LA TABLA USUARIO
CREATE TRIGGER admin_update
BEFORE UPDATE
ON usuario
FOR EACH ROW
BEGIN
  IF OLD.alias = 'admin' THEN -- Abort when trying to remove this record
    CALL cannot_update_error;  -- raise an error to prevent updating from the table
  END IF;
END^;





 


