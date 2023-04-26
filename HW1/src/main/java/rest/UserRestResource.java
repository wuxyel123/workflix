package rest;

import dao.GetUserByIdDatabase;
import dao.InsertUserDatabase;
import dao.UpdateUserDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ErrorCode;

import dao.DeleteUserDatabase;

import resource.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UserRestResource extends RestResource{

    protected final String op;
    protected ErrorCode ec = null;
    protected String response = null;
    protected final String[] tokens;

    public UserRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Create a user
     * @throws IOException Error in IO operations
     */
    public void CreateUser() throws IOException {
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new InsertUserDatabase(con, user).insertUser();
            if (newUser == null) {
                initError(ErrorCode.USER_ALREADY_EXISTS);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Get a user
     * @throws IOException Error in IO operations
     */
    public void GetUser() throws IOException{
        try {
            User user = getUserFromId(Integer.parseInt(tokens[5]));
            if (new GetUserByIdDatabase(con, user).getUserById()==null) {
                initError(ErrorCode.USER_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Update a user
     * @throws IOException Error in IO operations
     */
    public void UpdateUserNoPassword() throws IOException{
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new UpdateUserDatabase(con, user).updateUser();
            if (newUser == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Delete a user
     * @throws IOException Error in IO operations
     */
    public void DeleteUser() throws IOException{
        try {
            User user = new User();
            user.setUserId(Integer.parseInt(tokens[5]));
            if (new DeleteUserDatabase(con, user).deleteUser()==null) {
                initError(ErrorCode.USER_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }

    }

    private void respond() throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(response);
    }
    private void initError(ErrorCode ec){
        this.ec = ec;
        response = ec.toJSON().toString();
    }

    private User getUserFromId(int id) throws SQLException{
        User user = new User();
        user.setUserId(Integer.parseInt(tokens[5]));
        return new GetUserByIdDatabase(con, user).getUserById();
    }
}
