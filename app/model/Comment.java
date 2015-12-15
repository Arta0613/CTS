package model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends Model {

    @Id
    public Long id;

    public String body;

    @ManyToOne
    public User user;

    @ManyToOne
    public Tool tool;

    public static Finder<Long, Comment> find = new Finder<>(Comment.class);

    /*public static Comment createNewComment(String name, String toolTypeName, String description, User owner) {
        Tools tool = new Tools();
        tool.name = name;
        ToolType toolType = new ToolType();
        toolType.name = toolTypeName;
        tool.toolType = toolType;
        tool.description = description;
        tool.owner = owner;

        return tool;

    }*/
}
