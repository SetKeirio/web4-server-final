package rest;


import com.google.gson.Gson;
import entities.Result;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import entities.ResultRequest;
import repositories.ResultsTable;
import util.Transformation;

import javax.ejb.EJB;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Path("/results")
public class ResultsData {

    @EJB
    ResultsTable table;

    @DELETE
    public Response clear() {
        table.clear();
        return corsResults(Response.ok()).build();
    }

    private Response.ResponseBuilder corsResults(Response.ResponseBuilder update){
        return update.header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Allow-Credentials", "true").
                header("Access-Control-Allow-Headers",
                        "Origin, X-Api-Key, X-Requested-With, Content-Type, Accept, Authorization").
                header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response addResult(ResultRequest resultRequest) {
        try {

            double x = Transformation.roundDouble(resultRequest.x);
            double y = Transformation.roundDouble(resultRequest.y);
            double r = Transformation.roundDouble(resultRequest.r);
            boolean click = resultRequest.fromCanvas;
            boolean normal = Transformation.validate(x, y, r, click);
            if (!normal) {
                throw new UnsupportedOperationException();
            }
            Result add = new Result();
            add.setX(x);
            add.setY(y);
            add.setR(r);
            boolean inArea = Transformation.checkInArea(x, y, r);
            add.setInarea(inArea);
            table.save(add);
            return corsResults(Response.ok()).entity(new Gson().toJson(add)).build();

        } catch (Exception e) {
            Map<String, String> answer = new HashMap<String, String>();
            double x = Transformation.roundDouble(resultRequest.x);
            double y = Transformation.roundDouble(resultRequest.y);
            double r = Transformation.roundDouble(resultRequest.r);
            answer.put("answer", "check request data!" + x + y + r);
            return corsResults(Response.serverError()).entity(new Gson().toJson(answer)).build();
        }
    }

        @GET
        public Response getResults(){

            //table.clear();
            //return Response.ok().build();
            return corsResults(Response.ok()).
                    entity(new Gson().toJson(table.get())).build();
        }

        @OPTIONS
    public Response options(){
        return corsResults(Response.status(204)).build();
        }
}
