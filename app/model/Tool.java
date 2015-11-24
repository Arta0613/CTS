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

    @Constraints.Required
    public String description;

    @Constraints.Required
    @ManyToOne
    public User owner;

    @Constraints.Required
    @ManyToOne
    public ToolType toolType;

    @Constraints.Required
    @OneToMany(mappedBy = "tool")
    public List<Comments> commentsList = new ArrayList<>();

    // A finder object for easier querying
    public static Finder<Long, Tool> find = new Finder<>(Tool.class);

    public static Tool createNewTool(String name, String toolTypeName, String description, User owner) {
        Tool tool = new Tool();
        tool.name = name;
        ToolType toolType = new ToolType();
        toolType.name = toolTypeName;
        tool.toolType = toolType;
        tool.description = description;
        tool.owner = owner;

        return tool;

    }
}
