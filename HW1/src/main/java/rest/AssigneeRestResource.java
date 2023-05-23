package rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.Activities;
import resource.User;
import utils.ErrorCode;
import dao.InsertAssigneeDatabase;
import dao.DeleteAssigneeDatabase;
import dao.GetAssigneeDatabase;
import resource.Assignee;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This class is used to handle the requests for the assignee resource
 */
public class AssigneeRestResource extends RestResource {

    // The operation requested by the client
    protected final String op;
    // The error code
    protected ErrorCode ec = null;
    // The response
    protected String response = null;
    // The tokens of the request
    protected final String[] tokens;

    /**
     * Constructor
     * @param req The request
     * @param res The response
     * @param con The connection to the database
     */
    public AssigneeRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Delete an assignee
     * @throws IOException Error in IO operations
     */
    public void deleteAssignee() throws IOException {
        try {
            Assignee assignee = Assignee.fromJSON(req.getInputStream());
            assignee.setActivityId(Integer.parseInt(tokens[4]));
            assignee= new DeleteAssigneeDatabase(con, assignee).deleteAssignee();
            if (assignee == null) {
                initError(ErrorCode.ASSIGNEE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = assignee.toJSON().toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }

    }

    /**
     * Add an assignee
     * @throws IOException Error in IO operations
     */
    public void addAssignee() throws IOException {
        try {
            Assignee assignee = Assignee.fromJSON(req.getInputStream());
            assignee.setActivityId(Integer.parseInt(tokens[4]));
            assignee = new InsertAssigneeDatabase(con, assignee).addAssignee();
            if (assignee == null) {
                initError(ErrorCode.ASSIGNEE_ALREADY_PRESENT);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = assignee.toJSON().toString();
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                initError(ErrorCode.ASSIGNEE_ALREADY_PRESENT);
            } else {
                initError(ErrorCode.INTERNAL_ERROR);
                logger.error("stacktrace:", e);
            }
        } finally {
            respond();
        }
    }

    /**
     * Get an assignee
     * @throws IOException Error in IO operations
     */
    public void getAssignee() throws IOException {
        try {
            Activities activity = Activities.fromJSON(req.getInputStream());
            activity.setActivityId(Integer.parseInt(tokens[4]));
            List<User> users= new GetAssigneeDatabase(con, activity).getAssignee();
            if (users == null || users.isEmpty()) {
                initError(ErrorCode.ASSIGNEE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = User.toJSONList(users).toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    /**
     * Respond to the client
     * @throws IOException Error in IO operations
     */
    private void respond() throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(response);
    }

    /**
     * Initialize the error
     * @param ec The error code
     */
    private void initError(ErrorCode ec) {
        this.ec = ec;
        response = ec.toJSON().toString();
    }
}