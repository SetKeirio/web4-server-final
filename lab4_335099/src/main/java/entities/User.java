package entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@FieldDefaults(level = AccessLevel.PRIVATE)
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

    public User() {

    }
}
