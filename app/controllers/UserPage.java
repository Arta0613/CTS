package controllers;


import model.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
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
        List<model.ToolType> toolTypes = ToolType.find.all();
        if (session().containsKey("user_id") && session().get("user_id").equals(user.id.toString()))
            return ok(views.html.users.index.render(user, toolTypes));
        return redirect(routes.Application.index());
    }

//    public Result uheader() {
//        User user = User.find.byId(Long.parseLong(session().get("user_id")));
//        return ok(views.html.users.uheader.render(user));
//    }

    @Security.Authenticated(UserAuth.class)
    public Result addTrans() {
        List<Transaction> trans = Transaction.find.all();
        Form<Transaction> transactionForm = form(Transaction.class).bindFromRequest();
        Transaction transaction = transactionForm.get();
        String t_id = transactionForm.data().get("tool_id");
        User u = User.find.byId(Long.parseLong(session().get("user_id")));
        Tool t = Tool.find.byId(Long.parseLong(t_id));
        Long tool_id = t.id;

        if (u.id.equals(t.owner.id)) {
            flash("error", "You can not borrow your own tools");
        } else {
            t = null;
            Transaction currentTrans = null;

            if (trans == null) {
                transaction.to_borrow = Tool.find.byId(Long.parseLong(t_id));
                transaction.renter = User.find.byId(Long.parseLong(session().get("user_id")));
                transaction.Available = false;
                transaction.save();
                flash("success", "You have successfully borrowed: " + transaction.to_borrow.name);
            } else {
                for (Transaction c : trans) {
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

        List<Transaction> trans = Transaction.find.all();
        Form<Transaction> transactionForm = form(Transaction.class).bindFromRequest();
        String t_id = transactionForm.data().get("tool_id");
        User u = User.find.byId(Long.parseLong(session().get("user_id")));
        Tool t = Tool.find.byId(Long.parseLong(t_id));

        for (Transaction tr : trans) {
            if (tr.renter != null && u.id.equals(tr.renter.id) && t.id.equals(tr.to_borrow.id)) {
                tr.delete();
                tr.Available = true;
                tr.save();
                flash("success", "You returned: " + tr.to_borrow.name);
            }
        }
        return redirect(routes.UserPage.index(u.id));
    }

    @Security.Authenticated(UserAuth.class)
    public Result comments() {
        User u = User.find.byId(Long.parseLong(session().get("user_id")));
        List<model.ToolType> toolTypes = ToolType.find.all();
        return ok(views.html.comment.show.render(u, u.commentsList, toolTypes));
    }

    @Security.Authenticated(UserAuth.class)
    public Result mytools() {
        User u = User.find.byId(Long.parseLong(session().get("user_id")));
        List<model.ToolType> toolTypes = ToolType.find.all();
        return ok(views.html.users.showT.render(u, u.toolsList, toolTypes));
    }

    @Security.Authenticated(UserAuth.class)
    public Result mybortools() {
        User u = User.find.byId(Long.parseLong(session().get("user_id")));
        List<Tool> tools = Tool.find.all();
        List<model.ToolType> toolTypes = ToolType.find.all();
        return ok(views.html.users.showB.render(u, tools, toolTypes, u.transactionList));
    }
}
