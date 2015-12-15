package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import model.ToolType;
import model.Tool;
import play.mvc.Security;

import java.util.List;


/**
 * Created by dima on 12/1/15.
 */
public class ToolTypes extends Controller {

    public Result index() {
        List<ToolType> tooltypes = ToolType.find.all();
        return ok(views.html.tooltypes.index.render(tooltypes));
    }

    @Security.Authenticated(UserAuth.class)
    public Result create() {
        ToolType tooltype = Form.form(ToolType.class).bindFromRequest().get();
        tooltype.save();
        flash("success", "Saved new Tool Type: " + tooltype.name);
        return redirect(routes.ToolTypes.index());

    }

    public Result show(Long id){
        ToolType tooltype = ToolType.find.byId(id);
        if(tooltype == null) {
            return notFound("not found");
        } else {
            List<Tool> tools = tooltype.toolList;
            return ok(views.html.tooltypes.show.render(tooltype, tools));
        }
    }



}
