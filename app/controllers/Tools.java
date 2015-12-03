package controllers;


import model.Comment;
import model.Tool;
import model.ToolType;
import model.User;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;


/**
 * Created by dima on 11/24/15.
 */
public class Tools extends Controller {

    public Result index(){
        List<model.Tool> tools = model.Tool.find.all();
        List<model.ToolType> tooltypes = model.ToolType.find.all();
        return ok(views.html.tools.index.render(tools, tooltypes));
    }

    @Security.Authenticated(UserAuth.class)
    public Result create(){
        Form<model.Tool> toolForm = form(model.Tool.class).bindFromRequest();
        String tooltype_id = toolForm.data().get("tooltype_id");

        model.ToolType tooltype = model.ToolType.find.byId(Long.parseLong(tooltype_id));
        if(tooltype == null) {
            flash("error", "Invalid Tool Type: " + tooltype_id + " Try again.");
            return redirect(routes.Tools.index());
        }

        model.Tool tool = toolForm.get();
        tool.toolType = tooltype;
        tool.owner = User.find.byId(Long.parseLong(session().get("user_id")));
        tool.save();
        flash("success", "Saved new Tool: " + tool.name);
        return redirect(routes.Tools.index());

    }

    public Result show(Long id){
        model.Tool tool = model.Tool.find.byId(id);
        if(tool == null) {
            return notFound("not found");
        } else {

            return ok(views.html.tools.show.render(tool));
        }
    }

    //Work on this!
    /*public Result createComment(){
        Form<model.Comment> commentForm = form(model.Comment.class).bindFromRequest();
        model.Comment comment = commentForm.get();
        comment.user = User.find.byId(Long.parseLong(session().get("user_id")));
        String tool_id = commentForm.data().get("tool_id");
        comment.save();
        Long id = comment.tool.id;
        model.Tool tool = model.Tool.find.byId(id);
            return ok(views.html.tools.show.render(tool));
    }
    */
    /* Future Possible Search Function
    public List<String> searchTools(String toolToSearch) {
        List<model.Tool> tools = model.Tool.find.all();
        List<String> arrayofTools = new ArrayList<>();
        for (int i=0;i<tools.size();i++){
            arrayofTools.add(tools.get(i).toString());
        }
        for (String tool : arrayofTools) {
            System.out.println("This is the tool "+ tool);
        }
        return arrayofTools;
    }
    */

}


