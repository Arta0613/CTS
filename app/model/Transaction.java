package model;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.swing.*;
import javax.validation.Constraint;


/**
 * Created by dima on 12/10/15.
 */

@Entity
public class Transaction extends Model {

    @Id
    public Long id;

    @ManyToOne
    public User renter;

    @ManyToOne
    public Tool to_borrow;

    public Boolean Available = true;

    // A finder object for easier querying
    public static Finder<Long, model.Transaction> find = new Finder<>(model.Transaction.class);


}
