package rest;

import com.password4j.Hash;
import entities.User;
import register.LoginEnum;
import register.LoginException;
import repositories.UsersTable;
import com.password4j.Password;
import util.Transformation;


import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;


@Path("/login")
public class UserData {

    @EJB
    UsersTable table;

    @DELETE
    public Response logout(@CookieParam("token") Cookie cookie) {
        table.find("token", cookie.getValue()).forEach(user -> {user.setToken(null);table.update(user);});
        return Response.ok().build();
    }

    private String randomToken(){
        return Transformation.stringRandom(30);
    }

    private LoginEnum process(String user, String pass, boolean reg, Cookie c) {
        if (!validate(user, pass)) {
            return LoginEnum.WRONG_USER_OR_PASSWORD;
        }
        if ((table.find("token", c.getValue()).size() != 0) && (c.getValue() != null) && (c != null)) {
            return LoginEnum.IS_AUTH;
        }
        if (reg){
            return register(user, pass);
        }
        else{
            return login(user, pass);
        }
    }

    private LoginEnum login(String username, String pass) {
        User user = table.findOne(username);
        if (user == null){
            return LoginEnum.WRONG_USERNAME;
        }
        boolean checked = Password.check(pass, user.getHash()).addSalt(user.getPlus()).withArgon2();
        if (!checked){
            return LoginEnum.WRONG_PASSWORD;
        }
        return LoginEnum.SUCCESS;
    }

    private LoginEnum register(String username, String pass){
        User user = table.findOne(username);
        if (user != null){
            return LoginEnum.USERNAME_EXISTS;
        }
        Hash hash = Password.hash(pass).addRandomSalt(5).withArgon2();
        User add = new User(username, hash.getResult(), hash.getSalt(), null);
        table.save(add);
        return LoginEnum.SUCCESS;
    }

    private boolean validate(String user, String pass) {
        return (user != null) && (pass != null) && (user.matches("[a-zA-Z0-9]{6,}") && pass.matches("[!-~]{6,}"));
    }

    @POST
    public Response authentication(@FormParam("username") String username,
            @FormParam("password") String pass,
            @FormParam("registration") String reg,
            @CookieParam("token") Cookie c) {
        try {
            LoginEnum status = process(username, pass,
                    Boolean.parseBoolean(reg), c);
            if (status != LoginEnum.SUCCESS){
                throw new LoginException(status);
            }
            User user = table.findOne(username);
            String token = randomToken();
            user.setToken(token);
            table.update(user);
            return Response.ok().cookie().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.FORBIDDEN).entity(ex.getMessage()).build();
        }
    }
}
