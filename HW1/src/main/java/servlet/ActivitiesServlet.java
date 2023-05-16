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
        op = op.substring(op.lastIndexOf("activity") + 9);
    }

    /**
     * Manage activities post requests
     * @see AbstractServlet
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("activity")+9);

        switch (op){
            case "update/":
                updateOperations(req, res);
                break;
            case "create/":
                insertionOperations(req, res);
                break;
            default:
                writeError(res, ErrorCode.INTERNAL_ERROR);
                logger.warn("requested op "+op);
        }

    }

    /**
     * Manage update operations
     * @see AbstractServlet
     */
    private void updateOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            String activity_id = req.getParameter("activity_id");

            Activities activities = null;
            boolean updatable = true;
            ErrorCode ec = null;
            Message m = null;
//            String dispatchPage = null;

            if (activity_id == null || activity_id.equals("") ) {
                updatable =false;
                ec = ErrorCode.ACTIVITY_INFORMATION_MISSING;;
                m = new Message(true, ec.getErrorMessage());
            } else {
                activities = new Activities();
                int id=Integer.parseInt(activity_id);
                activities.setActivityId(id);

                if (new GetActivityByIdDatabase(getDataSource().getConnection(), activities).getActivityById()==null){
                    updatable = false;
                    ec = ErrorCode.ANALYTICS_NOT_FOUND;
                    m = new Message(true, ec.getErrorMessage());
                }

                if (updatable){
                    activities = new UpdateActivityDatabase(getDataSource().getConnection(), activities).UpdateActivity();
                    if (activities!=null) {
                        ec = ErrorCode.OK;
                        m = new Message(true, "activity updated correctly");
                    } else {
                        writeError(res, ErrorCode.INTERNAL_ERROR);
                        logger.error("problem when updating activity: " + req.getRequestURL());
                    }
                }
            }

            req.setAttribute("message", m);
            res.setStatus(ec.getHTTPCode());
//            req.getRequestDispatcher(dispatchPage).forward(req, res);

        } catch (NamingException | SQLException e) {
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }
    }

    /**
     * Manage activities insert requests
     * @see AbstractServlet
     */
    private void insertionOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
        try {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String start_date = req.getParameter("start_date");
        String end_date = req.getParameter("end_date");
        String worked_time = req.getParameter("worked_time");
        String index = req.getParameter("index");

        Activities activities;

        ErrorCode ec = null;
        Message m = null;
//        String dispatchPage = null;
        boolean insertable = true;
        if (name == null || name.equals("") || description == null || description.equals("") || start_date == null || start_date.equals("")
                || end_date == null || end_date.equals("")|| worked_time == null || worked_time.equals("")|| index == null || index.equals("")) {
            ec = ErrorCode.ACTIVITY_INFORMATION_MISSING;;
            m = new Message(true, ec.getErrorMessage());
//            dispatchPage = "/jsp/builder-area/edit-activity.jsp";
        } else {
            activities = new Activities();
            if (new GetActivityByIdDatabase(getDataSource().getConnection(), activities).getActivityById() != null) {
                insertable = false;
                ec = ErrorCode.ACTIVITY_ALREADY_PRESENT;
//                dispatchPage = "/jsp/builder-area/edit-activity.jsp";
                req.setAttribute("message", new Message(true, ec.getErrorMessage()));
                m = new Message(true, ec.getErrorMessage());
            }

            if (insertable) {
                activities = new InsertActivityDatabase(getDataSource().getConnection(), activities).insertActivity();
                if (activities != null) {
                    m = new Message(true, "Activity inserted correctly");
                    ec = ErrorCode.OK;
//                    dispatchPage = "/jsp/message-page.jsp";
                } else {
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                    logger.error("unknown error: " + req.getRequestURL());
                }
            }
        }

        res.setStatus(ec.getHTTPCode());
        req.setAttribute("message", m);
//        req.getRequestDispatcher(dispatchPage).forward(req, res);

    } catch (NamingException | SQLException e){
        writeError(res, ErrorCode.INTERNAL_ERROR);
        logger.error("stacktrace:", e);
    }
}

    /**
     * Manage activities delete requests
     * @see AbstractServlet
     */
    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        deleteOperations(req, res);
    }


    /**
     * Manage activities delete requests
     * @see AbstractServlet
     */
    public void deleteOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String activity_id = req.getParameter("activity_id");
        Activities activities;
        try {
            if (activity_id == null || activity_id.equals("")) {
                Message m = new Message(true, "activity not found");
                ErrorCode ec = ErrorCode.ACTIVITY_NOT_FOUND;
                res.setStatus(ec.getHTTPCode());
                req.setAttribute("message", m);
//                req.getRequestDispatcher("/jsp/builder-area/edit-park.jsp").forward(req, res);
            } else {
                activities  =new Activities();
                int id=Integer.parseInt(activity_id);
                activities.setActivityId(id);

                activities = new DeleteActivityDatabase(getDataSource().getConnection(), activities).DeleteActivity();

                if (activities != null) {
                    logger.error("activity deleted correctly");
                    Message m = new Message(true, "activity deleted correctly");
                    res.setStatus(HttpServletResponse.SC_OK);

                    res.getWriter().write(m.toJSON().toString());
                } else {
                    ErrorCode ec = ErrorCode.INTERNAL_ERROR;
                    writeError(res, ec);
                    logger.error("problem when deleting activity: " + req.getRequestURL());
                }
            }
        } catch (SQLException | NamingException e) {
            ErrorCode ec = ErrorCode.INTERNAL_ERROR;
            writeError(res, ec);
            logger.error("stacktrace:", e);
        }
    }

}
