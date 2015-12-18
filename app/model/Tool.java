package model;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

@Entity
public class Tool extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    public String image;

    @ManyToOne
    public User owner;

    @ManyToOne
    public ToolType toolType;

    @OneToMany(mappedBy = "tool", cascade = CascadeType.ALL)
    public List<Comment> commentList;

    @OneToMany(mappedBy = "to_borrow")
    public List<Transaction> transactionList;

    // A finder object for easier querying
    public static Finder<Long, Tool> find = new Finder<>(Tool.class);

}