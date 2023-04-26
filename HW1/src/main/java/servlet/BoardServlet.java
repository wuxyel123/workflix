package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rest.BoardRestResource;
import rest.CommentRestResource;
import rest.UserWorkspaceRestResource;
import rest.WorkspaceRestResource;
import utils.ErrorCode;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class BoardServlet extends AbstractServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        if (processBoard(req, res)) {
            return;
        }

        String op = req.getRequestURI();
        writeError(res, ErrorCode.OPERATION_UNKNOWN);
        logger.warn("requested op " + op);
    }
    private boolean processBoard(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("board")) {
            return false;
        }
        try {
            /**
             * Board APIs are:
             * Board/{boardid}
             * board/create
             * board/update/{boardid}
             **/

            // board/get
            if (tokens.length == 5 && tokens[4].equals("get")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    //case "GET" -> urr.get();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // board/create
            else if (tokens.length == 5 && tokens[4].equals("add")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateBoard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // board/delete
            else if (tokens.length == 5 && tokens[3].equals("remove")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteBoard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // board/update
            else if (tokens.length == 5 && tokens[3].equals("update")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.UpdateBoard();
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