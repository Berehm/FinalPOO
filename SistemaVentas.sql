-- BASE DE DATOS: SISTEMA DE VENTAS
-- Motor: SQL SERVER
-- =============================================

-- Crear la base de datos
CREATE DATABASE SistemaVentas;
GO

USE SistemaVentas;
GO

-- =============================================
-- 1. TABLA: usuarios
-- =============================================
CREATE TABLE usuarios (
    id INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    pass VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('Administrador', 'Cajero'))
);
GO

-- =============================================
-- 2. TABLA: clientes
-- =============================================
CREATE TABLE clientes (
    id INT PRIMARY KEY IDENTITY(1,1),
    dni VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    direccion VARCHAR(200)
);
GO

-- =============================================
-- 3. TABLA: proveedor
-- =============================================
CREATE TABLE proveedor (
    id INT PRIMARY KEY IDENTITY(1,1),
    ruc VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    direccion VARCHAR(200)
);
GO

-- =============================================
-- 4. TABLA: productos
-- =============================================
CREATE TABLE productos (
    id INT PRIMARY KEY IDENTITY(1,1),
    codigo VARCHAR(50) NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    proveedor INT NOT NULL,
    stock INT DEFAULT 0,
    precio DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (proveedor) REFERENCES proveedor(id)
);
GO

-- =============================================
-- 5. TABLA: ventas
-- =============================================
CREATE TABLE ventas (
    id INT PRIMARY KEY IDENTITY(1,1),
    cliente INT NOT NULL,
    id_usuario INT NOT NULL,
    vendedor VARCHAR(100) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (cliente) REFERENCES clientes(id),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
GO

-- =============================================
-- 6. TABLA: detalle
-- =============================================
CREATE TABLE detalle (
    id INT PRIMARY KEY IDENTITY(1,1),
    id_pro INT NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    id_venta INT NOT NULL,
    FOREIGN KEY (id_pro) REFERENCES productos(id),
    FOREIGN KEY (id_venta) REFERENCES ventas(id) ON DELETE CASCADE
);
GO

-- =============================================
-- 7. TABLA: config
-- =============================================
CREATE TABLE config (
    id INT PRIMARY KEY IDENTITY(1,1),
    ruc VARCHAR(20) NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    telefono VARCHAR(15),
    direccion VARCHAR(200),
    mensaje VARCHAR(500)
);
GO

-- =============================================
-- DATOS INICIALES
-- =============================================

-- Insertar usuario administrador por defecto
INSERT INTO usuarios (nombre, correo, pass, rol) 
VALUES ('Administrador', 'admin@sistema.com', 'admin123', 'Administrador');
GO

-- Insertar cajero de ejemplo
INSERT INTO usuarios (nombre, correo, pass, rol) 
VALUES ('Juan Pérez', 'cajero@sistema.com', 'cajero123', 'Cajero');
GO

-- Insertar configuración de empresa
INSERT INTO config (ruc, nombre, telefono, direccion, mensaje) 
VALUES ('20123456789', 'Mi Empresa SAC', '987654321', 'Av. Principal 123', 'Gracias por su compra');
GO

-- =============================================
-- CONSULTAS ÚTILES PARA VERIFICAR
-- =============================================

-- Ver todas las tablas creadas
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE';
GO

-- Ver usuarios registrados
SELECT * FROM usuarios;
GO

-- Ver configuración
SELECT * FROM config;
GO

SELECT * FROM proveedor;
GO

SELECT * FROM productos;
GO
-- Registrar venta
CREATE PROCEDURE RegistrarVenta
    @cliente INT,
    @id_usuario INT,
    @vendedor VARCHAR(100),
    @total DECIMAL(10,2),
    @fecha DATE
AS
BEGIN

    INSERT INTO ventas (cliente, id_usuario, vendedor, total, fecha)
    VALUES (@cliente, @id_usuario, @vendedor, @total, @fecha);
END;


-- Registrar detalle de venta
CREATE PROCEDURE RegistrarDetalle
    @id_pro INT,
    @cantidad INT,
    @precio DECIMAL(10,2),
    @id_venta INT
AS
BEGIN
    INSERT INTO detalle (id_pro, cantidad, precio, id_venta)
    VALUES (@id_pro, @cantidad, @precio, @id_venta);
END;


-- Actualizar stock por venta
CREATE PROCEDURE ActualizarStock
    @id_pro INT,
    @cantidad INT
AS
BEGIN
    UPDATE productos
    SET stock = stock - @cantidad
    WHERE id = @id_pro;
END;


------------------------------
-- 3.5.2 FUNCIONES
------------------------------

-- Obtener stock de un producto
CREATE FUNCTION ObtenerStock (@id_pro INT)
RETURNS INT
AS
BEGIN
    DECLARE @resultado INT;

    SELECT @resultado = stock 
    FROM productos 
    WHERE id = @id_pro;

    RETURN @resultado;
END;


-- Obtener total calculado de una venta
CREATE FUNCTION TotalVenta (@id_venta INT)
RETURNS DECIMAL(10,2)
AS
BEGIN
    DECLARE @total DECIMAL(10,2);

    SELECT @total = SUM(cantidad * precio)
    FROM detalle
    WHERE id_venta = @id_venta;

    RETURN @total;
END;


------------------------------
-- 3.5.3 TRIGGERS
------------------------------
USE SistemaVentas;
GO
INSERT INTO clientes (dni, nombre, telefono, direccion)
VALUES ('12345678', 'Cliente Prueba', '999999999', 'Lima');

INSERT INTO ventas (cliente, id_usuario, vendedor, total, fecha)
VALUES (1003, 1, 'Vendedor Prueba', 150.50, GETDATE());

INSERT INTO detalle (id_pro, cantidad, precio, id_venta)
VALUES (1, 2, 5.50);
---------------------------
-- Obtener total calculado de una venta
CREATE FUNCTION TotalVenta (@id_venta INT)
RETURNS DECIMAL(10,2)
AS
BEGIN
    DECLARE @total DECIMAL(10,2);

    SELECT @total = SUM(cantidad * precio)
    FROM detalle
    WHERE id_venta = @id_venta;

    RETURN @total;
END;

-- 3.5.3 TRIGGERS
------------------------------

-- Trigger para disminuir stock automáticamente al registrar un detalle
CREATE TRIGGER trg_ActualizarStockVenta
ON detalle
AFTER INSERT
AS
BEGIN
    UPDATE productos
    SET productos.stock = productos.stock - i.cantidad
    FROM productos
    INNER JOIN inserted i ON productos.id = i.id_pro;
END;