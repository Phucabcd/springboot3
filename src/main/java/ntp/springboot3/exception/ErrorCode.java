package ntp.springboot3.exception;


public enum ErrorCode {
    UnCATEGORIZED_EXCEPTION(1001, "Uncategorized Exception"),
    INVALID_KEy(1005, "invalid message key"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_INVALID(1003, "Username must be at least 2 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 6 characters"),
    ;


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
