package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.Map;

public interface TerminoRepository {

    Termino getTermino(String termino);
    Map<String, Termino> getAllTerminos();
    void saveTermino(Termino termino);
    void saveTerminos(Map<String, Termino> terminos);
}
