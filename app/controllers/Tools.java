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
        return ok(views.html.tools.show.render(tool, comments, transactions));
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
        String searchString = searchForm.get("search");

        List<ToolType> tooltypes = ToolType.find.all();
        String tooltype_id = searchForm.data().get("tooltype");

        // Buggy
        if (!tooltype_id.equals("0")) {
            tooltype = ToolType.find.byId(Long.parseLong(tooltype_id));
        }
        List<Tool> toolsAll = Tool.find.all();
        List<Tool> tools = new ArrayList<>();
        if (!toolsAll.isEmpty() && tooltype != null) {
            tools.addAll(toolsAll.stream().filter(t -> t.toolType.name.equals(tooltype.name)).collect(Collectors.toList()));
        } else {
            tools = toolsAll;
        }
        if (searchString == null && searchForm.get("searchString") != null) {
            searchString = searchForm.get("searchString");
        }
        if (!searchString.isEmpty()) {
            if (session().containsKey("user_id")) {
                User user = User.find.byId(Long.parseLong(session().get("user_id")));
                if (session().containsKey("user_id") && session().get("user_id").equals(user.id.toString()))
                    flash("success", searchString);
                return ok(views.html.search.ushow.render(searchString, tools, tooltypes, user));
            }
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
}


