INSERT IGNORE INTO roles (id, nombre, descripcion) VALUES (1, 'ADMINISTRADOR', 'Rol de administrador del sistema');
INSERT IGNORE INTO roles (id, nombre, descripcion) VALUES (2, 'PROPIETARIO', 'Rol de propietario de restaurante');
INSERT IGNORE INTO roles (id, nombre, descripcion) VALUES (3, 'EMPLEADO', 'Rol de empleado de restaurante');
INSERT IGNORE INTO roles (id, nombre, descripcion) VALUES (4, 'CLIENTE', 'Rol de cliente de la plazoleta');

INSERT IGNORE INTO usuarios (id, nombre, apellido, documento_de_identidad, celular, fecha_nacimiento, correo, clave, id_rol)
VALUES (99, 'Administrador', 'Plazoleta', '1000000000', '+573000000000', '1990-01-01', 'admin@plazoleta.com', '$2a$10$J7kD/15T2vWEwVgSbit42el9b.fXVdPvQAItYPwx8GEW1TYGE6MtO', 1);

