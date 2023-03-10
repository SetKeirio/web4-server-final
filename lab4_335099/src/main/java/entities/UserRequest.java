package entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserRequest {
    public String username;
    public String password;
    public Boolean registration;
}
