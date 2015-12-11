package controllers;


import model.Comment;
import model.Tool;
import model.Transaction;
import model.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import sun.rmi.runtime.Log;
import views.html.index;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

/**
 * Created by Arman on 11/18/15.
 */
public class UserPage extends Controller {

    public Result index(Long id) {
        // Authenticated
        User user = User.find.byId(id);
        if (session().containsKey("user_id") && session().get("user_id").equals(user.id.toString()))
            return ok(views.html.users.index.render(user, user.toolsList));
        return redirect(routes.Application.index());
    }

    public Result addTrans() {

        Form<Transaction> transactionForm = form(model.Transaction.class).bindFromRequest();
        model.Transaction transaction = transactionForm.get();

            String t_id = transactionForm.data().get("tool_id");
            transaction.to_borrow = Tool.find.byId(Long.parseLong(t_id));
            transaction.renter = User.find.byId(Long.parseLong(session().get("user_id")));
            transaction.Available = false;
            transaction.save();
            flash("success", "You have successfully borrowed: " + transaction.to_borrow.name);
//
//        else{
//            flash("error", "This tool is already borrowed. ");
//        }
        return redirect(routes.Tools.show(transaction.to_borrow.id));
    }

//    public Result returnTool(Long id) {}

//    public Result addInvent(){
//
//    }
}
