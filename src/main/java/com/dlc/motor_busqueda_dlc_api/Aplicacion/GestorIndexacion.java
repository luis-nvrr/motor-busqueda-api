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
    TerminoRepository terminoRepository;
    PosteoRepository posteoRepository;
    DocumentoRepository documentoRepository;
    GestorBusqueda gestorBusqueda;

    public GestorIndexacion(GestorBusqueda gestorBusqueda){
        this.gestorBusqueda = gestorBusqueda;
        this.vocabulario = new Vocabulario();
        this.indexador = new Indexador(vocabulario);
        this.terminoRepository = new MySQLTerminoRepository();
        this.posteoRepository = new MySQLPosteoRepository();
        this.documentoRepository = new MySQLDocumentoRepository();
    }

    public GestorIndexacion(){
        this(null);
    }

    public void cargarVocabularioDeArchivo(String archivoPath){
        IArchivo archivo = new ArchivoLocal(archivoPath);
        this.indexador.cargarVocabularioDeArchivo(archivo);
        persistir();
        actualizarVocabulario();
    }

    public void cargarVocabularioDeDirectorio(String directorioPath){
        IDirectorio directorio = new DirectorioLocal(directorioPath);
        this.indexador.cargarVocabularioDeDirectorio(directorio);
        persistirBulk();
    }

    private void actualizarVocabulario(){
        String documento = vocabulario.obtenerNombreDelUltimoDocumento();
        String[] terminos = vocabulario.obtenerUltimosTerminosString();
        this.gestorBusqueda.actualizarVocabularioLocal(documento, terminos);
    }

    private void persistir(){
        vocabulario.saveDocumentos(documentoRepository);
        vocabulario.saveTerminos(terminoRepository);
        vocabulario.savePosteos(posteoRepository);
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

    /*public String mostrarVocabulario(){
        return vocabulario.mostrarTerminos();
    }

    public String mostrarOrdenPosteo(){
        return vocabulario.mostrarOrdenPosteo();
    }*/
}
