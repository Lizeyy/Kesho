package projekt.projekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekt.projekt.config.LoginServices;
import projekt.projekt.entities.*;
import projekt.projekt.models.*;
import projekt.projekt.repositories.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MyController {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProducerRepository producerRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private PhotoRepository photoRepository;
    @Autowired private SaleRepository saleRepository;
    @Autowired private LoginRepository loginRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private DeliveryRepository deliveryRepository;
    @Autowired private EditProductRepository editProductRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private StatusRepository statusRepository;

    @Autowired private LoginServices loginServices;

    private Session session = new Session();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime now = LocalDateTime.now();

    @RequestMapping(value = {"/index", "/"})
    public String index(Model model, Principal principal){
        if(principal != null) model.addAttribute("auth", true);
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());

        model.addAttribute("salesEnding1XL", saleRepository.findSaleEnding(dtf.format(now), PageRequest.of(0, 4)));
        model.addAttribute("salesEnding2XL", saleRepository.findSaleEnding(dtf.format(now), PageRequest.of(1, 4)));
        model.addAttribute("salesEnding1LG", saleRepository.findSaleEnding(dtf.format(now), PageRequest.of(0, 3)));
        model.addAttribute("salesEnding2LG", saleRepository.findSaleEnding(dtf.format(now), PageRequest.of(1, 3)));
        model.addAttribute("salesEnding1MD", saleRepository.findSaleEnding(dtf.format(now), PageRequest.of(0, 2)));
        model.addAttribute("salesEnding2MD", saleRepository.findSaleEnding(dtf.format(now), PageRequest.of(1, 2)));
        model.addAttribute("salesEnding1SM", saleRepository.findSaleEnding(dtf.format(now), PageRequest.of(0, 1)));
        model.addAttribute("salesEnding2SM", saleRepository.findSaleEnding(dtf.format(now), PageRequest.of(1, 1)));

        model.addAttribute("comp", new Comp(saleRepository, editProductRepository, productRepository));
        model.addAttribute("productLast1XL", productRepository.findLastProduct(PageRequest.of(0, 4)));
        model.addAttribute("productLast2XL", productRepository.findLastProduct(PageRequest.of(1, 4)));
        model.addAttribute("productLast1LG", productRepository.findLastProduct(PageRequest.of(0, 3)));
        model.addAttribute("productLast2LG", productRepository.findLastProduct(PageRequest.of(1, 3)));
        model.addAttribute("productLast1MD", productRepository.findLastProduct(PageRequest.of(0, 2)));
        model.addAttribute("productLast2MD", productRepository.findLastProduct(PageRequest.of(1, 2)));
        model.addAttribute("productLast1SM", productRepository.findLastProduct(PageRequest.of(0, 1)));
        model.addAttribute("productLast2SM", productRepository.findLastProduct(PageRequest.of(1, 1)));

        model.addAttribute("productLast1", productRepository.findLastProduct(PageRequest.of(0, 3)));
        model.addAttribute("productLast2", productRepository.findLastProduct(PageRequest.of(1, 3)));
        return "/index";
    }

    @GetMapping("/list")
    public String list(Model model, Principal principal, @RequestParam(value = "cat") long cat,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                       @RequestParam(value = "sort", required = false, defaultValue = "0") int sort){
        if(principal != null) model.addAttribute("auth", true);
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());

        if(categoryRepository.findAllById(Collections.singleton(cat)).size() == 0){
            model.addAttribute("error", "Nie znaleziono produktów");

            model.addAttribute("cat", cat);
            return "list";
        }

        Category category = categoryRepository.findAllById(Collections.singleton(cat)).get(0);
        Page<Product> products0 = productRepository.findByCategoryAndQuantityGreaterThan(category, PageRequest.of(0, 7), 0);
        if(products0.getTotalElements() > 0){
            session.setCount(products0.getTotalPages());
            if(page > 0 && page <= session.getCount()) session.setPage(page - 1);
            Page<Product> products;
            switch (sort){
                case 0:
                    products = productRepository.findByCategoryAndQuantityGreaterThanOrderByName(category, PageRequest.of(session.getPage(), 7), 0);
                    break;
                case 1:
                    products = productRepository.findByCategoryAndQuantityGreaterThanOrderByNameDesc(category, PageRequest.of(session.getPage(), 7), 0);
                    break;
                case 2:
                    products = productRepository.findByCategoryAndQuantityGreaterThanOrderByPrice(category, PageRequest.of(session.getPage(), 7), 0);
                    break;
                case 3:
                    products = productRepository.findByCategoryAndQuantityGreaterThanOrderByPriceDesc(category, PageRequest.of(session.getPage(), 7), 0);
                    break;
                default:
                    products = productRepository.findByCategoryAndQuantityGreaterThan(category, PageRequest.of(session.getPage(), 7), 0);
                    break;
            }

            model.addAttribute("products", products);

            if(products.getTotalPages() > 0){
                List<Integer> pageNumber = IntStream.rangeClosed(1, products.getTotalPages())
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumber", pageNumber);
            }
            model.addAttribute("end", session.getCount());
            model.addAttribute("page", session.getPage() + 1);
            model.addAttribute("cat", cat);
            model.addAttribute("ok", products.getTotalPages() > 1);
            model.addAttribute("sort", sort);
            model.addAttribute("comp", new Comp(saleRepository, editProductRepository, productRepository));
        } else model.addAttribute("error", "Nie znaleziono produktów");

        return "list";
    }


    @GetMapping("/product")
    public String product(Model model, Principal principal, @RequestParam(value = "id") long id){
        Product product = productRepository.findAllById(Collections.singleton(id)).get(0);
        if(principal != null){
            model.addAttribute("auth", true);
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Login> loginList = loginRepository.findByName(name);
            if(loginList.get(0).getRole().equals("ROLE_CLIENT")){
                Customer customer = customerRepository.findByLogin(loginList.get(0)).get(0);
                if(cartRepository.findCustomerCartProductActive(customer.getId(), product.getId()).size() > 0) model.addAttribute("auth1", true);
                else model.addAttribute("auth2", true);
                model.addAttribute("customer", customer.getId());
            }
        }
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());


        model.addAttribute("product", product);
        model.addAttribute("photos", photoRepository.findByProductIdOrderByMainDesc(id));
        List<Integer> count = IntStream.rangeClosed(1, photoRepository.findByProductIdOrderByMainDesc(id).size())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("countPhotos", count);
        model.addAttribute("comp", new Comp(saleRepository, editProductRepository, productRepository));

        return "product";
    }

    @GetMapping("/product/add")
    public String productAdd(Model model, Principal principal, RedirectAttributes attributes, @RequestParam(value = "id") long id, @RequestParam(value = "idC") long customerID){
        Customer customer = customerRepository.findAllById(Collections.singleton(customerID)).get(0);
        Product product = productRepository.findAllById(Collections.singleton(id)).get(0);

        if(cartRepository.findByCustomerAndBought(customer, false).size() > 0){
            Cart cart = cartRepository.findByCustomerAndBought(customer, false).get(0);
            cart.addProduct(product, LocalDate.now(), 1);
            cartRepository.save(cart);
        } else {
            Cart cart = new Cart(customer);
            cartRepository.save(cart);
            cart.addProduct(product, LocalDate.now(), 1);
            cartRepository.save(cart);
        }
        attributes.addAttribute("id", id);
        return "redirect:/product";
    }
    @GetMapping("/product/remove")
    public String productRemove(Model model, Principal principal, RedirectAttributes attributes, @RequestParam(value = "id") long id, @RequestParam(value = "idC") long customerID){
        Customer customer = customerRepository.findAllById(Collections.singleton(customerID)).get(0);
        Product product = productRepository.findAllById(Collections.singleton(id)).get(0);
        Cart cart = cartRepository.findByCustomerAndBought(customer, false).get(0);
        cart.removeProduct(product);
        cartRepository.save(cart);

        attributes.addAttribute("id", id);
        return "redirect:/product";
    }


    @GetMapping("/staff")
    public String staff(Model model){
        model.addAttribute("addItems", new AddItems());
        model.addAttribute("editItems", new EditItems());
        model.addAttribute("listCat", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("listSub", categoryRepository.findBySubcategoryNotNull());
        model.addAttribute("listProducer", producerRepository.findAll());
        model.addAttribute("listProducts", productRepository.findAll());
        model.addAttribute("listPhoto", photoRepository.findByMain(true));
        model.addAttribute("listDelivery", deliveryRepository.findAll());

        model.addAttribute("order1", orderRepository.findStatus(statusRepository.findByName("Złożenie zamówienia").get(0).getId()));
        model.addAttribute("order2", orderRepository.findStatus(statusRepository.findByName("W trakcie pakowania").get(0).getId()));
        model.addAttribute("order3", orderRepository.findStatus(statusRepository.findByName("Czeka na wysyłkę").get(0).getId()));
        model.addAttribute("order4", orderRepository.findStatus(statusRepository.findByName("Wysłane").get(0).getId()));
        model.addAttribute("order5", orderRepository.findStatus(statusRepository.findByName("Odebrane").get(0).getId()));
        model.addAttribute("orders", orderRepository.findAllOrder());

   //     model.addAttribute("LastOrders", orderRepository.findDate(LocalDate.now().minusDays(30).format(dtf)));
        return "staff";
    }
    public String admin(Model model){
        return "admin";
    }




    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, Principal principal){
        if(request.isUserInRole("ROLE_ADMIN")) return admin(model);
        if(request.isUserInRole("ROLE_STAFF")) return staff(model);
        if(request.isUserInRole("ROLE_CLIENT")) return "redirect:/client";
        else return "login";
    }
    @GetMapping("/register/login")
    public String register(Model model){
        model.addAttribute("register", new Register());
        return "registerLogin";
    }
    @PostMapping("/register/login")
    public String registerLogin(Model model, Register register){
        if(loginRepository.findByName(register.getNameLogin()).size() == 0){
            return registerNext(model, register);
        } else {
            model.addAttribute("busy", "Podany login już istnieje");
            return register(model);
        }
    }
    @GetMapping("/register/next")
    public String registerNext(Model model, Register register) {
        model.addAttribute("register", register);
        return "registerNext";
    }
    @PostMapping("/register/next")
    public String registerNextPost(Model model, Register register, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        if(customerRepository.findByEmail(register.getEmail()).size() == 0){
            Address address = new Address();
            address.setStreet(register.getStreet().substring(0,1).toUpperCase() + register.getStreet().substring(1));
            address.setHouseNumber(register.getHouseNumber());
            address.setApartmentNumber(register.getApartmentNumber());
            address.setZipCode(register.getZipCode());
            address.setCity(register.getCity().substring(0,1).toUpperCase() + register.getCity().substring(1));
            addressRepository.save(address);

            Login login = new Login(register.getNameLogin(), register.getPass());
            loginRepository.save(login);

            Customer customer = new Customer();
            customer.setFirstName(register.getFirstName().substring(0,1).toUpperCase() + register.getFirstName().substring(1));
            customer.setLastName(register.getLastName().substring(0,1).toUpperCase() + register.getLastName().substring(1));
            customer.setEmail(register.getEmail());
            customer.setPhone(register.getPhone());
            customer.setAddress(address);
            customer.setLogin(login);
            customerRepository.save(customer);

            loginServices.register(login, customer, request.getRequestURL().toString().replace(request.getServletPath(), ""));
            return "registerSuccess";
        } else {
            model.addAttribute("error", "Istnieje już konto powiązane z tym adresem e-mail");
            model.addAttribute("register", register);
            return "registerNext";
        }
    }
    @GetMapping("/verify")
    public String verify(@RequestParam("code") String token){
        if(loginServices.verify(token)) return "verifySuccess";
        else return "verifyFail";
    }

    @ExceptionHandler
    public String handlerException(Model model, Exception exception) {
        String message = exception.getMessage();
        model.addAttribute("error", message);
        return "error";
    }
}
