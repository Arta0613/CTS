package model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Created by Arman on 11/24/15.
 */

@Entity
public class Comments extends Model {

    @Id
    public Long id;

    public String body;

    @ManyToOne
    public User user;

    @ManyToOne
    public Tool tool;


}
