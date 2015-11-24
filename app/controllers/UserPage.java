package controllers;


import model.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

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

    public Result create() {
        return ok(views.html.tools.form.render("Anything"));
    }

}
