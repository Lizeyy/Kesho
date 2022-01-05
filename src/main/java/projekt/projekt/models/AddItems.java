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

    private String saleDateFrom, saleDateTo;
    private double salePrice;
    private Product saleProduct;


    public AddItems(){}

   // public String getCategoryName() {
   //     return categoryName;
   // }
   // public void setCategoryName(String categoryName) {
   //     this.categoryName = categoryName;
   // }
   // public Category getCategorySub() {
   //     return categorySub;
   // }
   // public void setCategorySub(Category categorySub) {
   //     this.categorySub = categorySub;
   // }
   // public String getProducerName() {
   //     return producerName;
   // }
   // public void setProducerName(String producerName) {
   //     this.producerName = producerName;
   // }
   // public String getProductName() {
   //     return productName;
   // }
   // public void setProductName(String productName) {
   //     this.productName = productName;
   // }
   // public String getProductDescription() {
   //     return productDescription;
   // }
   // public void setProductDescription(String productDescription) {
   //     this.productDescription = productDescription;
   // }
   // public double getProductPrice() {
   //     return productPrice;
   // }
   // public void setProductPrice(double productPrice) {
   //     this.productPrice = productPrice;
   // }
   // public int getProductQuantity() {
   //     return productQuantity;
   // }
   // public void setProductQuantity(int productQuantity) {
   //     this.productQuantity = productQuantity;
   // }
   // public Category getProductCategory() {
   //     return productCategory;
   // }
   // public void setProductCategory(Category productCategory) {
   //     this.productCategory = productCategory;
   // }
   // public Producer getProductProducer() {
   //     return productProducer;
   // }
   // public void setProductProducer(Producer productProducer) {
   //     this.productProducer = productProducer;
   // }
   // public String getDeliveryName() {
   //     return deliveryName;
   // }
   // public void setDeliveryName(String deliveryName) {
   //     this.deliveryName = deliveryName;
   // }
   // public double getDeliveryCost() {
   //     return deliveryCost;
   // }
   // public void setDeliveryCost(double deliveryCost) {
   //     this.deliveryCost = deliveryCost;
   // }
   // public String getDiscountCode() {
   //     return discountCode;
   // }
   // public void setDiscountCode(String discountCode) {
   //     this.discountCode = discountCode;
   // }
   // public String getDiscountDateFrom() {
   //     return discountDateFrom;
   // }
   // public void setDiscountDateFrom(String discountDateFrom) {
   //     this.discountDateFrom = discountDateFrom;
   // }
   // public String getDiscountDateTo() {
   //     return discountDateTo;
   // }
   // public void setDiscountDateTo(String discountDateTo) {
   //     this.discountDateTo = discountDateTo;
   // }
   // public int getDiscountRebate() {
   //     return discountRebate;
   // }
   // public void setDiscountRebate(int discountRebate) {
   //     this.discountRebate = discountRebate;
   // }
   // public List<Producer> getDiscountList() {
   //     return discountList;
   // }
   // public void setDiscountList(List<Producer> discountList) {
   //     this.discountList = discountList;
   // }
   // public String getSaleDateFrom() {
   //     return saleDateFrom;
   // }
   // public void setSaleDateFrom(String saleDateFrom) {
   //     this.saleDateFrom = saleDateFrom;
   // }
   // public String getSaleDateTo() {
   //     return saleDateTo;
   // }
   // public void setSaleDateTo(String saleDateTo) {
   //     this.saleDateTo = saleDateTo;
   // }
   // public double getSalePrice() {
   //     return salePrice;
   // }
   // public void setSalePrice(double salePrice) {
   //     this.salePrice = salePrice;
   // }
   // public Product getSaleProduct() {
   //     return saleProduct;
   // }
   // public void setSaleProduct(Product saleProduct) {
   //     this.saleProduct = saleProduct;
   // }
}
