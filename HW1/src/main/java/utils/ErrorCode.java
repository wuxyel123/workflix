package utils;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
public enum ErrorCode {
<<<<<<< HEAD

    /**
     * 200
     * OK
     * Indicates that the request has succeeded.
     * 201
     * Created
     * Indicates that the request has succeeded and a new
     * user/workspace/board/subboard/comment has been created as a result.
     * 400
     * BAD REQUEST
     * Request made to the server without specifying all the mandatory params
     * 401
     * Unauthorized
     * Failed login attempt due to unauthorized user information.
     * 403
     * Forbidden
     * Unauthorized request. The client does not have access rights to the content.
     * 404
     * NOT FOUND
     * The server can not find the requested resource. Use a broken link or a
     * mistyped URL.
     * 409
     * CONFLICT
     * Resource of the same type already used.
     * 500
     * Internal Server Error
     * The server encountered an unexpected condition that prevented it from
     * fulfilling the request.
     */
    OK(0, HttpServletResponse.SC_OK, "OK."),
    USER_NOT_FOUND(-100, HttpServletResponse.SC_NOT_FOUND, "User not found."),
    USER_ALREADY_EXISTS(-101, HttpServletResponse.SC_CONFLICT, "User already exists."),
    USER_NOT_AUTHORIZED(-102, HttpServletResponse.SC_UNAUTHORIZED, "User not authorized."),
    ASSIGNEE_NOT_FOUND(-200, HttpServletResponse.SC_NOT_FOUND, "Assignee not found."),
    COMMENT_NOT_FOUND(-300, HttpServletResponse.SC_NOT_FOUND, "Comment not found."),
    INTERNAL_ERROR(-999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error."),
    METHOD_NOT_ALLOWED(-1000, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed."),
    BAD_REQUEST(-1001, HttpServletResponse.SC_BAD_REQUEST, "Bad request."),
    WRONG_REST_FORMAT(-1002, HttpServletResponse.SC_BAD_REQUEST, "Wrong REST format."),
    OPERATION_UNKNOWN(-1003, HttpServletResponse.SC_BAD_REQUEST, "Operation unknown.");
=======
    OK(0, HttpServletResponse.SC_OK,"OK."),
    USER_NOT_FOUND(-100, HttpServletResponse.SC_NOT_FOUND, "User not found."),
    USER_ALREADY_EXISTS(-101, HttpServletResponse.SC_CONFLICT, "User already exists."),
    USER_NOT_AUTHORIZED(-102, HttpServletResponse.SC_UNAUTHORIZED, "User not authorized."),
    TEMPLATE_NOT_FOUND(-100, HttpServletResponse.SC_NOT_FOUND, "Template not found."),
    TEMPLATE_INFORMATION_MISSING(-400, HttpServletResponse.SC_BAD_REQUEST, "Template information missing."),
    INTERNAL_ERROR(-999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error.");
>>>>>>> 7ef762a5305784161347fa8d09b57735b194e408

    private final int errorCode;
    private final int httpCode;
    private final String errorMessage;

    ErrorCode(int errorCode, int httpCode, String errorMessage) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getHTTPCode() {
        return httpCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("code", errorCode);
        data.put("message", errorMessage);
        JSONObject info = new JSONObject();
        info.put("error", data);
        return info;
    }
}
