package dao;

import resource.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetBoardByWorkspaceIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.board WHERE workspace_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The park to be searched
     */
    Board board;

    public GetBoardByWorkspaceIdDatabase(final Connection con, final Board b) {
        this.con = con;
        this.board = b;
    }

    public List<Board> getBoardByWorkspaceId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Board> boards = new ArrayList<>();

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, board.getWorkspaceId());

            rs = ps.executeQuery();

            while (rs.next()) {
                Board board = new Board();
                board.setWorkspaceId(rs.getInt(Board.WORKSPACE_ID));
                board.setName(rs.getString(Board.NAME));
                board.setDescription(rs.getString(Board.DESCRIPTION));
                board.setVisibility(rs.getString(Board.VISIBILITY));
                board.setCreateTime(rs.getDate(Board.CREATE_TIME));
                boards.add(board);
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
        return boards;
    }

}
