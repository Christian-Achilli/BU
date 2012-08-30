package useCases;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

public class CriptaPassword {

	@Test
	public void criptaPassword() {
		System.out.println(BCrypt.hashpw("super123", BCrypt.gensalt()));
	}
}
