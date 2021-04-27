CREATE DATABASE `motor-busqueda-dlc`;
USE `motor-busqueda-dlc`;

CREATE TABLE Terminos(
    termino VARCHAR(100),
    cantidadDocumentos INTEGER,
    maximaFrecuenciaTermino INTEGER
);

CREATE TABLE Documentos(
    nombre VARCHAR(20),
    path VARCHAR(200),
    link VARCHAR(200)
);

CREATE TABLE Posteos(
    nombre VARCHAR(20),
    termino VARCHAR(100),
    frecuenciaTermino INTEGER
);


