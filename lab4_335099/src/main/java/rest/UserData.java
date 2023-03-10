package rest;

import com.google.gson.Gson;
import com.password4j.Hash;
import entities.Message;
import entities.User;
import entities.UserRequest;
import register.LoginEnum;
import register.LoginException;
import repositories.UsersTable;
import com.password4j.Password;
import util.Transformation;


import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;


@Path("/login")
public class UserData {

    @EJB
    UsersTable table;

    Message m = new Message();

    @GET
    public Response checkToken(@QueryParam("token") String s){
        Message m = new Message();
        if (table.find("token", s).size() > 0){
            m.setMessage("ok");
        }
        else {
            m.setMessage("error");
        }
        return corsResults(Response.ok()).entity(new Gson().toJson(m)).build();

    }

    @DELETE
    public Response logout(@CookieParam("token") Cookie cookie) {
        table.find("token", cookie.getValue()).forEach(user -> {user.setToken(null);table.update(user);});
        return corsResults(Response.ok()).build();
    }

    private String randomToken(){
        return Transformation.stringRandom(30);
    }

    private LoginEnum process(String user, String pass, boolean reg, String c) {
        System.out.println(user + pass + reg);
        if (!validate(user, pass)) {
            return LoginEnum.WRONG_USER_OR_PASSWORD;
        }
        System.out.println(user + pass + reg);
        if ((table.find("token", c).size() != 0) && (!c.equals(null)) && (c != null)) {
            return LoginEnum.IS_AUTH;
        }
        System.out.println(user + pass + reg);
        if (reg){
            System.out.println(user + pass + reg);
            return register(user, pass);

        }

        else{System.out.println(user + pass + reg);

            return login(user, pass);
        }
    }

    private LoginEnum login(String username, String pass) {
        User user = table.findOne(username);
        if (user == null){
            m.setMessage("Wrong username!");
            return LoginEnum.WRONG_USERNAME;
        }
        System.out.println(pass + " " + user.getHash() + " " + user.getPlus());
        boolean checked = Password.check(pass, user.getHash()).addSalt(user.getPlus()).withArgon2();
        if (!checked){
            m.setMessage("Wrong password!");
            return LoginEnum.WRONG_PASSWORD;
        }
        m.setMessage("Success");
        return LoginEnum.SUCCESS;
    }

    private LoginEnum register(String username, String pass){
        User user = table.findOne(username);
        if (user != null){
            m.setMessage("Username already exists!");
            return LoginEnum.USERNAME_EXISTS;
        }
        Hash hash = Password.hash(pass).addRandomSalt(5).withArgon2();
        User add = new User(username, hash.getSalt(), hash.getResult(), null);
        table.save(add);
        m.setMessage("Success");
        return LoginEnum.SUCCESS;
    }

    private boolean validate(String user, String pass) {
        return (user != null) && (pass != null) && (user.matches("[a-zA-Z0-9]{6,}") && pass.matches("[!-~]{6,}"));
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
    public Response authentication(UserRequest user1) {
        try {
            LoginEnum status = process(user1.username, user1.password,
                    user1.registration, "");
            System.out.println(status);
            if (status != LoginEnum.SUCCESS){
                throw new LoginException(status);
            }
            User user = table.findOne(user1.username);
            String token = randomToken();
            user.setToken(token);
            table.update(user);
            m.setToken(user.getToken());
            return corsResults(Response.ok()).cookie().entity(new Gson().toJson(m)).build();
        } catch (Exception ex) {
            m.setToken("");
            return corsResults(Response.status(Response.Status.FORBIDDEN))
                    .header("user", user1.username).header("password", user1.password)
                    .header("regis", user1.registration).entity(new Gson().toJson(m)).build();
        }
    }

    @OPTIONS
    public Response options(){
        return corsResults(Response.status(204)).build();
    }
}
