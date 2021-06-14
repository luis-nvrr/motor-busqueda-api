package com.dlc.motor_busqueda_dlc_api.Aplicacion;

import com.dlc.motor_busqueda_dlc_api.Dominio.*;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.DirectorioLocal;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IDirectorio;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLDocumentoRepository;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLPosteoRepository;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLTerminoRepository;

public class GestorIndexacionDirectorio {
    private Indexador indexador;
    private Vocabulario vocabulario;
    private DocumentoRepository documentoRepository;
    private TerminoRepository terminoRepository;
    private PosteoRepository posteoRepository;


    public GestorIndexacionDirectorio(){
        this.vocabulario = new Vocabulario();
        this.indexador = new Indexador(vocabulario);
        this.documentoRepository = new MySQLDocumentoRepository();
        this.terminoRepository = new MySQLTerminoRepository();
        this.posteoRepository = new MySQLPosteoRepository();
    }


    public void cargarVocabulario(String directorioPath){
        IDirectorio directorio = new DirectorioLocal(directorioPath);
        this.indexador.cargarVocabularioDeDirectorio(directorio);
        persistirBulk();
    }

    private void persistirBulk(){
        vocabulario.ordenarTerminos();
        vocabulario.ordenarDocumentos();
        vocabulario.ordenarPosteos();
        vocabulario.bulkSaveDocumentos(documentoRepository);
        vocabulario.bulkSaveTerminos(terminoRepository);
        vocabulario.bulkSavePosteos(posteoRepository);
    }

    public int mostrarCantidadTerminosVocabulario(){
        return vocabulario.cantidadTerminos();
    }
}
