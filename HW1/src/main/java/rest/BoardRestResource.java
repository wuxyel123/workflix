package rest;

import dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.Board;
import utils.ErrorCode;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class represents the REST resource "/board"
 */
public class BoardRestResource extends RestResource{

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
    public BoardRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Get a board
     * @throws IOException Error in IO operations
     */
    public void GetBoardById() throws IOException {
        try {
            Board board = new Board();
            board.setBoardId(Integer.parseInt(tokens[3]));
            if (new GetBoardByIdDatabase(con, board).getBoardById() == null) {
                initError(ErrorCode.BOARD_NOT_FOUND);
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
     * Get all boards of a workspace
     * @throws IOException Error in IO operations
     */
    public void GetBoardsByWorkspaceId() throws IOException {
        try {
            Board board = new Board();
            board.setWorkspaceId(Integer.parseInt(tokens[3]));
            if (new GetBoardByWorkspaceIdDatabase(con, board).getBoardByWorkspaceId() == null) {
                initError(ErrorCode.BOARD_NOT_FOUND);
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
     * Create a board
     * @throws IOException Error in IO operations
     */
    public void CreateBoard() throws IOException {
        try {
            Board board = Board.fromJSON(req.getInputStream());
            Board newboard = new InsertBoardDatabase(con, board).addBoard();

            if (newboard == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newboard.toJSON().toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    /**
     * Update a board
     * @throws IOException Error in IO operations
     */
    public void UpdateBoard() throws IOException {
        try {
            Board board = Board.fromJSON(req.getInputStream());
            board.setBoardId(Integer.parseInt(tokens[4]));
            Board newboard = new UpdateBoardDatabase(con, board).updateBoard();

            if (newboard == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                response = newboard.toJSON().toString();

            }
        } catch (SQLException e) {
            ec = ErrorCode.OK;
            res.setContentType("application/json");
        } finally {
            respond();
        }

    }

    /**
     * Delete a board
     * @throws IOException Error in IO operations
     */
    public void DeleteBoard() throws IOException {
        try {
            Board board = new Board();
            board.setBoardId(Integer.parseInt(tokens[4]));
            if (new DeleteBoardDatabase(con, board).deleteBoard() == null) {
                initError(ErrorCode.BOARD_NOT_FOUND);
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
