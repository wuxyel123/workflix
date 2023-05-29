package utils;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Description: Error codes
 * Input: None
 * Output: None
 * Usage: Used to define error codes to return to the frontend
 */
public enum ErrorCode {
    OK(0, HttpServletResponse.SC_OK, "OK."),
    USER_NOT_FOUND(-100, HttpServletResponse.SC_NOT_FOUND, "User not found."),
    USER_ALREADY_EXISTS(-101, HttpServletResponse.SC_CONFLICT, "User already exists."),
    USER_NOT_AUTHORIZED(-102, HttpServletResponse.SC_UNAUTHORIZED, "User not authorized."),
    TEMPLATE_NOT_FOUND(-103, HttpServletResponse.SC_NOT_FOUND, "Template not found."),
    TEMPLATE_INFORMATION_MISSING(-104, HttpServletResponse.SC_BAD_REQUEST, "Template information missing upon making a request."),
    TEMPLATE_NAME_ALREADY_EXIST(105, HttpServletResponse.SC_CONFLICT, "Template already exists."),
    ANALYTICS_NOT_FOUND(-106,HttpServletResponse.SC_NOT_FOUND, "Analytics not found."),
    WORKSPACE_NOT_FOUND(-107,HttpServletResponse.SC_NOT_FOUND, "Workspace not found."),
    ASSIGNEE_NOT_FOUND(-108, HttpServletResponse.SC_NOT_FOUND, "Assignee not found."),
    COMMENT_NOT_FOUND(-109, HttpServletResponse.SC_NOT_FOUND, "Comment not found."),
    ACTIVITY_NOT_FOUND(-110, HttpServletResponse.SC_NOT_FOUND, "Activity not found."),
    ACTIVITY_INFORMATION_MISSING(-202, HttpServletResponse.SC_BAD_REQUEST, "Activity information missing upon making a request."),
    ACTIVITY_ALREADY_PRESENT(-106, HttpServletResponse.SC_CONFLICT, "Activity already exists."),
    TEMPLATE_ALREADY_PRESENT(-105, HttpServletResponse.SC_CONFLICT, "Template already exists."),
    BOARD_NOT_FOUND(-111, HttpServletResponse.SC_NOT_FOUND, "Board not found."),
    SUBBOARD_NOT_FOUND(-112, HttpServletResponse.SC_NOT_FOUND, "Subboard not found."),
    USER_WORKSPACE_NOT_FOUND(-113, HttpServletResponse.SC_NOT_FOUND, "The given user and workspace are not related to each other."),
    USER_ALREADY_IN_WORKSPACE(-114, HttpServletResponse.SC_CONFLICT, "User already in workspace."),
    ASSIGNEE_ALREADY_PRESENT(-115, HttpServletResponse.SC_CONFLICT, "Assignee already exists."),
    INTERNAL_ERROR(-999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error."),
    METHOD_NOT_ALLOWED(-1000, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed."),
    WRONG_REST_FORMAT(-1002, HttpServletResponse.SC_BAD_REQUEST, "Wrong REST format."),
    OPERATION_UNKNOWN(-1003, HttpServletResponse.SC_BAD_REQUEST, "Operation unknown."),
    INVALID_INPUT(-1004, HttpServletResponse.SC_BAD_REQUEST, "Invalid input."),
    FAILED_DURING_PROCESSING(-9999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed during processing."),;


    // Error code
    private final int errorCode;
    // HTTP code
    private final int httpCode;
    // Error message
    private final String errorMessage;

    /**
     * Constructor
     * @param errorCode error code
     * @param httpCode HTTP code
     * @param errorMessage error message
     */
    ErrorCode(int errorCode, int httpCode, String errorMessage) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Get error code
     * @return error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Get HTTP code
     * @return HTTP code
     */
    public int getHTTPCode() {
        return httpCode;
    }

    /**
     * Get error message
     * @return error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Convert error code to JSON
     * @return JSON object
     */
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("code", errorCode);
        data.put("message", errorMessage);
        JSONObject info = new JSONObject();
        info.put("error", data);
        return info;
    }
}