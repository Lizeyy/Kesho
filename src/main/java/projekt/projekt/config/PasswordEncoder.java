package projekt.projekt.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String passwordAdmin = "client";
        System.out.println("Haslo oryginalne dla admin: |"+passwordAdmin+"|");
        String encodedAdmin = encoder.encode(passwordAdmin);
        System.out.println("haslo zakodowane dla admin: |"+encodedAdmin+"|");
    }
}
