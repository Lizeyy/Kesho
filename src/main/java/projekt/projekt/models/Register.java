package projekt.projekt.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Data
@NoArgsConstructor
@Component
@SessionScope
public class Register {
    String nameLogin, pass;
    String firstName, lastName, email, phone;
    String street, houseNumber, apartmentNumber, zipCode, city;
}
