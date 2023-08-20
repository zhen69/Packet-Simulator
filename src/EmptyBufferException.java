/**
 * The EmptyBufferException class inherits all properties and behaviors of the Exception class.
 * This exception is thrown when attempting to remove packets from an empty router.
 *
 * @author Zhen Wei Liao
 **/
public class EmptyBufferException extends Exception{
    /**
     * Constructor method calls the constructor of the Exception class.
     */

    public EmptyBufferException() {
        super();
    }

    /**
     * Constructor calls the constructor of the Exception class,
     * while changing the display error message to the given error variable.
     *
     * @param error
     * 		Error message to display when this exception is thrown.
     * */
    public EmptyBufferException(String error) {
        super(error);
    }
}


