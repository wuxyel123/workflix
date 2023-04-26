package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetTemplateByIdDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.template WHERE template_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The park to be searched
     */
    Template template;

    public GetTemplateByIdDatabase(final Connection con, final Template template) {
        this.con = con;
        this.template=template;
    }

    public Template getTemplateById() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created park
        Template template = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, template.getTemplateId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                template=new Template();
                template.setImageUrl(rs.getString(Template.IMAGE_URL));
                template.setTemplateName(rs.getString(Template.TEMPLATE_NAME));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            con.close();
        }

        return template;
    }
}
