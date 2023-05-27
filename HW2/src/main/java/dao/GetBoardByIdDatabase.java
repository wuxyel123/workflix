package dao;

import resource.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for getting a board from the database
 */
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

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param b   the board to be searched
     */
    public GetBoardByIdDatabase(final Connection con, final Board b) {
        this.con = con;
        this.board = b;
    }

    /**
     * Get the board from the database
     *
     * @return the board
     * @throws SQLException if an error occurred while trying to get the board
     */
    public Board getBoardById() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Board res=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, board.getBoardId());

            rs = ps.executeQuery();

            if (rs.next()) {
                res = new Board();
                res.setBoardId(rs.getInt(Board.BOARD_ID));
                res.setWorkspaceId(rs.getInt(Board.WORKSPACE_ID));
                res.setName(rs.getString(Board.NAME));
                res.setDescription(rs.getString(Board.DESCRIPTION));
                res.setVisibility(rs.getString(Board.VISIBILITY));
                res.setCreateTime(rs.getDate(Board.CREATE_TIME));
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
        return res;
    }

}
