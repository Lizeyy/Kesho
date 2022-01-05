package projekt.projekt.entities;

import javax.persistence.*;

@Entity
@Table(name="Category")
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private Category subcategory;

    protected Category(){}
    public Category(String name, Category subcategory) {
        this.name = name;
        this.subcategory = subcategory;
    }

    public Category(String name) {
        this.name = name;
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
    public Category getSubcategory() {return subcategory;}
    public void setSubcategory(Category category) {this.subcategory = subcategory;}
}
