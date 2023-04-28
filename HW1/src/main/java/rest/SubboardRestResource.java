package rest;

import dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.Subboard;
import utils.ErrorCode;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SubboardRestResource extends RestResource {
    protected final String op;
    protected ErrorCode ec = null;
    protected String response = null;
    protected final String[] tokens;

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
            Subboard subboard = new Subboard();
            subboard.setBoardId(Integer.parseInt(tokens[3]));
            if (new GetSubboardByBoardIdDatabase(con, subboard).getSubboardByBoardId() == null) {
                initError(ErrorCode.SUBBOARD_NOT_FOUND);
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

    /**
     * Get a subboard
     * @throws IOException Error in IO operations
     */
    public void GetSubboardById() throws IOException {
        try {
            Subboard subboard = new Subboard();
            subboard.setSubboardId(Integer.parseInt(tokens[3]));
            if (new GetSubboardDatabase(con,subboard).getSubboard() == null) {
                initError(ErrorCode.SUBBOARD_NOT_FOUND);
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

    /**
     * Create a subboard
     * @throws IOException Error in IO operations
     */
    public void CreateSubboard() throws IOException {
        try {
            Subboard subboard =  Subboard.fromJSON(req.getInputStream());
            Subboard newsubboard = new InsertSubboarDatabase(con, subboard).insertSubboards();

            if (newsubboard == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newsubboard.toJSON().toString();
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
            subboard.setSubboardId(Integer.parseInt(tokens[3]));
            Subboard newsubboard = new UpdateSubboardDatabase(con,subboard).UpdateSubboards();

            if (newsubboard == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                response = newsubboard.toJSON().toString();
            }
        } catch (SQLException e) {
            ec = ErrorCode.OK;
            res.setContentType("application/json");
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
            Subboard subboard = new Subboard();
            subboard.setSubboardId(Integer.parseInt(tokens[3]));
            if (new DeleteSubboardsDatabase(con, subboard).DeleteSubboard() == null) {
                initError(ErrorCode.INTERNAL_ERROR);
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