package register;

import entities.User;
import repositories.UsersTable;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class LoginFilter implements ContainerRequestFilter {

    @EJB
    UsersTable table;

    private boolean validate(String t){
        List<User> users = table.find("token", t);
        return (users.size() != 0) && (t != null);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            String token = requestContext.getCookies().get("token").getValue();
            if (!validate(token)) {
                throw new LoginException(LoginEnum.TOKEN_TROUBLES);
            }
        } catch (Exception ex) {
            requestContext.abortWith(Response.seeOther(URI.create("/login")).build());
        }
    }
}
