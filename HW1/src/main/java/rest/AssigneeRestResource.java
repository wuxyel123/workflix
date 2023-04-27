package rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ErrorCode;
import dao.InsertAssigneeDatabase;
import dao.DeleteAssigneeDatabase;
import dao.GetAssigneeDatabase;
import resource.Assignee;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AssigneeRestResource extends RestResource {

    protected final String op;
    protected ErrorCode ec = null;
    protected String response = null;
    protected final String[] tokens;

    public AssigneeRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    public void deleteAssignee() throws IOException {
        try {
            Assignee assignee = new Assignee();
            assignee.setActivityId(Integer.parseInt(tokens[6]));
            assignee.setUserId(Integer.parseInt(tokens[7]));
            if (new DeleteAssigneeDatabase(con, assignee).deleteAssignee() == null) {
                initError(ErrorCode.ASSIGNEE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }

    }

    public void addAssignee() throws IOException {
        try {
            Assignee assignee = new Assignee();
            assignee.setActivityId(Integer.parseInt(tokens[6]));
            assignee.setUserId(Integer.parseInt(tokens[7]));
            if (new InsertAssigneeDatabase(con, assignee).addAssignee() == null) {
                initError(ErrorCode.ASSIGNEE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    public void getAssignee() throws IOException {
        try {
            Assignee assignee = new Assignee();
            assignee.setActivityId(Integer.parseInt(tokens[6]));
            assignee.setUserId(Integer.parseInt(tokens[7]));
            if (new GetAssigneeDatabase(con, assignee).getAssignee() == null) {
                initError(ErrorCode.ASSIGNEE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    private void respond() throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(response);
    }

    private void initError(ErrorCode ec) {
        this.ec = ec;
        response = ec.toJSON().toString();
    }
}