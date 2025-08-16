package ziyad.demo.exception;

public class UnauthorizedVoterException extends RuntimeException {
    public UnauthorizedVoterException(String message) {
        super(message);
    }
}
