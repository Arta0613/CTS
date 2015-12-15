package controllers;


import model.*;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static play.data.Form.form;


/**
 * Created by dima on 11/24/15.
 */
public class Tools extends Controller {

    public Result index(){
        List<Tool> tools = Tool.find.all();
        List<ToolType> tooltypes = ToolType.find.all();
        return ok(views.html.tools.index.render(tools, tooltypes));
    }

    @Security.Authenticated(UserAuth.class)
    public Result create(){
        Form<Tool> toolForm = form(Tool.class).bindFromRequest();
        String tooltype_id = toolForm.data().get("tooltype_id");
        ToolType tooltype = ToolType.find.byId(Long.parseLong(tooltype_id));
        if(tooltype == null) {
            flash("error", "Invalid Tool Type: " + tooltype_id + " Try again.");
            return redirect(routes.Tools.index());
        }
        Tool tool = toolForm.get();
        tool.toolType = tooltype;
        tool.owner = User.find.byId(Long.parseLong(session().get("user_id")));
        tool.save();
        flash("success", "Saved new Tool: " + tool.name);
        return redirect(routes.Tools.index());

    }
//    public Result form(){
//        List<model.ToolType> tooltypes = model.ToolType.find.all();
//        return ok(views.html.tools.form.render(tooltypes));
//    }
    public Result show(Long id){
        Tool tool = Tool.find.byId(id);
        List<Comment> comments = tool.commentList;
        List<Transaction> transactions = tool.transactionList;

        if(tool == null) {
            return notFound("not found");
        } else {

            return ok(views.html.tools.show.render(tool, comments, transactions));
        }
    }

    public Result createComment(){
        Form<Comment> commentForm = form(Comment.class).bindFromRequest();
        Comment comment = commentForm.get();
        String t_id = commentForm.data().get("tool_id");
        comment.tool = Tool.find.byId(Long.parseLong(t_id));
        comment.user = User.find.byId(Long.parseLong(session().get("user_id")));
        comment.save();
            return redirect(routes.Tools.show(comment.tool.id));
    }

    public Result search() {
        DynamicForm searchForm = Form.form().bindFromRequest();
        String searchString = searchForm.get("search");
        List<Tool> tools = Tool.find.all();
        List<ToolType> tooltypes = ToolType.find.all();
        if (!searchString.isEmpty()) {
            flash("success", searchString);
            return ok(views.html.search.show.render(searchString, tools, tooltypes));
        } else {
            flash("error", "oh man =( you didn't search for anything");
            //TODO: fill rest of cases
            String previousURL = searchForm.get("previousURL");
            if (previousURL.startsWith("/user")) {
                return redirect(routes.UserPage.index(Integer.parseInt(session().get("user_id"))));
            } else
                return redirect(routes.Application.index());
        }
    }
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


