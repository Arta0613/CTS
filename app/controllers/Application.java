package controllers;

import model.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Welcome to HubCT"));
    }

    public Result login() {
        DynamicForm userForm = Form.form().bindFromRequest();
        String username = userForm.data().get("username");
        String password = userForm.data().get("password");

        User user = User.find.where().eq("username", username).findUnique();

        if(user != null && user.authenticate(password)) {
            flash("success", "Welcome back " + user.username);
            session("user_id", user.id.toString());
            return redirect(routes.UserPage.index(user.id));
        } else {
            flash("error", "Invalid login. Check your username and password.");
            return redirect(routes.Application.index());
        }

    }

    public Result signup() {
        DynamicForm userForm = Form.form().bindFromRequest();
        String username = userForm.data().get("username");
        String password = userForm.data().get("password");
        String confirm_password = userForm.data().get("confirm_password");
        String email = userForm.data().get("email");
        String confirm_email = userForm.data().get("confirm_email");

        if (!email.equals(confirm_email)) {
            flash("error", "emails do no match\n" + email + " - " + confirm_email);
            return redirect(routes.Application.index());
        } else if (!password.equals(confirm_password)) {
            flash("error", "passwords do no match\n" + password + " - " + confirm_password);
            return redirect(routes.Application.index());
        }

        User user = User.createNewUser(username, password, email);

        if(user == null) {
            flash("error", "Invalid user");
            return redirect(routes.Application.index());
        }

        user.save();

        flash("success", "Welcome new user " + user.username);
        session("user_id", user.id.toString());
        return redirect(routes.Application.index());
    }

    public Result logout() {
        flash("success", "Logged out");
        session().remove("user_id");
        return redirect(routes.Application.index());
    }
}
