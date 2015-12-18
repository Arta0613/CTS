package controllers;


import model.*;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static play.data.Form.form;


/**
 * Created by dima on 11/24/15.
 */
public class Tools extends Controller {

    public Result index() {
        List<Tool> tools = Tool.find.all();
        List<model.ToolType> toolTypes = ToolType.find.all();
        List<Transaction> transactions = Transaction.find.all();
        if (session().containsKey("user_id")) {
            User user = User.find.byId(Long.parseLong(session().get("user_id")));
            if (session().containsKey("user_id") && session().get("user_id").equals(user.id.toString()))
                return ok(views.html.tools.uindex.render(user, tools, toolTypes, transactions));
        }
        return ok(views.html.tools.index.render(tools, toolTypes));
    }

    @Security.Authenticated(UserAuth.class)
    public Result create() {
        Form<Tool> toolForm = form(Tool.class).bindFromRequest();
        String tooltype_id = toolForm.data().get("tooltype_id");
        ToolType tooltype = ToolType.find.byId(Long.parseLong(tooltype_id));
        if (tooltype == null) {
            flash("error", "Invalid Tool Type: " + tooltype_id + " Try again.");
            return redirect(routes.Tools.index());
        }
        Tool tool = toolForm.get();
        tool.toolType = tooltype;
        tool.owner = User.find.byId(Long.parseLong(session().get("user_id")));
        String imgURL = toolForm.data().get("imgURL");
        tool.image = imgURL;
        tool.save();
        flash("success", "Saved new Tool: " + tool.name);
        return redirect(routes.UserPage.mytools());

    }

    public Result toolform() {
        List<ToolType> tooltypes = ToolType.find.all();
        User user = User.find.byId(Long.parseLong(session().get("user_id")));
        return ok(views.html.tools.form.render(tooltypes, user));

    }

    public Result show(Long id) {
        Tool tool = Tool.find.byId(id);
        List<Comment> comments = tool.commentList;
        List<Transaction> transactions = tool.transactionList;
        List<model.ToolType> toolTypes = ToolType.find.all();
        if (tool == null) {
            return notFound("Not Avaiable");
        }
        if (session().containsKey("user_id")) {
            User user = User.find.byId(Long.parseLong(session().get("user_id")));
            if (session().containsKey("user_id") && session().get("user_id").equals(user.id.toString()))
                return ok(views.html.tools.ushow.render(user, comments, transactions, tool, toolTypes));
        }
        return ok(views.html.tools.show.render(tool, comments, transactions, toolTypes));
    }

    @Security.Authenticated(UserAuth.class)
    public Result createComment() {
        Form<Comment> commentForm = form(Comment.class).bindFromRequest();
        Comment comment = commentForm.get();
        String t_id = commentForm.data().get("tool_id");
        comment.tool = Tool.find.byId(Long.parseLong(t_id));
        comment.user = User.find.byId(Long.parseLong(session().get("user_id")));
        comment.save();
        return redirect(routes.Tools.show(comment.tool.id));
    }

    ToolType tooltype = null;
    public Result search() {
        DynamicForm searchForm = Form.form().bindFromRequest();
        List<ToolType> tooltypes = ToolType.find.all();
        String tooltype_id = searchForm.data().get("tooltype");
        List<Tool> toolsAll = Tool.find.all();
        List<Tool> tools = new ArrayList<>();
        // Header Search
        String searchString = searchForm.get("search");
        tooltype = null;
        if (searchString != null) {
            if (!tooltype_id.equals("0")) {
                tooltype = ToolType.find.byId(Long.parseLong(tooltype_id));
            }
            if (searchString.isEmpty())
                searchString = "All Tools";
            if (!toolsAll.isEmpty() && tooltype != null) {
                tools.addAll(toolsAll.stream().filter(t -> t.toolType.name.equals(tooltype.name)).collect(Collectors.toList()));
            } else {
                tools = toolsAll;
            }
            return renderShow(searchString, tools, tooltypes);
        } else {
            searchString = searchForm.get("searchString");
            if (!tooltype_id.equals("0")) {
                tooltype = ToolType.find.byId(Long.parseLong(tooltype_id));
            }
            if (!toolsAll.isEmpty() && tooltype != null) {
                tools.addAll(toolsAll.stream().filter(t -> t.toolType.name.equals(tooltype.name)).collect(Collectors.toList()));
            } else {
                tools = toolsAll;
            }
            return renderShow(searchString, tools, tooltypes);
        }
    }

    public Result delete() {
        DynamicForm form = Form.form().bindFromRequest();
        Tool.find.ref(Long.parseLong(form.data().get("id"))).delete();
        return redirect(routes.UserPage.mytools());
    }

    public Result searchUserTools() {
        DynamicForm userSelect = Form.form().bindFromRequest();
        String user_id = userSelect.data().get("user");
        User loggedUser = User.find.byId(Long.parseLong(session().get("user_id")));
        User searchedUser = User.find.byId(Long.parseLong(user_id));
        return ok(views.html.search.ushowuserstools.render(searchedUser, loggedUser, ToolType.find.all()));
    }

    private Result renderShow(String searchString, List<Tool> tools, List<ToolType> tooltypes) {
        if (session().containsKey("user_id")) {
            List<User> users = User.find.all();
            User user = User.find.byId(Long.parseLong(session().get("user_id")));
            if (session().containsKey("user_id") && session().get("user_id").equals(user.id.toString()))
                flash("success", searchString);
            return ok(views.html.search.ushow.render(searchString, tools, tooltypes, user, users));
        }
        flash("success", searchString);
        return ok(views.html.search.show.render(searchString, tools, tooltypes));
    }
}


