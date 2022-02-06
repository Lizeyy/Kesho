package projekt.projekt.config;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekt.projekt.entities.Customer;
import projekt.projekt.entities.Login;
import projekt.projekt.repositories.LoginRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class LoginServices {
    @Autowired private LoginRepository loginRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JavaMailSender mailSender;

    public void register(Login login, Customer customer, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(login.getPassword());
        login.setPassword(encodedPassword);

        String randomToken = RandomString.make(64);
        login.setToken(randomToken);

        loginRepository.save(login);
        sendVerificationEmail(login, customer, siteURL);
    }


    public void sendVerificationEmail(Login login, Customer customer, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String toMail = customer.getEmail();
        String fromMail = "drogeria.kesho@gmail.com";
        String senderName = "Drogeria Kesho";
        String subject = "Weryfikacja konta w drogerii Kesho";
        String verifyURL = siteURL + "/verify?code=" + login.getToken();
        String content = "<div style=\"text-align: center;\"><img src=\"http://localhost:8080/images/LogoBG.png\"/></div>"
                        + "<br><br><h2>Witaj " + customer.getFirstName() + " " + customer.getLastName() + "</h2><br>"
                        + "Radujemy się z powodu Twojego dołączenia! <br>"
                        + "Kliknij w link poniżej, aby zweryfikować i aktywować Twoje konto:" + "<br>"
                        + "<div style=\"text-align: center;\"><h3><a href=\"" + verifyURL + "\" target=\"_blank\">KLIK!</a></h3></div>";


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(fromMail, senderName);
        messageHelper.setTo(toMail);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
    }

    public boolean verify(String token){
        try{
            Login login = loginRepository.findByToken(token).get(0);
            if(login == null || login.isActive()) return false;
            else {
                login.setToken(null);
                login.setActive(true);
                loginRepository.save(login);
                return true;
            }
        } catch (Exception e){}
        return false;
    }
}
