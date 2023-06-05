package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rest.*;
import utils.ErrorCode;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Description: Servlet for rest dispatcher
 * Input: None
 * Output: None
 * Usage: Called by frontend
 */
@MultipartConfig
public class RestDispatcherServlet extends AbstractServlet{


    /**
     * Service method for rest dispatcher
     * Divide the request in different parts and call the right rest resource
     * @see HttpServlet.HttpServlet()
     */
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

        if (processSubboard(req, res)) {
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
        if (tokens.length<5 || !tokens[3].equals("user")){
            return false;
        }
        try{
            /**User APIs are: start counting from 3 for tokens e.g. user is at index 3
             *  user/login
             *  user/register
             *  user/logout
             *  user/delete/{userid}
             *  user/update/{userid}
             *  user/update/{userid}/password
             *  user/{userid}
             *  user/{userid}/workspaces
             *  user/getbyemail
             * */
            // user/login
            if (tokens.length==5 && tokens[4].equals("login")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.GetUserFromMailAndPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/register
            else if (tokens.length==5 && tokens[4].equals("register")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/delete/{userid}
            else if (tokens.length==6 && tokens[4].equals("delete")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/update/{userid}
            else if (tokens.length==6 && tokens[4].equals("update")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateUserNoPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/update/{userid}/password
            else if (tokens.length==7 && tokens[4].equals("update") && tokens[6].equals("password")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateUserPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // user/{userid}/workspaces
            else if (tokens.length==6 && tokens[5].equals("workspaces")){
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetWorkSpacesByUserId();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // user/getbyemail
            else if (tokens.length==5 && tokens[4].equals("getbyemail")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.GetUserFromMail();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // user/{userid}
            else if (tokens.length==5 && Integer.parseInt(tokens[4])%1==0){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetUserFromId();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            else{
                return false;
            }


        }
        catch (NumberFormatException e){
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
            logger.error("stacktrace:", e);
        }
        catch (NamingException | SQLException | ServletException e){
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
        if (tokens.length < 5 || !tokens[3].equals("workspace")) {
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
             * /workspace/[workspaceid}/assignuserpermission
             * /workspace/{workspaceid}/boards
             **/

            // workspace/create
            if (tokens.length == 5 && tokens[4].equals("create")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // workspace/delete/{workspaceid}
            else if (tokens.length == 6 && tokens[4].equals("delete")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // workspace/update/{workspaceid}
            else if (tokens.length == 6 && tokens[4].equals("update")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/adduser
            else if (tokens.length == 6 && tokens[5].equals("adduser")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.AddUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/removeuser
            else if (tokens.length == 6 && tokens[5].equals("removeuser")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteUserWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/assignuserpermission
            else if (tokens.length == 6 && tokens[5].equals("assignuserpermission")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.AssignUserPermission();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/boards
            else if (tokens.length == 6 && tokens[5].equals("boards")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetBoardsByWorkspaceId();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}
            else if (tokens.length == 5 && Integer.parseInt(tokens[4]) % 1 == 0) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }else {
                return false;
            }




        } catch (NumberFormatException e) {
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
            logger.error("stacktrace:", e);
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
        if (tokens.length < 7 || !tokens[3].equals("activity") || !tokens[5].equals("comment")) {
            return false;
        }
        try {
            /**
             * Activity APIs are:
             * activity/{activityid}/comment/get
             * activity/{activityid}/comment/add
             * activity/{activityid}/comment/delete/{commentid}
             * activity/{activityid}/comment/update/{commentid}
             **/


            // activity/{activityid}/comment/get
            if (tokens.length == 7 && tokens[6].equals("get")) {
                CommentRestResource urr = new CommentRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetComments();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // activity/{activityid}/comment/add
            else if (tokens.length == 7 && tokens[6].equals("add")) {
                CommentRestResource urr = new CommentRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.AddComments();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // activity/{activityid}/comment/delete/{commentid}
            else if (tokens.length == 8 && tokens[6].equals("delete")) {
                CommentRestResource urr = new CommentRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteComment();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // activity/{activityid}/comment/update/{commentid}
            else if (tokens.length == 8 && tokens[6].equals("update")) {
                CommentRestResource urr = new CommentRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateComment();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            else {
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
        if (tokens.length < 7 || !tokens[5].equals("assignee")) {
            return false;
        }
        try {
            /**
             * Activity APIs are:
             * activity/{activityid}/assignee/get
             * activity/{activityid}/assignee/add
             * activity/{activityid}/assignee/{assigneeid}/remove
             **/

            // activity/{activityid}/assignee/get
            if (tokens.length == 7 && tokens[6].equals("get")) {
                AssigneeRestResource urr = new AssigneeRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.getAssignee();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // activity/{activityid}/assignee/add
            else if (tokens.length == 7 && tokens[6].equals("add")) {
                AssigneeRestResource urr = new AssigneeRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.addAssignee();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // activity/{activityid}/assignee/remove
            else if (tokens.length == 7 && tokens[6].equals("remove")) {
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
        if (tokens.length < 5 || !tokens[3].equals("board")) {
            return false;
        }
        try {
            /**
             * board APIs are:
             * board/{boardid}
             * board/create
             * board/update/{boardid}
             * board/delete/{boardid}
             * board/{boardid}/subboards
             **/

            // board/create
            if (tokens.length == 5 && tokens[4].equals("create")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateBoard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // board/{boardid}/delete
            else if (tokens.length == 6 && tokens[5].equals("delete")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteBoard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // board/{boardid}/update
            else if (tokens.length == 6 && tokens[5].equals("update")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateBoard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // board/{boardid}/subboards
            else if (tokens.length == 6 && tokens[5].equals("subboards")) {
                SubboardRestResource urr = new SubboardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetSubboardsByBoardId();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // board/{boardid}
            else if (tokens.length == 5 && Integer.parseInt(tokens[4])%1==0) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetBoardById();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            else {
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
     * Process subboard dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processSubboard(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "subboard";
        if (tokens.length < 5 || !tokens[3].equals("subboard")) {
            return false;
        }
        try {
            /**
             * Subboard APIs are:
             * subboard/{subboardid}
             * subboard/create
             * subboard/{subboardid}/delete
             * subboard/{subboardid}/update
             * subboard/{subboardid}/activities
             **/

            // subboard/create
            if (tokens.length == 5 && tokens[4].equals("create")) {
                SubboardRestResource urr = new SubboardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateSubboard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // subboard/{subboardid}/delete
            else if (tokens.length == 6 && tokens[5].equals("delete")) {
                SubboardRestResource urr = new SubboardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteSubboard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // subboard/{subboardid}/update
            else if (tokens.length == 6 && tokens[5].equals("update")) {
                SubboardRestResource urr = new SubboardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateSubboards();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // subboard/{subboardid}/activities
            else if (tokens.length == 6 && tokens[5].equals("activities")) {
                SubboardRestResource urr = new SubboardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetActivitiesBySubboardId();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // subboard/{subboardid}
            else if (tokens.length == 5 && Integer.parseInt(tokens[4])%1==0) {
                SubboardRestResource urr = new SubboardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetSubboardById();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            else {
                return false;
            }

        } catch (NumberFormatException e) {
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
            logger.error("stacktrace:", e);
        } catch (NamingException | SQLException e) {
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }

        return true;
    }
}
