package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for getting a template from the database
 */
public class GetTemplateByNameDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.template WHERE template_name=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The park to be searched
     */
    Template template;

    /**
     * Initialize the DAO object with a connection to the database and the object to be searched
     *
     * @param con the connection to the database
     * @param template   the template to be searched
     */
    public GetTemplateByNameDatabase(final Connection con, final Template template) {
        this.con = con;
        this.template=template;
    }

    /**
     * Get the template from the database
     *
     * @return the template
     * @throws SQLException if an error occurred while trying to get the template
     */
    public Template getTemplateByName() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the created park
        Template template = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, template.getTemplateName());

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
