package rest;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class Lab4_335099 extends Application {

    /*@GET
    public JsonArray antihype(){
        return Json.createArrayBuilder().add(hype("Замай", 36)).add(hype("Слава", 32)).build();
    }

    public JsonObject hype(String name, int age){
        return Json.createObjectBuilder().add("name", name).add("age", age).build();
    }*/
}
