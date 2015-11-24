package model;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Arman on 11/24/15.
 */
@Entity
public class ToolType extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @OneToMany
    public List<Tool> tools;

    // A finder object for easier querying
    public static Finder<Long, ToolType> find = new Finder<>(ToolType.class);
}
