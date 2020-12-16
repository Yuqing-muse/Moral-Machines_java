/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
/**
 * Customized exception class to handle invalid character exceptions
 * @author Yuqing Chang
 *
 */
public class InvalidCharacteristicException extends Exception {
	
	/**
	 * Constructor of InvalidCharacteristicException class
	 * @param s {@code int} is the number of rows in file
	 */
	public InvalidCharacteristicException(int s) {
		System.out.println("WARNING:  invalid characteristic in config file in line " + s);
	}

}
