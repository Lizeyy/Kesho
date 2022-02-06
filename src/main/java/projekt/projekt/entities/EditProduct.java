package projekt.projekt.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@Entity
@Table(name="EditProduct")
public class EditProduct {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Product product;
    private String date;
    private double newPrice;
    private double oldPrice;

    public EditProduct(Product product, double oldPrice, double newPrice){
        this.product = product;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        this.date = dtf.format(now);
    }
}
