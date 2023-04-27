package dao;
import resource.Board;

import java.sql.*;
import java.time.LocalDateTime;
public class InsertBoardDatabase {
    /**
     * The SQL statement to be executed
     *
     */
    private static final String STATEMENT = "INSERT INTO workflix.board VALUES(?,?,?,?,?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Board board;
    public InsertBoardDatabase(final Connection con, final Board board) {
        this.con = con;
        this.board = board;
    }
    public Board addBoard() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Board addBoard = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setNull(1, Types.NULL);
            ps.setString(2, board.getName());
            ps.setString(3, board.getDescription());
            ps.setString(4, board.getVisibility());
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()));

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
