package com.dlc.motor_busqueda_dlc_api.Aplicacion;

import com.dlc.motor_busqueda_dlc_api.Aplicacion.GestorBusqueda;
import com.dlc.motor_busqueda_dlc_api.Dominio.DocumentoRecuperado;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

@Path("/")
public class DocumentosController {

    @Inject
    private GestorBusqueda gestorBusqueda;

    @Path("documento/{documento}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getDocumentoByNombre(@PathParam("documento") String documento){
        File file = new File(gestorBusqueda.buscarPathDocumento(documento));
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .build();
    }

    @Path("documentos/{termino}")
    @GET
    public Response getDocumentosByTermino(@PathParam("termino") String termino){

        gestorBusqueda.buscar(termino);
        List<DocumentoRecuperado> documentoRecuperados = gestorBusqueda.getDocumentosRecuperados();

        JSONArray list = new JSONArray();

        for(DocumentoRecuperado documentoRecuperado: documentoRecuperados){
            JSONObject obj = new JSONObject();
            obj.put("nombre", documentoRecuperado.getNombre());
            obj.put("indice", documentoRecuperado.getIndiceRelevancia());
            obj.put("texto", documentoRecuperado.getTexto());
            list.add(obj);
        }

        return Response
                .status(Response.Status.OK)
                .entity(list.toJSONString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}