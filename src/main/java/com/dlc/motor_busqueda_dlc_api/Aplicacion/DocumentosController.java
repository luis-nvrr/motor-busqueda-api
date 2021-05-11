package com.dlc.motor_busqueda_dlc_api.Aplicacion;

import com.dlc.motor_busqueda_dlc_api.Dominio.DocumentoNoEncontradoException;
import com.dlc.motor_busqueda_dlc_api.Dominio.DocumentoRecuperado;
import com.dlc.motor_busqueda_dlc_api.Dominio.TerminoNoEncontradoException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;
import java.util.logging.Logger;

@Path("/")
public class DocumentosController {

    @Inject
    private GestorBusqueda gestorBusqueda;
    private static Logger logger = Logger.getLogger("global");

    @Path("download/{documento}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getDocumentoFile(@PathParam("documento") String documento){
        try {
            File file = new File(gestorBusqueda.buscarPathDocumento(documento));
            return Response
                    .ok(file, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                    .build();
        } catch (DocumentoNoEncontradoException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }

    }

    @Path("documentos/{documento}")
    @GET
    public Response getDocumentoText(@PathParam("documento") String documento){
        try {
            JSONObject obj = new JSONObject();
            obj.put("nombre", documento);
            obj.put("texto", gestorBusqueda.buscarDocumento(documento));

            return Response
                    .status(Response.Status.OK)
                    .entity(obj.toJSONString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (DocumentoNoEncontradoException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }

    }

    @Path("documentos")
    @GET
    public Response getDocumentos(){
        JSONArray list = new JSONArray();
        JSONObject obj = new JSONObject();

        String[] nombresDocumentos = gestorBusqueda.buscarNombresDocumentos();
        for(String nombre : nombresDocumentos){
            list.add(nombre);
        }

        obj.put("documentos", list);

        return Response
                .status(Response.Status.OK)
                .entity(obj.toJSONString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }


    @Path("terminos/{termino}")
    @GET
    public Response getDocumentosByTermino(@PathParam("termino") String termino){

        try{
            gestorBusqueda.buscarTerminos(termino);
            List<DocumentoRecuperado> documentoRecuperados = gestorBusqueda.getDocumentosRecuperados();

            JSONArray list = new JSONArray();

            for(DocumentoRecuperado documentoRecuperado: documentoRecuperados){
                JSONObject obj = new JSONObject();
                obj.put("nombre", documentoRecuperado.getNombre());
                obj.put("indice", documentoRecuperado.getIndiceRelevancia());
                obj.put("ubicacion", documentoRecuperado.getPath());
                list.add(obj);
            }

            return Response
                    .status(Response.Status.OK)
                    .entity(list.toJSONString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        catch(final TerminoNoEncontradoException e){
            JSONArray emptyList = new JSONArray();
            return Response
                    .status(Response.Status.OK)
                    .entity(emptyList.toJSONString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        }
        catch(final Exception e){
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @POST
    @Path("/documentos")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(final MimeMultipart file) {
        if (file == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("nombre de archivo no válido").build();

        try {
            BodyPart bodyPart = file.getBodyPart(0);
            String nombreDocumento = bodyPart.getFileName();
            String documentoPath = System.getenv("DIRECTORIO_DOCUMENTOS") + nombreDocumento;

            bodyPart.removeHeader("Content-Disposition");
            bodyPart.removeHeader("Content-Type");
            bodyPart.writeTo(new FileOutputStream(documentoPath));

            GestorIndexacion gestorIndexacion = new GestorIndexacion(gestorBusqueda);
            gestorIndexacion.cargarVocabularioDeArchivo(documentoPath);

            return Response.ok("carga correcta").build();

        } catch (final Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}