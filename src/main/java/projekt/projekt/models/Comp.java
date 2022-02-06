package projekt.projekt.models;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import projekt.projekt.entities.CartProduct;
import projekt.projekt.entities.EditProduct;
import projekt.projekt.entities.Orders;
import projekt.projekt.entities.Sale;
import projekt.projekt.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Component
public class Comp {
    private SaleRepository saleRepository;
    private EditProductRepository editProductRepository;
    private ProductRepository productRepository;
    private CartProductRepository cartProductRepository;
    private OrderRepository orderRepository;
    private CartRepository  cartRepository;

    public Comp(SaleRepository saleRepository, EditProductRepository editProductRepository, ProductRepository productRepository){
        this.saleRepository = saleRepository;
        this.editProductRepository = editProductRepository;
        this.productRepository = productRepository;
    }
    public Comp(SaleRepository saleRepository, EditProductRepository editProductRepository, ProductRepository productRepository, CartProductRepository cartProductRepository){
        this.saleRepository = saleRepository;
        this.editProductRepository = editProductRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
    }

    public Comp(SaleRepository saleRepository, EditProductRepository editProductRepository, ProductRepository productRepository, OrderRepository orderRepository, CartProductRepository cartProductRepository){
        this.orderRepository = orderRepository;
        this.saleRepository = saleRepository;
        this.editProductRepository = editProductRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
    }


    //Aktywna promocja
    public Sale getSaleProduct(long id){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        try{
            List<Sale> sales = saleRepository.findActiveSale(id, dtf.format(now));
            return sales.get(0);
        } catch (Exception e) {}
        return null;
    }

    //Promocja w czasie zakupu
    public Sale getSaleProductThen(long id, LocalDate date){
        try{
            List<Sale> sales = saleRepository.findActiveSale(id, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return sales.get(0);
        } catch (Exception e) {}
        return null;
    }

    //Cena produkty w czasie zakupu
    public double getPriceThen(long id, LocalDate date){
        try{
            List<EditProduct> editProducts = editProductRepository.findDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), id);
            return editProducts.get(0).getOldPrice();
        } catch (Exception e) {}
        return productRepository.findAllById(Collections.singleton(id)).get(0).getPrice();
    }

    public String getParsedSale(int quantity, long idP){
        return String.format("%.2f", (getSaleProduct(idP).getPrice() * quantity)) + " zł";
    }
    public String getParsedSaleThen(int quantity, long idP, LocalDate date){
        return String.format("%.2f", (getSaleProductThen(idP, date).getPrice() * quantity)) + " zł";
    }
    public double getNotParsedSale(int quantity, long idP){
        return getSaleProduct(idP).getPrice() * quantity;
    }
    public String getParsed(int quantity, double price){
        return String.format("%.2f", (price * quantity)) + " zł";
    }

  //  public String getParsedThen(int quantity, long id, LocalDate date){
  //      return String.format("%.2f", (Double.parseDouble(getPriceThen(id, date)) * quantity)) + " zł";
  //  }
    public String sum(List<CartProduct> list){
        double sum = 0;
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.size());
            if(getSaleProduct(list.get(i).getProduct().getId()) != null) sum += list.get(i).getQuantity() * getSaleProduct(list.get(i).getProduct().getId()).getPrice();
            else sum += list.get(i).getQuantity() * list.get(i).getProduct().getPrice();
        }
        return String.format("%.2f", sum) + " zł";
    }

    public int getQuantity1(long idP, long idC){
        return cartProductRepository.findCartProduct(idC, idP).getQuantity();
    }


    public int getQuaOr(long idP, String from, String to){
        List<Orders> orders = orderRepository.findProductSale(idP, from, to);
        int quantity = 0;
        for(int i = 0; i < orders.size(); i++){
            quantity += cartProductRepository.findCartProduct(orders.get(i).getCart().getId(), idP).getQuantity();
        }
        return quantity;
    }
}
