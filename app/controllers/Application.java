package controllers;

import model.Tool;
import model.ToolType;
import model.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.List;

public class Application extends Controller {

    public Result index() {
        List<Tool> tools = Tool.find.all();
        List<model.ToolType> toolTypes = ToolType.find.all();
        if (session().containsKey("user_id")) {
            User user = User.find.byId(Long.parseLong(session().get("user_id")));
            if (session().containsKey("user_id") && session().get("user_id").equals(user.id.toString()))
                return ok(views.html.uindex.render(user, tools, toolTypes));
        }
        return ok(index.render(tools, toolTypes));
    }

    public Result login() {
        DynamicForm userForm = Form.form().bindFromRequest();
        String username = userForm.data().get("username");
        String password = userForm.data().get("password");

        User user = User.find.where().eq("username", username).findUnique();

        if (user != null && user.authenticate(password)) {
            flash("success", "Welcome " + user.username);
            session("user_id", user.id.toString());
            return redirect(routes.UserPage.index(user.id));
        } else {
            flash("error", "Invalid login. Check your username and password.");
            return redirect(routes.Application.loginform());
        }

    }

    public Result signup() {
        DynamicForm userForm = Form.form().bindFromRequest();
        String username = userForm.data().get("username");
        String password = userForm.data().get("password");
        String confirm_password = userForm.data().get("confirm_password");
        String email = userForm.data().get("email");
        String confirm_email = userForm.data().get("confirm_email");
        List<model.ToolType> toolTypes = ToolType.find.all();

        if (!email.equals(confirm_email)) {
            flash("error", "emails do no match\n" + email + " - " + confirm_email);
            return redirect(routes.Application.signupform());
        } else if (!password.equals(confirm_password)) {
            flash("error", "passwords do no match\n" + password + " - " + confirm_password);
            return redirect(routes.Application.signupform());
        }

        User user = User.find.where().eq("username", username).findUnique();
        if (user != null) {
            flash("error", username + " exists");
            return ok(views.html.loginfrom.render(toolTypes));
        } else if (User.find.where().eq("email", email).findUnique() != null) {
            ;
            flash("error", email + " exists");
            return ok(views.html.loginfrom.render(toolTypes));
        }

        user = User.createNewUser(username, password, email);

        if (user == null) {
            flash("error", "Please check your input and try again");
            return redirect(routes.Application.signupform());
        }

        user.save();

        flash("success", "Created new user " + user.username);
        session("user_id", user.id.toString());
        return redirect(routes.UserPage.index(user.id));
    }

    public Result logout() {
        flash("success", "Logged out");
        session().remove("user_id");
        return redirect(routes.Application.index());
    }

    public Result loginform() {
        List<model.ToolType> toolTypes = ToolType.find.all();
        return ok(views.html.loginfrom.render(toolTypes));
    }

    public Result signupform() {
        List<model.ToolType> toolTypes = ToolType.find.all();
        return ok(views.html.users.form.render(toolTypes));
    }
}
