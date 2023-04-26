package dao;
import resource.Board;
import resource.Comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
public class CreateBoardDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.board VALUES(?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    Board board;
    public CreateBoardDatabase(final Connection con, final Board board) {
        this.con = con;
        this.board = board;
    }
    public Board addBoard() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Board addBoard = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, board.getBoardId());

            rs = ps.executeQuery();

            if (rs.next()) {
                addBoard = new Board();
                addBoard.setCreateTime(LocalDateTime.parse(rs.getString(Board.CREATE_TIME)));
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