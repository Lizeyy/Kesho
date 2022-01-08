package projekt.projekt.models;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import projekt.projekt.entities.Producer;
import projekt.projekt.entities.Product;

import javax.persistence.Lob;
import java.util.List;

@Data
@Component
@SessionScope
public class AddItems {
    private String categoryName;
    private String categorySub;

    private String producerName;

    private String productName;
    @Lob
    private String productDescription;
    private double productPrice;
    private int productQuantity;
    private String productCategory;
    private String productProducer;

    private String deliveryName;
    private double deliveryCost;

    private String discountCode, discountDateFrom, discountDateTo;
    private int discountRebate;
    private List<Producer> discountList;

    //private String saleDateFrom, saleDateTo;
    //private double salePrice;
    //private String saleProduct;


    public AddItems(){}
}
