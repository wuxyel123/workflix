package dao;

import resource.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UpdateBoardDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.board SET name=?, description=?, visibility=? WHERE board_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The board to be inserted
     */
    Board board;

    /**
     * Initialize the DAO object with a connection to the database and the object to be updated
     *
     * @param con the connection to the database
     * @param b   the board to be updated
     */
    public UpdateBoardDatabase(final Connection con, final Board b) {
        this.con = con;
        this.board = b;
    }

    /**
     * Update the board in the database
     *
     * @return the updated board
     * @throws SQLException if an error occurred while trying to update the board
     */
    public Board updateBoard() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created board
        Board newBoard = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, board.getName());
            ps.setString(2, board.getDescription());
            ps.setString(3, board.getVisibility());
            ps.setInt(4, board.getBoardId());
            rs = ps.executeQuery();

            if (rs.next()) {
                newBoard = new Board();
                newBoard.setCreateTime(rs.getDate(Board.CREATE_TIME));
                newBoard.setVisibility(rs.getString(Board.VISIBILITY));
                newBoard.setDescription(rs.getString(Board.DESCRIPTION));
                newBoard.setName(rs.getString(Board.NAME));
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

        return newBoard;
    }

}
