package projekt.projekt.models;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Data
@Component
@SessionScope
public class AddOrder {
    private String deliveryID;
    private String cost;

    private String statusID;
}
