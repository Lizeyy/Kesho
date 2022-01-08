package projekt.projekt.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import projekt.projekt.entities.Category;
import projekt.projekt.entities.Product;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@Component
@SessionScope
public class EditItems {
    private Category editCategory;
    private String newCategory;

    private String editProducer, newProducer;

    private double newPrice, newSalePrice, newNextSalePrice, newUpNextSalePrice;
    private String newDate, newNextDateFrom, newNextDateTo, newUpNextDateFrom, newUpNextDateTo;
    @Lob private String newDescription;
    private int newQuantity;
    private Product product;
}
