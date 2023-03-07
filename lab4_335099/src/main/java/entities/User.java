package entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    String username;
    String plus;
    String hash;
    String token;

    public User(String username, String plus, String hash, String token) {
        this.username = username;
        this.plus = plus;
        this.hash = hash;
        this.token = token;
    }
}
