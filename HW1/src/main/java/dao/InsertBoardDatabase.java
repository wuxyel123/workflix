package dao;
import resource.Board;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * DAO class responsible for inserting a board to the database
 */
public class InsertBoardDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.board(workspace_id, name, description, visibility) VALUES(?,?,?,?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The board to be inserted
     */
    Board board;

    /**
     * Initialize the DAO object with a connection to the database and the object to be inserted
     *
     * @param con the connection to the database
     * @param board   the board to be inserted
     */
    public InsertBoardDatabase(final Connection con, final Board board) {
        this.con = con;
        this.board = board;
    }

    /**
     * Insert the board from the database
     *
     * @return the board
     * @throws SQLException if an error occurred while trying to insert the board
     */
    public Board addBoard() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Board addBoard = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, board.getWorkspaceId());
            ps.setString(2, board.getName());
            ps.setString(3, board.getDescription());
            ps.setString(4, board.getVisibility());

            rs = ps.executeQuery();

            if (rs.next()) {
                addBoard = new Board();
                addBoard.setCreateTime(rs.getDate(Board.CREATE_TIME));
                addBoard.setVisibility(rs.getString(Board.VISIBILITY));
                addBoard.setDescription(rs.getString(Board.DESCRIPTION));
                addBoard.setName(rs.getString(Board.NAME));
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            con.close();
        }
        return addBoard;
    }

}
