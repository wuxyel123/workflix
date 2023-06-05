package rest;

import dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.Activities;
import resource.Board;
import resource.Subboard;
import utils.ErrorCode;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This class represents the REST resource "/subboard"
 */
public class SubboardRestResource extends RestResource {

    // The operation requested by the client
    protected final String op;
    // The error code
    protected ErrorCode ec = ErrorCode.OK;
    // The response
    protected String response = "";
    // The tokens of the request
    protected final String[] tokens;

    /**
     * Constructor
     * @param req The request
     * @param res The response
     * @param con The connection to the database
     */
    public SubboardRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Get all the subboards of a board
     * @throws IOException Error in IO operations
     */
    public void GetSubboardsByBoardId() throws IOException {
        try {
            Board board = new Board();
            board.setBoardId(Integer.parseInt(tokens[4]));
            List<Subboard> subboards = new GetSubboardByBoardIdDatabase(con, board).getSubboardByBoardId();
            if (subboards == null || subboards.isEmpty()) {
                initError(ErrorCode.SUBBOARD_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = Subboard.toJSONList(subboards).toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    /**
     * Get a subboard
     * @throws IOException Error in IO operations
     */
    public void GetSubboardById() throws IOException {
        try {
            Subboard subboard = new Subboard();
            subboard.setSubboardId(Integer.parseInt(tokens[4]));
            subboard = new GetSubboardDatabase(con, subboard).getSubboard();
            if (subboard == null) {
                initError(ErrorCode.SUBBOARD_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = subboard.toJSON().toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    /**
     * Create a subboard
     * @throws IOException Error in IO operations
     */
    public void CreateSubboard() throws IOException {
        try {
            Subboard subboard =  Subboard.fromJSON(req.getInputStream());
            subboard = new InsertSubboarDatabase(con, subboard).insertSubboards();

            if (subboard == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = subboard.toJSON().toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    /**
     *  update the subboards
     * @throws IOException Error in IO operations
     **/
    public void UpdateSubboards() throws IOException {
        try {
            Subboard subboard = Subboard.fromJSON(req.getInputStream());
            subboard.setSubboardId(Integer.parseInt(tokens[4]));
            subboard = new UpdateSubboardDatabase(con,subboard).UpdateSubboards();

            if (subboard == null) {
                initError(ErrorCode.SUBBOARD_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = subboard.toJSON().toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }

    }

    /**
     * Delete a subboard
     * @throws IOException Error in IO operations
     */
    public void DeleteSubboard() throws IOException {
        try {
            Subboard subboard = Subboard.fromJSON(req.getInputStream());
            subboard.setSubboardId(Integer.parseInt(tokens[4]));
            subboard = new DeleteSubboardsDatabase(con, subboard).DeleteSubboard();
            if (subboard == null) {
                initError(ErrorCode.SUBBOARD_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = subboard.toJSON().toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }

    }

    /** Get all activities of a subboard
     * @throws IOException Error in IO operations
     */
    public void GetActivitiesBySubboardId() throws IOException {
        try {
            Subboard subboard = new Subboard();
            subboard.setSubboardId(Integer.parseInt(tokens[4]));
            List<Activities> activities = new GetActivitiesBySubboardIdDatabase(con, subboard).getActivitiesBySubboardId();
            if (activities == null || activities.isEmpty()) {
                initError(ErrorCode.ACTIVITY_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = Activities.toJSONList(activities).toString();
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