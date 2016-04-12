/**
 * Created by loomisdf on 4/11/2016.
 */
public class ErrorCode {
    ErrorResult result;

    public String message;

    public ErrorCode(ErrorResult result, String message) {
        this.result = result;
        this.message = message;
    }
}
