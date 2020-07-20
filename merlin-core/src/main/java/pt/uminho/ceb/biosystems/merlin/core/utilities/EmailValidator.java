package pt.uminho.ceb.biosystems.merlin.core.utilities;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailValidator {

	
	public static boolean isEmailValid (String email) {
		boolean isValid = true;
		try {
			InternetAddress emailAddress = new InternetAddress(email);
			emailAddress.validate();
			
		} catch (AddressException e) {
			e.printStackTrace();
			isValid = false;
		}
		return isValid;
	}
}
