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
    link VARCHAR(200),
    CONSTRAINT pk_documentos PRIMARY KEY Documentos(nombre)
);

CREATE TABLE Posteos(
    nombre VARCHAR(20),
    termino VARCHAR(100),
    frecuenciaTermino INTEGER,
    CONSTRAINT pk_posteos PRIMARY KEY Posteos(nombre, termino),
    CONSTRAINT fk_posteos_terminos FOREIGN KEY (termino) REFERENCES Terminos(termino),
    CONSTRAINT fk_posteos_documentos FOREIGN KEY (nombre) REFERENCES Documentos(nombre)
);


