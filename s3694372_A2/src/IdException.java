/*
* Class: IdException
* Description: When an id isn't valid, an id exception is thrown
* Author: Labiba Islam - 3694372
*/
public class IdException extends Exception {

	String message;

	public IdException(String message) {
		super(message);
	}

}
