package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.Map;

public interface TerminoRepository {

    Map<String, Termino> getAllTerminos();
    void saveTerminos(Map<String, Termino> terminos);
    void bulkSaveTerminos(Map<String, Termino> terminos);
}
