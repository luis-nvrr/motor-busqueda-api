package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.Map;

public interface DocumentoRepository {
    void saveDocumentos(Map<String, Documento> documentos);
    Map<String, Documento> getAllDocumentos();
}
