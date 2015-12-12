package model;

import com.avaje.ebean.Model;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arman on 11/18/15.
 */

@Table(name = "users")
@Entity
public class User extends Model {

    @Id
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String username;

    @Constraints.Email
    @Column(unique = true)
    public String email;

    public String password_hash;

    @OneToMany(mappedBy = "owner")
    public List<Tool> toolsList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    public List<Comment> commentsList = new ArrayList<>();

    @OneToMany(mappedBy = "renter")
    public List<Transaction> transactionList = new ArrayList<>();

    // A finder object for easier querying
    public static Finder<Long, model.User> find = new Finder<>(model.User.class);

    // NOT FOR PRODUCTION - must ensure this is a valid user first. I have not done that.
    public boolean authenticate(String password) {
        return BCrypt.checkpw(password, this.password_hash);
    }

    public static User createNewUser(String username, String password, String email) {
        if (password == null || username == null || password.length() < 8) {
            return null;
        }

        // Create a password hash
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.username = username;
        user.password_hash = passwordHash;
        user.email = email;


        return user;
    }
}
