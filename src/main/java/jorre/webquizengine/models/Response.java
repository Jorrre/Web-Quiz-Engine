package jorre.webquizengine.models;

public class Response {

    private final static String CORRECT = "Correct!";
    private final static String INCORRECT = "Incorrect, please try again.";

    private final boolean success;
    private final String feedback;

    public Response(boolean success) {
        this.success = success;
        this.feedback = success ? CORRECT : INCORRECT;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFeedback() {
        return feedback;
    }
}
