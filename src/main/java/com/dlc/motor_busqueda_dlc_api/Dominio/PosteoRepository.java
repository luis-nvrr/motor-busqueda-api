package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.List;
import java.util.Map;

public interface PosteoRepository {

    List<Posteo> getAllPosteosByTermino(String termino, Map<String, Documento> documentos) throws TerminoNoEncontradoException;
    void savePosteos(Map<String, Termino> terminos);
    void bulkSavePosteos(Map<String, Termino> terminos);
}
