-- tabla extra para acceso 

CREATE TABLE usuarios (
    correo VARCHAR(100) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (correo)
);

-- 1. Crear tabla cliente
CREATE TABLE cliente (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

-- 2. Crear tabla producto
CREATE TABLE producto (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    tipoProducto VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

-- 3. Crear tabla sucursal
CREATE TABLE sucursal (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

-- 4. Crear tabla inscripcion (Relación Muchos a Muchos entre Cliente y Producto)
CREATE TABLE inscripcion (
    idProducto INT NOT NULL,
    idCliente INT NOT NULL,
    PRIMARY KEY (idProducto, idCliente),
    FOREIGN KEY (idProducto) REFERENCES producto(id) ON DELETE CASCADE,
    FOREIGN KEY (idCliente) REFERENCES cliente(id) ON DELETE CASCADE
);

-- 5. Crear tabla disponibilidad (Relación Muchos a Muchos entre Sucursal y Producto)
CREATE TABLE disponibilidad (
    idSucursal INT NOT NULL,
    idProducto INT NOT NULL,
    PRIMARY KEY (idSucursal, idProducto),
    FOREIGN KEY (idSucursal) REFERENCES sucursal(id) ON DELETE CASCADE,
    FOREIGN KEY (idProducto) REFERENCES producto(id) ON DELETE CASCADE
);

-- 6. Crear tabla visitan (Relación Muchos a Muchos con atributo adicional)
CREATE TABLE visitan (
    idSucursal INT NOT NULL,
    idCliente INT NOT NULL,
    fechaVisita DATE NOT NULL,
    PRIMARY KEY (idSucursal, idCliente, fechaVisita), -- Incluimos fecha por si visita varias veces
    FOREIGN KEY (idSucursal) REFERENCES sucursal(id) ON DELETE CASCADE,
    FOREIGN KEY (idCliente) REFERENCES cliente(id) ON DELETE CASCADE
);