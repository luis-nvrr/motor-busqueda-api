package com.dlc.motor_busqueda_dlc_api.Aplicacion;
import com.dlc.motor_busqueda_dlc_api.Dominio.*;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.ArchivoLocal;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.DirectorioLocal;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IArchivo;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLDocumentoRepository;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLPosteoRepository;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLTerminoRepository;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IDirectorio;

public class GestorIndexacion {

    Indexador indexador;
    Vocabulario vocabulario;
    StopWord stopWord;
    TerminoRepository terminoRepository;
    PosteoRepository posteoRepository;
    DocumentoRepository documentoRepository;

    public GestorIndexacion(){
        this.vocabulario = new Vocabulario();
        this.stopWord = new StopWord();
        this.indexador = new Indexador(vocabulario, stopWord);
        this.terminoRepository = new MySQLTerminoRepository();
        this.posteoRepository = new MySQLPosteoRepository();
        this.documentoRepository = new MySQLDocumentoRepository();
    }

    public void cargarStopWords(String archivoPath){
        IArchivo archivo = new ArchivoLocal(archivoPath);
        indexador.cargarStopWords(archivo);
    }

    public void cargarVocabularioArchivo(String archivoPath){
        IArchivo archivo = new ArchivoLocal(archivoPath);
        indexador.cargarVocabularioArchivo(archivo);
        persistir();
    }

    public void cargarVocabularioDirectorio(String directorioPath){
        IDirectorio directorio = new DirectorioLocal(directorioPath);
        indexador.cargarVocabularioDirectorio(directorio);
        persistirBulk();
    }

    public String mostrarVocabulario(){
        return vocabulario.mostrarTerminos();
    }

    public int mostrarCantidadTerminosVocabulario(){
        return vocabulario.cantidadTerminos();
    }

    public String mostrarOrdenPosteo(){
        return vocabulario.mostrarOrdenPosteo();
    }

    private void persistir(){
        vocabulario.saveDocumentos(documentoRepository);
        vocabulario.saveTerminos(terminoRepository);
        vocabulario.savePosteos(posteoRepository);
    }

    private void persistirBulk(){
        vocabulario.bulkSaveDocumentos(documentoRepository);
        vocabulario.bulkSaveTerminos(terminoRepository);
        vocabulario.bulkSavePosteos(posteoRepository);
    }
}
