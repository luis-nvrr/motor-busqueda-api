package com.dlc.motor_busqueda_dlc_api.Aplicacion;

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

        try{
            gestorBusqueda.buscar(termino);
            List<DocumentoRecuperado> documentoRecuperados = gestorBusqueda.getDocumentosRecuperados();

            JSONArray list = new JSONArray();

            for(DocumentoRecuperado documentoRecuperado: documentoRecuperados){
                JSONObject obj = new JSONObject();
                obj.put("nombre", documentoRecuperado.getNombre());
                obj.put("indice", documentoRecuperado.getIndiceRelevancia());
                obj.put("ubicacion", documentoRecuperado.getPath());
                obj.put("texto", documentoRecuperado.getTexto());
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
            return Response.status(Response.Status.OK)
                    .entity(emptyList.toJSONString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        }
        catch(final Exception e){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e)
                    .build();
        }

    }

    @POST
    @Path("/documento")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(final MimeMultipart file) {
        if (file == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Must supply a valid file").build();

        try {
            BodyPart bodyPart = file.getBodyPart(0);
            String documentoPath = "C:\\Users\\luis\\Downloads\\DLC\\"+ bodyPart.getFileName();
            bodyPart.writeTo(new FileOutputStream(documentoPath));

            GestorIndexacion gestorIndexacion = new GestorIndexacion();
            gestorIndexacion.cargarVocabularioArchivo(documentoPath);
            gestorBusqueda.recuperarVocabulario();

            return Response.ok("Done").build();

        } catch (final Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e)
                    .build();
        }
    }

}