/**
 * The FullBufferException class inherits all properties and behaviors of the Exception class.
 *
 * @author Zhen Wei Liao
 **/
public class FullBufferException extends Exception{
    /**
     * Constructor method calls the constructor of the Exception class.
     */

    public FullBufferException() {
        super();
    }

    /**
     * Constructor calls the constructor of the Exception class,
     * while changing the display error message to the given error variable.
     *
     * @param error
     * 		Error message to display when this exception is thrown.
     * */
    public FullBufferException(String error) {
        super(error);
    }
}

