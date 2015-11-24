package controllers;

import model.*;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by Arman on 11/18/15.
 */
public class UserAuth  extends Security.Authenticator{
    // When return is null, Authentication failed
    @Override
    public String getUsername(final Http.Context ctx) {
        String userIdStr = ctx.session().get("user_id");
        if(userIdStr == null) return null;

        model.User user = model.User.find.byId(Long.parseLong(userIdStr));
        return (user != null ? user.id.toString() : null);
    }

    @Override
    public Result onUnauthorized(final Http.Context ctx) {
        ctx.flash().put("error",
                "Nice try, but you need to log in first!");
        return redirect(routes.Application.index());
    }
}
