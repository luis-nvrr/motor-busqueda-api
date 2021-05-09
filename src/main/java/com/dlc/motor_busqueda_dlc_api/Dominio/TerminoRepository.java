package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.List;
import java.util.Map;

public interface TerminoRepository {

    Map<String, Termino> getAllTerminos();
    List<Termino> getTerminos(String[] terminosString);
    void saveTerminos(Map<String, Termino> terminos);
    void bulkSaveTerminos(Map<String, Termino> terminos);
}
