


public class User {

    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final String DESCRIPTION = "description";
    public static final String CREATE_DATE = "create_date";

    private Integer userId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String description;
    private Date createDate;

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /** Get a list of User objects from an InputStream
     *
     * @param inputStream InputStream containing a list of user data
     * @return List of User objects
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static List<User> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<User> users = new ArrayList<>();
        JSONArray usersJSONList = jobj.getJSONArray("users-list");

        for(int i=0; i<usersJSONList.length(); i++){
            users.add(fromJSON(usersJSONList.getJSONObject(i)));
        }

        return users;
    }

    /** Get a User object from an InputStream
     *
     * @param inputStream InputStream containing the user data
     * @return User object
     * @throws IOException if an I/O error occurs
     * @throws JSONException if the input is not valid JSON
     */
    public static User fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    /** Get a User object from a JSONObject
     *
     * @param jobj JSONObject containing the user data
     * @return User object
     * @throws JSONException if the input is not valid JSON
     */
    public static User fromJSON(JSONObject jobj) throws JSONException {

        Integer userId = jobj.getInt(USER_ID);
        String username = jobj.getString(USERNAME);
        String password = jobj.getString(PASSWORD);
        String email = jobj.getString(EMAIL);
        String firstName = jobj.getString(FIRST_NAME);
        String lastName = jobj.getString(LAST_NAME);
        String profilePicture = jobj.getString(PROFILE_PICTURE);
        String description = jobj.getString(DESCRIPTION);
        Date createDate = java.util.Date
                .from(LocalDateTime.parse(jobj.getString(CREATE_DATE)).atZone(ZoneId.systemDefault())
                        .toInstant());

        // Create User object, set values and return. Constructor is not used cause it's not clean with so many parameters.
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setProfilePicture(profilePicture);
        user.setDescription(description);
        user.setCreateDate(createDate);

        return user;

    }
    /** Get a JSONObject from a User object
     *
     * @return JSONObject containing the user data
     * @throws JSONException if the input is not valid JSON
     */
    public JSONObject toJSON() throws JSONException {

        JSONObject userJSON = new JSONObject();
        userJSON.put(USER_ID, userId);
        userJSON.put(USERNAME, username);
        userJSON.put(PASSWORD, password);
        userJSON.put(EMAIL, email);
        userJSON.put(FIRST_NAME, firstName);
        userJSON.put(LAST_NAME, lastName);
        userJSON.put(PROFILE_PICTURE, profilePicture);
        userJSON.put(DESCRIPTION, description);
        userJSON.put(CREATE_DATE, createDate.toString());

        return userJSON;
    }



}

public class UserRestResource extends RestResource{

    protected final String op;
    protected ErrorCode ec = null;
    protected String response = null;
    protected final String[] tokens;

    public UserRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * Create a user
     * @throws IOException Error in IO operations
     */
    public void CreateUser() throws IOException {
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new InsertUserDatabase(con, user).insertUser();
            if (newUser == null) {
                initError(ErrorCode.USER_ALREADY_EXISTS);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Get a user
     * @throws IOException Error in IO operations
     */
    public void GetUserFromId() throws IOException{
        try {
            User user = getUserFromId(Integer.parseInt(tokens[4]));
            if (new GetUserByIdDatabase(con, user).getUserById()==null) {
                initError(ErrorCode.USER_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Get user from mail and password
     * @throws IOException Error in IO operations
     */
    public void GetUserFromMailAndPassword() throws IOException{
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new GetUserByMailPasswordDatabase(con, user).getUserByMailAndPassword();
            if (newUser == null) {
                User checkUser = new GetUserByMailDatabase(con, user).getUserByMail();
                if (checkUser == null) {
                    initError(ErrorCode.USER_NOT_FOUND);
                } else {
                    initError(ErrorCode.USER_NOT_AUTHORIZED);
                }
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Update a user
     * @throws IOException Error in IO operations
     */
    public void UpdateUserNoPassword() throws IOException{
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new UpdateUserDatabase(con, user).updateUser();
            if (newUser == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Update user password
     * @throws IOException Error in IO operations
     */
    public void UpdateUserPassword() throws IOException{
        try {
            User user = User.fromJSON(req.getInputStream());
            User newUser = new UpdateUserPasswordDatabase(con, user).updateUserPassword();
            if (newUser == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newUser.toJSON().toString();
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }
    }

    /**
     * Delete a user
     * @throws IOException Error in IO operations
     */
    public void DeleteUser() throws IOException{
        try {
            User user = new User();
            user.setUserId(Integer.parseInt(tokens[4]));
            if (new DeleteUserDatabase(con, user).deleteUser()==null) {
                initError(ErrorCode.USER_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e){
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally { respond(); }

    }

    private void respond() throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(response);
    }
    private void initError(ErrorCode ec){
        this.ec = ec;
        response = ec.toJSON().toString();
    }

    private User getUserFromId(Integer id) throws SQLException{
        User user = new User();
        user.setUserId(id);
        return new GetUserByIdDatabase(con, user).getUserById();
    }
}

public class RestDispatcherServlet extends AbstractServlet{



    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        if(processUser(req, res)){
            return;
        }

        if(processWorkspace(req, res)){
            return;
        }

        if(processComment(req, res)){
            return;
        }

        if (processAssignee(req, res)) {
            return;
        }

        if (processBoard(req, res)) {
            return;
        }
        String op = req.getRequestURI();
        writeError(res, ErrorCode.OPERATION_UNKNOWN);
        logger.warn("requested op " + op);

    }

    /**
     * Process user rest dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processUser(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        //the first token will always be the empty;
        //the second will be the context;
        //the third should be "user";
        if (tokens.length<4 || !tokens[3].equals("user")){
            return false;
        }
        try{
            /**User APIs are:
             *  user/login
             *  user/register
             *  user/logout
             *  user/delete/{userid}
             *  user/update/{userid}
             *  user/update/{userid}/password
             *  user/{userid}
             * */
            // user/login
            if (tokens.length==4 && tokens[3].equals("login")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.GetUserFromMailAndPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/register
            else if (tokens.length==4 && tokens[3].equals("register")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/delete/{userid}
            else if (tokens.length==5 && tokens[3].equals("delete")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/update/{userid}
            else if (tokens.length==5 && tokens[3].equals("update")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateUserNoPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/update/{userid}/password
            else if (tokens.length==6 && tokens[3].equals("update") && tokens[5].equals("password")){
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateUserPassword();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // user/{userid}
            else if (tokens.length==4 && Integer.parseInt(tokens[3])%1==0){
                Integer.parseInt(tokens[4]);
                UserRestResource urr = new UserRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetUserFromId();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            } else {
                return  false;
            }

        }
        catch (NumberFormatException e){
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
        }
        catch (NamingException | SQLException e){
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }


        return true;
    }

    /**
     * Process Workspace rest dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processWorkspace(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("workspace")) {
            return false;
        }
        try {
            /**
             * workspace APIs are:
             * /workspace/{workspaceid}
             * /workspace/create
             * /workspace/delete/{workspaceid}
             * /workspace/update/{workspaceid}
             * /workspace/{workspaceid}/adduser
             * /workspace/{workspaceid}/removeuser
             * workspace/[workspaceid}/assignuserpermission
             **/

            // workspace/{workspaceid}
            if (tokens.length == 4 && Integer.parseInt(tokens[3]) % 1 == 0) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.GetWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // workspace/create
            else if (tokens.length == 4 && tokens[3].equals("create")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // workspace/delete/{workspaceid}
            else if (tokens.length == 5 && tokens[3].equals("delete")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // workspace/update/{workspaceid}
            else if (tokens.length == 5 && tokens[3].equals("update")) {
                WorkspaceRestResource urr = new WorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.UpdateWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/adduser
            else if (tokens.length == 5 && tokens[5].equals("adduser")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.AddUser();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/removeuser
            else if (tokens.length == 5 && tokens[4].equals("removeuser")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteUserWorkSpace();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }

            // /workspace/{workspaceid}/assignuserpermission
            else if (tokens.length == 5 && tokens[4].equals("assignuserpermission")) {
                UserWorkspaceRestResource urr = new UserWorkspaceRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "PUT" -> urr.AssignUserPermission();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            } else {
                return false;
            }


        } catch (NumberFormatException e) {
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
        } catch (NamingException | SQLException e) {
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }

        return true;
    }

    /**
     * Process comment rest dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processComment(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("comment")) {
            return false;
        }
        try {
            /**
             * Activity APIs are:
             * activity/comment/get
             * activity/comment/add
             **/

            // assignee/get
            if (tokens.length == 5 && tokens[4].equals("get")) {
                CommentRestResource urr = new CommentRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.getComments();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // assignee/add
            else if (tokens.length == 5 && tokens[4].equals("add")) {
                CommentRestResource urr = new CommentRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.addComments();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            } else {
                return false;
            }

        } catch (NumberFormatException e) {
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
        } catch (NamingException | SQLException e) {
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }

        return true;
    }

    /**
     * Process assignee rest dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processAssignee(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("assignee")) {
            return false;
        }
        try {
            /**
             * Activity APIs are:
             * activity/assignee/get
             * activity/assignee/add
             * activity/assignee/remove
             **/

            // assignee/get
            if (tokens.length == 5 && tokens[4].equals("get")) {
                AssigneeRestResource urr = new AssigneeRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "GET" -> urr.getAssignee();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // assignee/add
            else if (tokens.length == 5 && tokens[4].equals("add")) {
                AssigneeRestResource urr = new AssigneeRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.addAssignee();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // assignee/remove
            else if (tokens.length == 5 && tokens[4].equals("remove")) {
                AssigneeRestResource urr = new AssigneeRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.deleteAssignee();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            } else {
                return false;
            }

        } catch (NumberFormatException e) {
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
        } catch (NamingException | SQLException e) {
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }

        return true;
    }

    /**
     * Process user board dispatcher
     * @param req Request
     * @param res Response
     * @return true if the operation is processed, false otherwise
     * @throws IOException Error in IO operations
     * */
    private boolean processBoard(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String op = req.getRequestURI();
        String[] tokens = op.split("/");
        // the first token will always be the empty;
        // the second will be the context;
        // the third should be "activity";
        if (tokens.length < 5 || !tokens[4].equals("board")) {
            return false;
        }
        try {
            /**
             * Board APIs are:
             * Board/{boardid}
             * board/create
             * board/update/{boardid}
             **/

            // board/get
            if (tokens.length == 5 && tokens[4].equals("get")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    //case "GET" -> urr.get();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // board/create
            else if (tokens.length == 5 && tokens[4].equals("add")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.CreateBoard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // board/delete
            else if (tokens.length == 5 && tokens[3].equals("remove")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "DELETE" -> urr.DeleteBoard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            }
            // board/update
            else if (tokens.length == 5 && tokens[3].equals("update")) {
                BoardRestResource urr = new BoardRestResource(req, res, getDataSource().getConnection());
                switch (req.getMethod()) {
                    case "POST" -> urr.UpdateBoard();
                    default -> writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                }
            } else {
                return false;
            }

        } catch (NumberFormatException e) {
            writeError(res, ErrorCode.WRONG_REST_FORMAT);
        } catch (NamingException | SQLException e) {
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }

        return true;
    }

}

public class GetUserByIdDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.users WHERE user_id=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    User user;

    public GetUserByIdDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    public User getUserById() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setInt(1, user.getUserId());

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString(User.USERNAME));
                user.setEmail(rs.getString(User.EMAIL));
                user.setFirstName(rs.getString(User.FIRST_NAME));
                user.setLastName(rs.getString(User.LAST_NAME));
                user.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                user.setDescription(rs.getString(User.DESCRIPTION));

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
        return user;
    }

}
public class GetUserByMailDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.users WHERE email=?;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    User user;

    public GetUserByMailDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    public User getUserByMail() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getEmail());

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString(User.USERNAME));
                user.setEmail(rs.getString(User.EMAIL));
                user.setFirstName(rs.getString(User.FIRST_NAME));
                user.setLastName(rs.getString(User.LAST_NAME));
                user.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                user.setDescription(rs.getString(User.DESCRIPTION));

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

        return user;
    }

}
public class GetUserByMailPasswordDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM workflix.users WHERE email=? AND password=md5(?);";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be searched
     */
    User user;

    public GetUserByMailPasswordDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    public User getUserByMailAndPassword() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user=null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString(User.USERNAME));
                user.setEmail(rs.getString(User.EMAIL));
                user.setFirstName(rs.getString(User.FIRST_NAME));
                user.setLastName(rs.getString(User.LAST_NAME));
                user.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                user.setDescription(rs.getString(User.DESCRIPTION));
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

        return user;
    }
}
public class InsertUserDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO workflix.users(username, password, email, first_name, last_name, profile_picture, description) VALUES (?, md5(?), ?, ?, ?, ?, ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    User user;

    public InsertUserDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    public User insertUser() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        User newUser = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getProfilePicture());
            ps.setString(7, user.getDescription());
            rs = ps.executeQuery();

            if (rs.next()) {
                newUser = new User();
                newUser.setUsername(rs.getString(User.USERNAME));
                newUser.setPassword(rs.getString(User.PASSWORD));
                newUser.setEmail(rs.getString(User.EMAIL));
                newUser.setFirstName(rs.getString(User.FIRST_NAME));
                newUser.setLastName(rs.getString(User.LAST_NAME));
                newUser.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                newUser.setDescription(rs.getString(User.DESCRIPTION));
                newUser.setCreateDate(rs.getTimestamp(User.CREATE_DATE));
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

        return newUser;
    }
}
public class UpdateUserDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE workflix.users SET username=?, email=?, first_name=?, last_name=?, profile_picture=?, description=? WHERE user_id=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be inserted
     */
    User user;

    public UpdateUserDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    public User updateUser() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        User newUser = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getProfilePicture());
            ps.setString(6, user.getDescription());
            ps.setInt(7, user.getUserId());
            rs = ps.executeQuery();

            if (rs.next()) {
                newUser = new User();
                newUser.setUsername(rs.getString(User.USERNAME));
                newUser.setEmail(rs.getString(User.EMAIL));
                newUser.setFirstName(rs.getString(User.FIRST_NAME));
                newUser.setLastName(rs.getString(User.LAST_NAME));
                newUser.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                newUser.setDescription(rs.getString(User.DESCRIPTION));
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

        return newUser;
    }
}

public class DeleteUserDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM workflix.users WHERE email=? RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be deleted
     */
    User user;

    public DeleteUserDatabase(final Connection con, final User u) {
        this.con = con;
        this.user = u;
    }

    public User deleteUser() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        // the created user
        User deletedUser = null;

        try {
            ps = con.prepareStatement(STATEMENT);
            ps.setString(1, user.getEmail());

            rs = ps.executeQuery();

            if (rs.next()) {
                deletedUser = new User();
                deletedUser.setUsername(rs.getString(User.USERNAME));
                deletedUser.setPassword(rs.getString(User.PASSWORD));
                deletedUser.setEmail(rs.getString(User.EMAIL));
                deletedUser.setFirstName(rs.getString(User.FIRST_NAME));
                deletedUser.setLastName(rs.getString(User.LAST_NAME));
                deletedUser.setProfilePicture(rs.getString(User.PROFILE_PICTURE));
                deletedUser.setDescription(rs.getString(User.DESCRIPTION));

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
        return deletedUser;
    }

}
