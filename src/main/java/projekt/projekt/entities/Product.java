package projekt.projekt.entities;

import javax.persistence.*;

@Entity
@Table(name="Product")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private int quantity;


    private double price;
    @Lob
    private String description;

    @ManyToOne
    private Category category;
    @ManyToOne
    private Producer producer;

    @ManyToOne
    private Photo photo;

    protected Product(){}
    public Product(String name, int quantity, double price, String description, Category category, Producer producer) {

        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.category = category;
        this.producer = producer;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public String getParsedPrice(){
        return String.format("%.2f", price);
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public Producer getProducer() {
        return producer;
    }
    public void setProducer(Producer producer) {
        this.producer = producer;
    }
    public Photo getPhoto() {
        return photo;
    }
    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
