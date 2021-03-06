package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.Map;

public interface DocumentoRepository {

    Map<String, Documento> getAllDocumentos();
    Documento getDocumento(String documento);
    void saveDocumentos(Map<String, Documento> documentos);
    void bulkSaveDocumentos(Map<String, Documento> documentos);
}
