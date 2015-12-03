package model;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arman on 11/24/15.
 */

@Entity
public class Tool extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;


    @ManyToOne
    public User owner;


    @ManyToOne
    public ToolType toolType;


    @OneToMany(mappedBy = "tool")
    public List<Comment> commentList;

    // A finder object for easier querying
    public static Finder<Long, model.Tool> find = new Finder<>(model.Tool.class);

    }

