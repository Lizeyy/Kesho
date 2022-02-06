package projekt.projekt.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Data
@NoArgsConstructor
@Component
@SessionScope
public class EditCustomer {
    private String firstName;
    private String lastName;
    private String phone;

    private String street;
    private String house_number;
    private String apartment_number;
    private String zip;
    private String city;
}
