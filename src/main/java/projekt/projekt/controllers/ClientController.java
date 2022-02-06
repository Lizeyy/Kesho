package projekt.projekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekt.projekt.entities.*;
import projekt.projekt.models.AddOrder;
import projekt.projekt.models.Comp;
import projekt.projekt.models.EditCart;
import projekt.projekt.models.EditCustomer;
import projekt.projekt.repositories.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
public class ClientController {
    @Autowired CategoryRepository categoryRepository;
    @Autowired CartRepository cartRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired LoginRepository loginRepository;
    @Autowired CartProductRepository cartProductRepository;
    @Autowired SaleRepository saleRepository;
    @Autowired ProductRepository productRepository;
    @Autowired DeliveryRepository deliveryRepository;
    @Autowired StatusRepository statusRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired EditProductRepository editProductRepository;

    @GetMapping("/client")
    public String client(Model model, Principal principal){
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());

        Customer customer = customerRepository.findByLogin(loginRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get(0)).get(0);
        model.addAttribute("customer", customer);
        model.addAttribute("edit", new EditCustomer());

        model.addAttribute("ordersNow", orderRepository.findActive(customer.getId()));
        model.addAttribute("orders", orderRepository.findAllCustomer(customer.getId()));


        return "client";
    }

    @PostMapping("/client/change/data")
    public String changeData(EditCustomer editCustomer, Principal principal){
        Customer customer = customerRepository.findByLogin(loginRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get(0)).get(0);
        if(!editCustomer.getFirstName().isEmpty()) customer.setFirstName(editCustomer.getFirstName());
        if(!editCustomer.getLastName().isEmpty()) customer.setLastName(editCustomer.getLastName());
        if(!editCustomer.getPhone().isEmpty()) customer.setPhone(editCustomer.getPhone());
        customerRepository.save(customer);

        return "redirect:/client";
    }

    @PostMapping("/client/change/address")
    public String changeAddress(EditCustomer editCustomer, Principal principal){
        Customer customer = customerRepository.findByLogin(loginRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get(0)).get(0);
        if(!editCustomer.getStreet().isEmpty()) customer.getAddress().setStreet(editCustomer.getStreet());
        if(!editCustomer.getHouse_number().isEmpty()) customer.getAddress().setHouseNumber(editCustomer.getHouse_number());
        if(!editCustomer.getApartment_number().isEmpty()) customer.getAddress().setApartmentNumber(editCustomer.getApartment_number());
        if(!editCustomer.getZip().isEmpty()) customer.getAddress().setZipCode(editCustomer.getZip());
        if(!editCustomer.getCity().isEmpty()) customer.getAddress().setCity(editCustomer.getCity());
        customerRepository.save(customer);
        return "redirect:/client";
    }


    @GetMapping("/client/cart")
    public String cart(Model model){
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());
        Comp comp = new Comp(saleRepository, editProductRepository, productRepository);

        Customer customer = customerRepository.findByLogin(loginRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get(0)).get(0);
        List<CartProduct> products = cartProductRepository.findActive(customer.getId());
        model.addAttribute("products", products);
        if(products.size() > 0) model.addAttribute("cart", products.get(0).getCart());

        float coast = 0;
        float difference = 0;
        for(int i = 0; i < products.size(); i++){
            coast += products.get(i).getQuantity() * products.get(i).getProduct().getPrice();
            if(comp.getSaleProduct(products.get(i).getProduct().getId()) != null){
                difference += (products.get(i).getProduct().getPrice() - comp.getSaleProduct(products.get(i).getProduct().getId()).getPrice()) * products.get(i).getQuantity();
            }
        }
        float total = coast - difference;
        model.addAttribute("coast", String.format("%.2f", coast));
        model.addAttribute("total", String.format("%.2f", total));
        model.addAttribute("difference", String.format("%.2f", difference));

        model.addAttribute("comp", comp);
        model.addAttribute("edit", new EditCart());
        return "cart";
    }
    @PostMapping("/client/cart/change")
    public String cart(Model model, EditCart editCart){
        Product product = productRepository.findAllById(Collections.singleton(editCart.getIdProduct())).get(0);
        if(product.getQuantity() >= editCart.getQuantity()){
            CartProduct cartProduct = cartProductRepository.findCartProduct(editCart.getIdCart(), editCart.getIdProduct());
            cartProduct.setQuantity(editCart.getQuantity());
            cartProductRepository.save(cartProduct);
            return "redirect:/client/cart";
        } else {
            model.addAttribute("missing", true);
            return cart(model);
        }
    }
    @GetMapping("/client/cart/remove")
    public String cartRemove(@RequestParam(value = "idCart") long idCart, @RequestParam(value = "idProduct") long idProduct){
        Cart cart = cartRepository.findAllById(Collections.singleton(idCart)).get(0);
        Product product = productRepository.findAllById(Collections.singleton(idProduct)).get(0);
        cart.removeProduct(product);
        cartRepository.save(cart);
        return "redirect:/client/cart";
    }


    @GetMapping("/client/cart/order")
    public String order(Model model, @RequestParam(value = "cart") long idCart){
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());

        Cart cart = cartRepository.findAllById(Collections.singleton(idCart)).get(0);
        Customer customer = customerRepository.findAllById(Collections.singleton(cart.getCustomer().getId())).get(0);
        model.addAttribute("cart", cart);
        model.addAttribute("customer", customer);
        model.addAttribute("listDelivery", deliveryRepository.findByActive(true));
        model.addAttribute("products", cartProductRepository.findActive(customer.getId()));
        model.addAttribute("comp", new Comp(saleRepository, editProductRepository, productRepository));
        model.addAttribute("addOrder", new AddOrder());

        return "order";
    }
    @PostMapping("/client/cart/success")
    public String order(Model model, AddOrder addOrder, @RequestParam(value = "idCart") long idCart){
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        Cart cart = cartRepository.findAllById(Collections.singleton(idCart)).get(0);
        List<CartProduct> list = cartProductRepository.findActive(cart.getCustomer().getId());

        Orders order = new Orders(Double.parseDouble(addOrder.getCost()),
                now.format(dtf),
                statusRepository.findByName("Złożenie zamówienia").get(0),
                deliveryRepository.findAllById(Collections.singleton(Long.parseLong(addOrder.getDeliveryID()))).get(0),
                cart);
        orderRepository.save(order);
        cart.setBought(true);
        cartRepository.save(cart);
        for(int i = 0; i < list.size(); i++){
            Product product = list.get(i).getProduct();
            product.setQuantity(product.getQuantity() - list.get(i).getQuantity());
            productRepository.save(product);
        }

        return "orderSuccess";
    }


    @GetMapping("/client/order/check")
    public String check(Model model, @RequestParam(value = "id") long id){
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());
        Comp comp = new Comp(saleRepository, editProductRepository, productRepository, cartProductRepository);

        Orders order = orderRepository.findAllById(Collections.singleton(id)).get(0);
        Customer customer = order.getCart().getCustomer();
        List<Product> products = productRepository.findCart(order.getCart().getId());
        for(int i = 0; i < products.size(); i++){
            if(comp.getSaleProductThen(products.get(i).getId(), LocalDate.parse(order.getDate())) != null){
                products.get(i).setPrice(comp.getSaleProductThen(products.get(i).getId(), LocalDate.parse(order.getDate())).getPrice());
            } else {
                products.get(i).setPrice(comp.getPriceThen(products.get(i).getId(), LocalDate.parse(order.getDate())));
            }
        }

        model.addAttribute("order", order);
        model.addAttribute("cart", order.getCart());
        model.addAttribute("customer", customer);
        model.addAttribute("products", products);
        model.addAttribute("comp", comp);
        return "orderDetails";
    }
}
