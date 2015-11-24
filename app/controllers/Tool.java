package controllers;


import model.User;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by dima on 11/24/15.
 */
public class Tool extends Controller {

    public Result create(){

        DynamicForm toolForm = Form.form().bindFromRequest();
        String toolName = toolForm.get("tname");
        String toolType = toolForm.get("typeList");
        String toolDesc = toolForm.get("tdesc");
        User curUser = User.find.byId(Long.parseLong(session().get("user_id")));

        model.Tool tool = model.Tool.createNewTool(
                toolName,toolType,toolDesc,curUser);

        tool.save();

        flash("success", curUser.username+" your tool has been created");

        return redirect(routes.UserPage.index(curUser.id));
    }
}
