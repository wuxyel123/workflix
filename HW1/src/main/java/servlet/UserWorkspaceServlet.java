package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rest.UserWorkspaceRestResource;
import utils.ErrorCode;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class UserWorkspaceServlet extends AbstractServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        if (processUserWorkspace(req, res)) {
            return;
        }

        String op = req.getRequestURI();
        writeError(res, ErrorCode.OPERATION_UNKNOWN);
        logger.warn("requested op " + op);

    }

    private boolean processUserWorkspace(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("workspace")) {
            return false;
        }
        try {
            if (tokens.length == 5 && tokens[3].equals("get")) {
            	UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteUserWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            else if (tokens.length == 5 && tokens[3].equals("add")) {
            	UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateUserWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            else if (tokens.length == 5 && tokens[3].equals("remove")) {
            	UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.AssignUserPermission();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
        } catch (NamingException | SQLException e) {
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }

        return true;
    }

}
