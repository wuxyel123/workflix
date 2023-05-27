package servlet;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import resource.*;
import utils.ErrorCode;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
/**
    Description: Servlet for activities
    Input: None
    Output: None
    Usage: Called by frontend
 */
public class ActivitiesServlet extends AbstractServlet{

    /**
     * Manage activities get requests
     * @see AbstractServlet
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        if (tokens.length < 4){
            writeError(res, ErrorCode.OPERATION_UNKNOWN);
        } else {
            try{
                // /activity/{activity_id}/
                Integer activityId = Integer.parseInt(tokens[3]);
                try{
                    Activities activity = Activities.fromJSON(req.getInputStream());
                    activity.setActivityId(activityId);
                    activity = new GetActivityByIdDatabase(getDataSource().getConnection(),activity).getActivityById();
                    if (activity == null){
                        writeError(res, ErrorCode.ACTIVITY_NOT_FOUND);
                        return;
                    }
                    ErrorCode ec = ErrorCode.OK;
                    res.setStatus(ec.getHTTPCode());
                    res.setContentType("application/json");
                    res.getWriter().write(activity.toJSON().toString());
                } catch (NamingException | SQLException e){
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                }
            } catch (NumberFormatException e){
                writeError(res, ErrorCode.OPERATION_UNKNOWN);
            }
        }
    }

    /**
     * Manage activities post requests
     * @see AbstractServlet
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{

        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        if (tokens.length < 4){
            writeError(res, ErrorCode.OPERATION_UNKNOWN);
        } else if (tokens[3].equals("create")){ // /activity/create/
            try{
                Activities activity = Activities.fromJSON(req.getInputStream());
                activity = new InsertActivityDatabase(getDataSource().getConnection(), activity).insertActivity();
                if (activity == null){
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                    return;
                }
                ErrorCode ec = ErrorCode.OK;
                res.setStatus(ec.getHTTPCode());
                res.setContentType("application/json");
                res.getWriter().write(activity.toJSON().toString());
            } catch (NamingException | SQLException e){
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        } else {
            writeError(res, ErrorCode.OPERATION_UNKNOWN);
        }
    }

    /**
     * Manage activities put requests
     * @see AbstractServlet
     */
    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        if (tokens.length < 5){
            writeError(res, ErrorCode.OPERATION_UNKNOWN);
        } else if (tokens[4].equals("update")){ // /activity/{activityid}/update/
            try{
                Activities activity = Activities.fromJSON(req.getInputStream());
                activity.setActivityId(Integer.parseInt(tokens[3]));
                activity = new UpdateActivityDatabase(getDataSource().getConnection(), activity).UpdateActivity();
                if (activity == null){
                    writeError(res, ErrorCode.ACTIVITY_NOT_FOUND);
                    return;
                }
                ErrorCode ec = ErrorCode.OK;
                res.setStatus(ec.getHTTPCode());
                res.setContentType("application/json");
                res.getWriter().write(activity.toJSON().toString());
            } catch (NamingException | SQLException e){
                //writeError(res, ErrorCode.INTERNAL_ERROR);
                res.getWriter().write(e.toString());
            }
        } else {
            writeError(res, ErrorCode.OPERATION_UNKNOWN);
        }
    }

    /**
     * Manage activities delete requests
     * @see AbstractServlet
     */
    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        if (tokens.length < 5){
            writeError(res, ErrorCode.OPERATION_UNKNOWN);
        } else if (tokens[4].equals("delete")){ // /activity/{activityid}/delete/
            try{
                Activities activity = Activities.fromJSON(req.getInputStream());
                activity.setActivityId(Integer.parseInt(tokens[3]));
                activity = new DeleteActivityDatabase(getDataSource().getConnection(), activity).DeleteActivity();
                if (activity == null){
                    writeError(res, ErrorCode.ACTIVITY_NOT_FOUND);
                    return;
                }
                ErrorCode ec = ErrorCode.OK;
                res.setStatus(ec.getHTTPCode());
                res.setContentType("application/json");
                res.getWriter().write(activity.toJSON().toString());
            } catch (NamingException | SQLException e){
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        } else {
            writeError(res, ErrorCode.OPERATION_UNKNOWN);
        }
    }

}
