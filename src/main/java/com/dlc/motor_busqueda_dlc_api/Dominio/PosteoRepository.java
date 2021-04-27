package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.List;
import java.util.Map;

public interface PosteoRepository {

    Posteo getPosteo();
    List<Posteo> getAllPosteos(String termino, Map<String, Documento> documentos);
    void savePosteo(Posteo posteo);
    void savePosteos(Map<String, Termino> terminos);
}
