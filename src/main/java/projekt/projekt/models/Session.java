package projekt.projekt.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.annotation.SessionScope;

@NoArgsConstructor
@SessionScope
@Data
public class Session {
    int count, page;
}
