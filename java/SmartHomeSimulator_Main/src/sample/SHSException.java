package sample;

/**
 * Exception class for SHS-related exceptions
 */
public class SHSException extends Exception {

    /**
     * Constructor for SHS-Related exceptions
     * @param errorMessage
     */
    public SHSException(String errorMessage) {
        super(errorMessage);
    }
}

