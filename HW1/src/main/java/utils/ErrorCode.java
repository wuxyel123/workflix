package utils;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public enum ErrorCode {
    OK(0, HttpServletResponse.SC_OK, "OK."),
    USER_NOT_FOUND(-100, HttpServletResponse.SC_NOT_FOUND, "User not found."),
    USER_ALREADY_EXISTS(-101, HttpServletResponse.SC_CONFLICT, "User already exists."),
    USER_NOT_AUTHORIZED(-102, HttpServletResponse.SC_UNAUTHORIZED, "User not authorized."),
    TEMPLATE_NOT_FOUND(-103, HttpServletResponse.SC_NOT_FOUND, "Template not found."),
    TEMPLATE_INFORMATION_MISSING(-104, HttpServletResponse.SC_BAD_REQUEST, "Template information missing upon making a request."),
    TEMPLATE_NAME_ALREADY_EXIST(105, HttpServletResponse.SC_CONFLICT, "Template already exists."),
    ASSIGNEE_NOT_FOUND(-108, HttpServletResponse.SC_NOT_FOUND, "Assignee not found."),
    COMMENT_NOT_FOUND(-109, HttpServletResponse.SC_NOT_FOUND, "Comment not found."),
    ANALYTICS_NOT_FOUND(-106,HttpServletResponse.SC_NOT_FOUND, "Analytics not found."),
    WORKSPACE_NOT_FOUND(-107,HttpServletResponse.SC_NOT_FOUND, "Workspace not found."),
    TEMPLATE_CREATED(-200,HttpServletResponse.SC_CREATED, "Template inserted correctly."),
    USER_CREATED(-201,HttpServletResponse.SC_CREATED, "User inserted correctly."),
    WORKSPACE_CREATED(-202,HttpServletResponse.SC_CREATED, "workspace inserted correctly."),
    BOARD_CREATED(-203,HttpServletResponse.SC_CREATED, "board inserted correctly."),
    COMMENT_CREATED(-204,HttpServletResponse.SC_CREATED, "comment inserted correctly."),


    INTERNAL_ERROR(-999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error."),
    METHOD_NOT_ALLOWED(-1000, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed."),
    WRONG_REST_FORMAT(-1002, HttpServletResponse.SC_BAD_REQUEST, "Wrong REST format."),
    OPERATION_UNKNOWN(-1003, HttpServletResponse.SC_BAD_REQUEST, "Operation unknown.");

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
