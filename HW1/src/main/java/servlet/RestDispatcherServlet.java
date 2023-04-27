package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rest.UserRestResource;
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
            else if (tokens.length==4 && Integer.parseInt(tokens[4])%1==0){
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

}
