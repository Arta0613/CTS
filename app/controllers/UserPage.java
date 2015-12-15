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
        List<model.Tool> tools = model.Tool.find.all();
        if (session().containsKey("user_id") && session().get("user_id").equals(user.id.toString()))
            return ok(views.html.users.index.render(user, tools, user.transactionList));
        return redirect(routes.Application.index());
    }

    public Result addTrans() {
        List<model.Transaction> trans = Transaction.find.all();
        Form<Transaction> transactionForm = form(model.Transaction.class).bindFromRequest();
        model.Transaction transaction = transactionForm.get();
        String t_id = transactionForm.data().get("tool_id");
        model.User u = User.find.byId(Long.parseLong(session().get("user_id")));
        model.Tool t = Tool.find.byId(Long.parseLong(t_id));
        Long tool_id = t.id;

        if(u.id.equals(t.owner.id)){
            flash("error", "You can not borrow your own tools");
            }else{
            t = null;
            model.Transaction currentTrans = null;

            if (trans == null) {
                transaction.to_borrow = Tool.find.byId(Long.parseLong(t_id));
                transaction.renter = User.find.byId(Long.parseLong(session().get("user_id")));
                transaction.Available = false;
                transaction.save();
                flash("success", "You have successfully borrowed: " + transaction.to_borrow.name);
            } else {
                for (model.Transaction c : trans) {
                    if (c.to_borrow != null && c.to_borrow.id.equals(tool_id)) {
                        t = c.to_borrow;
                        currentTrans = c;
                    }
                }
                if (t != null) {
                    if (currentTrans.Available) {
                        transaction.to_borrow = t;
                        transaction.renter = User.find.byId(Long.parseLong(session().get("user_id")));
                        transaction.Available = false;
                        transaction.save();
                        flash("success", "You have successfully borrowed: " + transaction.to_borrow.name);
                    } else {
                        flash("error", "This tool is already borrowed. ");
                    }
                } else {
                    transaction.to_borrow = Tool.find.byId(tool_id);
                    transaction.renter = User.find.byId(Long.parseLong(session().get("user_id")));
                    transaction.Available = false;
                    transaction.save();
                    flash("success", "You have successfully borrowed: " + transaction.to_borrow.name);

                }
            }
        }
            return redirect(routes.Tools.show(tool_id));
    }

    public Result returnTool() {

        List<model.Transaction> trans = Transaction.find.all();
        Form<Transaction> transactionForm = form(model.Transaction.class).bindFromRequest();
        model.Transaction transaction = transactionForm.get();
        String t_id = transactionForm.data().get("tool_id");
        model.User u = User.find.byId(Long.parseLong(session().get("user_id")));
        model.Tool t = Tool.find.byId(Long.parseLong(t_id));
        Long tool_id = t.id;

        for (model.Transaction tr: trans) {
            if (u.id == tr.renter.id){
                transaction.to_borrow = Tool.find.byId(tool_id);
                transaction.renter = null;
                transaction.Available = true;
                transaction.save();
            }else{}
        }
        return redirect(routes.UserPage.index(u.id));
    }

}
