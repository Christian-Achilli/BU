package database;

import org.mindrot.jbcrypt.BCrypt;

public class CreaPassword {

    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("super123", BCrypt.gensalt()));
    }

}
