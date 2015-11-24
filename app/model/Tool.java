package model;

import com.avaje.ebean.Model;
import play.api.i18n.Messages;
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

    public String description;

    @ManyToOne
    public User owner;

    @ManyToOne
    public ToolType toolType;

    @OneToMany(mappedBy = "tool")
    public List<Comments> commentsList = new ArrayList<>();

    // A finder object for easier querying
    public static Finder<Long, Tool> find = new Finder<>(Tool.class);
}
