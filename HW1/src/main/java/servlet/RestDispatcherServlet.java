package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rest.*;
import utils.ErrorCode;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class RestDispatcherServlet extends AbstractServlet{



    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        if(processUser(req, res)){
            return;
        }

        if(processWorkspace(req, res)){
            return;
        }

        if(processComment(req, res)){
            return;
        }

        if (processAssignee(req, res)) {
            return;
        }

        if (processBoard(req, res)) {
            return;
        }
        String op = req.getRequestURI();
        writeError(res, ErrorCode.OPERATION_UNKNOWN);
        logger.warn("requested op " + op);

    }

    /**
     * Process user rest dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processUser(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        //the first token will always be the empty;
        //the second will be the context;
        //the third should be "user";
        if (tokens.length<4 || !tokens[3].equals("user")){
            return false;
        }
        try{
            /**User APIs are:
             *  user/login
             *  user/register
             *  user/logout
             *  user/delete/{userid}
             *  user/update/{userid}
             *  user/update/{userid}/password
             *  user/{userid}
             * */
            // user/login
            if (tokens.length==4 && tokens[3].equals("login")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.GetUserFromMailAndPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/register
            else if (tokens.length==4 && tokens[3].equals("register")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/delete/{userid}
            else if (tokens.length==5 && tokens[3].equals("delete")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/update/{userid}
            else if (tokens.length==5 && tokens[3].equals("update")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateUserNoPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/update/{userid}/password
            else if (tokens.length==6 && tokens[3].equals("update") && tokens[5].equals("password")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateUserPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/{userid}
            else if (tokens.length==4 && Integer.parseInt(tokens[3])%1==0){
                Integer.parseInt(tokens[4]);
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetUserFromId();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            } else {
                return  false;
            }

        }
        catch (NumberFormatException e){
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
        }
        catch (NamingException | SQLException e){
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }


        return true;
    }

    /**
     * Process Workspace rest dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processWorkspace(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("workspace")) {
            return false;
        }
        try {
            /**
             * workspace APIs are:
             * /workspace/{workspaceid}
             * /workspace/create
             * /workspace/delete/{workspaceid}
             * /workspace/update/{workspaceid}
             * /workspace/{workspaceid}/adduser
             * /workspace/{workspaceid}/removeuser
             * workspace/[workspaceid}/assignuserpermission
             **/

            // workspace/{workspaceid}
             if (tokens.length == 4 && Integer.parseInt(tokens[3]) % 1 == 0) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // workspace/create
            else if (tokens.length == 4 && tokens[3].equals("create")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // workspace/delete/{workspaceid}
            else if (tokens.length == 5 && tokens[3].equals("delete")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // workspace/update/{workspaceid}
            else if (tokens.length == 5 && tokens[3].equals("update")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/adduser
            else if (tokens.length == 5 && tokens[5].equals("adduser")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.AddUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/removeuser
            else if (tokens.length == 5 && tokens[4].equals("removeuser")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteUserWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/assignuserpermission
            else if (tokens.length == 5 && tokens[4].equals("assignuserpermission")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.AssignUserPermission();
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

    /**
     * Process comment rest dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processComment(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("comment")) {
            return false;
        }
        try {
            /**
             * Activity APIs are:
             * activity/comment/get
             * activity/comment/add
             **/

            // assignee/get
            if (tokens.length == 5 && tokens[4].equals("get")) {
                CommentRestResource urr = new CommentRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.getComments();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // assignee/add
            else if (tokens.length == 5 && tokens[4].equals("add")) {
                CommentRestResource urr = new CommentRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.addComments();
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

    /**
     * Process assignee rest dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processAssignee(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("assignee")) {
            return false;
        }
        try {
            /**
             * Activity APIs are:
             * activity/assignee/get
             * activity/assignee/add
             * activity/assignee/remove
             **/

            // assignee/get
            if (tokens.length == 5 && tokens[4].equals("get")) {
                AssigneeRestResource urr = new AssigneeRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.getAssignee();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // assignee/add
            else if (tokens.length == 5 && tokens[4].equals("add")) {
                AssigneeRestResource urr = new AssigneeRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.addAssignee();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // assignee/remove
            else if (tokens.length == 5 && tokens[4].equals("remove")) {
                AssigneeRestResource urr = new AssigneeRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.deleteAssignee();
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

    /**
     * Process user board dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
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
