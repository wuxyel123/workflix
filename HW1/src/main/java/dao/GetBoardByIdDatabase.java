package dao;

import resource.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetBoardByIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.board WHERE board_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The board to be searched
     */
    Board board;

    public GetBoardByIdDatabase(final Connection con, final Board b) {
        this.con = con;
        this.board = b;
    }

    public Board getBoardById() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Board board=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, board.getBoardId());

            rs = ps.executeQuery();

            if (rs.next()) {
                board = new Board();
                board.setWorkspaceId(rs.getInt(Board.WORKSPACE_ID));
                board.setName(rs.getString(Board.NAME));
                board.setDescription(rs.getString(Board.DESCRIPTION));
                board.setVisibility(rs.getString(Board.VISIBILITY));
                board.setCreateTime(rs.getDate(Board.CREATE_TIME));
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
        return board;
    }

}
