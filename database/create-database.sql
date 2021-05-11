CREATE DATABASE `motor-busqueda-dlc`;
USE `motor-busqueda-dlc`;

CREATE TABLE Terminos(
    termino VARCHAR(100),
    cantidadDocumentos INTEGER,
    maximaFrecuenciaTermino INTEGER,
    CONSTRAINT pk_terminos PRIMARY KEY Terminos(termino)
);

CREATE TABLE Documentos(
    nombre VARCHAR(20),
    path VARCHAR(200),
    CONSTRAINT pk_documentos PRIMARY KEY Documentos(nombre)
);

CREATE TABLE Posteos(
    termino VARCHAR(100),
    nombre VARCHAR(20),
    frecuenciaTermino INTEGER,
    CONSTRAINT pk_posteos PRIMARY KEY Posteos(termino,nombre)
);


