package dao;

import resource.Board;
import resource.WorkSpace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO class responsible for getting a board from the database */
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
     * The workspace in which to search the board
     */
    WorkSpace workspace;

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param b   the board to be searched
     */
    public GetBoardByWorkspaceIdDatabase(final Connection con, final WorkSpace w) {
        this.con = con;
        this.workspace = w;
    }

    /**
     * Get the board from the database
     *
     * @return the board
     * @throws SQLException if an error occurred while trying to get the board
     */
    public List<Board> getBoardByWorkspaceId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Board> boards = new ArrayList<>();

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, workspace.getWorkspaceId());

            rs = ps.executeQuery();

            while (rs.next()) {
                Board board = new Board();
                board.setBoardId(rs.getInt(Board.BOARD_ID));
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
