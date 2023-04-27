package dao;
import resource.Board;
import java.sql.*;
import java.time.LocalDateTime;

public class DeleteBoardDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.board WHERE board_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;
    /**
     * The board to be deleted
     */
    Board board;
    public DeleteBoardDatabase(final Connection con, final Board b) {
        this.con = con;
        this.board = b;
    }
    public Board deleteBoard() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        // the created board
        Board deletedBoard = null;
        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, board.getBoardId());
            rs = ps.executeQuery();
            if (rs.next()) {
                deletedBoard = new Board();
                deletedBoard.setName(rs.getString(Board.NAME));
                deletedBoard.setDescription(rs.getString(Board.DESCRIPTION));
                deletedBoard.setVisibility(rs.getString(Board.VISIBILITY));
                deletedBoard.setCreateTime(rs.getDate(Board.CREATE_TIME));
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
        return deletedBoard;
    }
}
