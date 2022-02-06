package projekt.projekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projekt.projekt.entities.*;
import projekt.projekt.models.AddItems;
import projekt.projekt.models.AddOrder;
import projekt.projekt.models.Comp;
import projekt.projekt.models.EditItems;
import projekt.projekt.repositories.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class StaffController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProducerRepository producerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private EditProductRepository editProductRepository;
    @Autowired
    private MyController myController;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/staff/addStatus")
    public String addStatus(Model model, AddItems addItems){
        if(statusRepository.findByName(addItems.getStatusName()).size() > 0) model.addAttribute("notification", "Istnieje już status o takiej nazwie");
        else {
            Status status = new Status(addItems.getStatusName(), addItems.getStatusDescription());
            statusRepository.save(status);
            model.addAttribute("notification", "Pomyślnie dodano nowy status zamówienia");
        }
        return myController.staff(model);
    }

    @PostMapping("/staff/addDelivery")
    public String addDelivery(Model model, AddItems addItems){
        if(deliveryRepository.findByName(addItems.getDeliveryName()).size() > 0) model.addAttribute("notification", "Istnieje już dostawca o takiej nazwie");
        else {
            Delivery delivery = new Delivery(addItems.getDeliveryName(), addItems.getDeliveryCost(), true);
            deliveryRepository.save(delivery);
            model.addAttribute("notification", "Pomyślnie dodano nowego dostawcę");
        }
        return myController.staff(model);
    }
    @PostMapping("/staff/editDelivery")
    public String editDelivery(Model model, EditItems editItems){
        Delivery delivery = deliveryRepository.findByName(editItems.getDeliveryName()).get(0);
        if(!editItems.getNewDeliveryName().isEmpty()){
            if(deliveryRepository.findByName(editItems.getNewDeliveryName()).size() > 0) model.addAttribute("notification", "Istnieje już dostawca o takiej nazwie");
            else delivery.setName(editItems.getNewDeliveryName());
        }

        if (!editItems.getDeliveryCost().isEmpty()) delivery.setCost(Double.parseDouble(editItems.getDeliveryCost()));

        delivery.setActive(editItems.isDeliveryActive());
        deliveryRepository.save(delivery);
        model.addAttribute("notification", "Pomyślnie edytowano dostawcę ");
        return myController.staff(model);
    }

    @PostMapping("/staff/addCategory")
    public String addCategory(Model model, AddItems addItems){
        if(categoryRepository.findByName(addItems.getCategoryName()).size() > 0){
            model.addAttribute("notification", "Istnieje już kategoria o takiej nazwie");
        } else {
            Category category;
            if(addItems.getCategorySub().isEmpty()){
                category = new Category(addItems.getCategoryName());
            } else {
                Category sub = categoryRepository.findByName(addItems.getCategorySub()).get(0);
                category = new Category(addItems.getCategoryName(), sub);
            }
            categoryRepository.save(category);
            model.addAttribute("notification", "Pomyślnie dodano nowego dostawcę");
        }
        return myController.staff(model);
    }

    @PostMapping("/staff/addProducer")
    public String addProducer(Model model, AddItems addItems){
        if(producerRepository.findByName(addItems.getProducerName()).size() > 0){
            model.addAttribute("notification", "Istnieje już producent o takiej nazwie");
        } else {
            Producer producer = new Producer(addItems.getProducerName());
            producerRepository.save(producer);
            model.addAttribute("notification", "Pomyślnie dodano nowego producenta");
        }
        return myController.staff(model);
    }
    @PostMapping("/staff/editProducer")
    public String editProducer(Model model, EditItems editItems){
        if(productRepository.findByName(editItems.getNewProducer()).size() > 0){
            model.addAttribute("notification", "Istnieje już producent o takiej nazwie");
        } else {
            Producer producer = producerRepository.findByName(editItems.getEditProducer()).get(0);
            producer.setName(editItems.getNewProducer());
            producerRepository.save(producer);
            model.addAttribute("notification", "Pomyślnie zmieniono nazwę producenta");
        }
        return myController.staff(model);
    }

    @GetMapping("/staff/editProduct")
    public String editProduct1(Model model, @RequestParam("id") long id){
        Product product = productRepository.findAllById(Collections.singleton(id)).get(0);
        model.addAttribute("product", product);
        model.addAttribute("photos", photoRepository.findByProductIdOrderByMainDesc(id));
        List<Integer> count = IntStream.rangeClosed(1, photoRepository.findByProductIdOrderByMainDesc(id).size())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("countPhotos", count);
        model.addAttribute("editItems", new EditItems());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        if(saleRepository.findActiveSale(product.getId(), dtf.format(now)).size() == 0) model.addAttribute("activeSale", false);
        else model.addAttribute("activeSale", saleRepository.findActiveSale(product.getId(), dtf.format(now)).get(0));

        if(saleRepository.findNextSale(product.getId(), dtf.format(now)).size() == 0) model.addAttribute("nextSale", false);
        else model.addAttribute("nextSale", saleRepository.findNextSale(product.getId(), dtf.format(now)).get(0));

        if(saleRepository.findPrevSale(product.getId(), dtf.format(now)).size() == 0) model.addAttribute("prevSale", false);
        else model.addAttribute("prevSale", saleRepository.findPrevSale(product.getId(), dtf.format(now)).get(0));

        model.addAttribute("comp", new Comp(saleRepository, editProductRepository, productRepository, orderRepository, cartProductRepository));
        return "staff-editProduct";
    }
    @PostMapping("/staff/editProduct")
    public String addSale(Model model, EditItems editItems, @RequestParam("id") long id) throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        Product product = productRepository.findAllById(Collections.singleton(id)).get(0);
        Sale activeSale = null;
        Sale nextSale = null;
        try{
            activeSale = saleRepository.findActiveSale(product.getId(), dtf.format(now)).get(0);
            nextSale = saleRepository.findNextSale(product.getId(), dtf.format(now)).get(0);
        } catch (Exception e) {}

        boolean ok = true;

        if(editItems.getNewPrice() > 0){
            if(activeSale != null) {
                if(activeSale.getPrice() < editItems.getNewPrice()) changePrice(product, editItems);
                else {
                    ok = false;
                    model.addAttribute("error0", "Istnieje promocja z wyższą ceną. Najpierw zmień cenę promocyjną");
                }
            }
            else if(nextSale != null){
                if(nextSale.getPrice() < editItems.getNewPrice()) changePrice(product, editItems);
                else {
                    ok = false;
                    model.addAttribute("error0", "Istnieje promocja z wyższą ceną. Najpierw zmień cenę promocyjną");
                }
            }
            else changePrice(product, editItems);
        }
        if(editItems.getNewQuantity() > 0){
            product.setQuantity(editItems.getNewQuantity());
            productRepository.save(product);
        }

        try{
            if (!editItems.getNextDateFrom().isEmpty() || !editItems.getNextDateTo().isEmpty() || editItems.getNextSalePrice() > 0) {
                if(editItems.getNextDateFrom().isEmpty() || editItems.getNextDateTo().isEmpty() || editItems.getNextSalePrice() == 0) {
                    model.addAttribute("error1", "Uzupełnij wszystkie pola");
                    ok = false;
                }
                if (saleRepository.findActiveSale(product.getId(), editItems.getNextDateFrom()).size() == 0) {
                    Date start = new SimpleDateFormat("yyyy-MM-dd").parse(editItems.getNextDateFrom());
                    Date end = new SimpleDateFormat("yyyy-MM-dd").parse(editItems.getNextDateTo());
                    if (start.compareTo(end) <= 0) {
                        if (editItems.getNextSalePrice() < product.getPrice() && editItems.getNextSalePrice() > 0) {
                            saleRepository.save(new Sale(editItems.getNextDateFrom(), editItems.getNextDateTo(), editItems.getNextSalePrice(), product));
                        } else {
                            model.addAttribute("error2", "Cena promocji jest wyższa od regularnej ceny lub nie została podana");
                            ok = false;
                        }
                    } else {
                        model.addAttribute("error3", "Wybrane daty są niepoprawne");
                        ok = false;
                    }
                } else {
                    model.addAttribute("error4", "Wybrana data koliduje z inną promocją");
                    ok = false;
                }
            }
        } catch (NullPointerException e){}


        try{
            if (!editItems.getUpDateFrom().isEmpty() || !editItems.getUpDateTo().isEmpty() || editItems.getUpSalePrice() > 0) {
                if(editItems.getUpSalePrice() > 0){
                    if(editItems.getUpSalePrice() < product.getPrice()){
                        nextSale.setPrice(editItems.getUpSalePrice());
                        saleRepository.save(nextSale);
                    } else {
                        model.addAttribute("error2", "Cena promocji jest wyższa od regularnej ceny");
                        ok = false;
                    }
                }
                if(!editItems.getUpDateFrom().isEmpty()){
                    if (saleRepository.findActiveSaleWithout(product.getId(), editItems.getUpDateFrom(), nextSale.getId()).size() == 0) {
                        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(editItems.getUpDateFrom());
                        if(editItems.getUpDateTo().isEmpty()){
                            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(nextSale.getDateTo());
                            if (start.compareTo(end) <= 0) {
                                nextSale.setDateFrom(editItems.getUpDateFrom());
                                saleRepository.save(nextSale);
                            } else {
                                model.addAttribute("error3", "Wybrane daty są niepoprawne");
                                ok = false;
                            }
                        } else {
                            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(editItems.getUpDateTo());
                            if (start.compareTo(end) <= 0) {
                                nextSale.setDateFrom(editItems.getUpDateFrom());
                                nextSale.setDateTo(editItems.getUpDateTo());
                                saleRepository.save(nextSale);
                            } else {
                                model.addAttribute("error3", "Wybrane daty są niepoprawne");
                                ok = false;
                            }
                        }
                    } else {
                        model.addAttribute("error4", "Wybrana data koliduje z inną promocją");
                        ok = false;
                    }
                } else if(!editItems.getUpDateTo().isEmpty()){
                    Date start = new SimpleDateFormat("yyyy-MM-dd").parse(nextSale.getDateFrom());
                    Date end = new SimpleDateFormat("yyyy-MM-dd").parse(editItems.getUpDateTo());
                    if (start.compareTo(end) <= 0) {
                        nextSale.setDateFrom(editItems.getUpDateFrom());
                        nextSale.setDateTo(editItems.getUpDateTo());
                        saleRepository.save(nextSale);
                    } else {
                        model.addAttribute("error3", "Wybrane daty są niepoprawne");
                        ok = false;
                    }
                }
            }
        } catch (NullPointerException e){}

        product.setDescription(editItems.getNewDescription());
        productRepository.save(product);
        if(ok) model.addAttribute("notification", "Produkt został zapisany");
        return editProduct1(model, product.getId());
    }
    private void changePrice(Product product, EditItems editItems){
        EditProduct editProduct = new EditProduct(product, product.getPrice(), editItems.getNewPrice());
        product.setPrice(editItems.getNewPrice());
        editProductRepository.save(editProduct);
        productRepository.save(product);
    }

    @GetMapping("/staff/order/check")
    public String check(@RequestParam("id") long id, Model model){
        Orders order = orderRepository.findAllById(Collections.singleton(id)).get(0);
        Comp comp = new Comp(saleRepository, editProductRepository, productRepository, orderRepository, cartProductRepository);
        Customer customer = order.getCart().getCustomer();

        List<Product> products = productRepository.findCart(order.getCart().getId());
        for(int i = 0; i < products.size(); i++){
            if(comp.getSaleProductThen(products.get(i).getId(), LocalDate.parse(order.getDate())) != null){
                products.get(i).setPrice(comp.getSaleProductThen(products.get(i).getId(), LocalDate.parse(order.getDate())).getPrice());
            } else {
                products.get(i).setPrice(comp.getPriceThen(products.get(i).getId(), LocalDate.parse(order.getDate())));
            }
        }

        model.addAttribute("listStatus", statusRepository.findAll());
        model.addAttribute("addOrder", new AddOrder());
        model.addAttribute("order", order);
        model.addAttribute("cart", order.getCart());
        model.addAttribute("customer", customer);
        model.addAttribute("products", products);
        model.addAttribute("comp", comp);

        return "orderCheck";
    }

    @PostMapping("/staff/order/check")
    public String check(AddOrder addOrder, @RequestParam("id") long id, Model model){
        Orders orders = orderRepository.findAllById(Collections.singleton(id)).get(0);
        orders.setStatus(statusRepository.findAllById(Collections.singleton(Long.parseLong(addOrder.getStatusID()))).get(0));
        orderRepository.save(orders);
        return check(id, model);
    }


    @PostMapping("/staff/addProduct")
    public String addProduct(Model model, AddItems addItems, @RequestParam("main") MultipartFile main, @RequestParam("files") MultipartFile[] files){
        if(addItems.getProductName().isEmpty() || addItems.getProductCategory().isEmpty() || addItems.getProductProducer().isEmpty() || addItems.getProductPrice() <= 0
        || main.isEmpty()){
            model.addAttribute("notification", "Wystąpił błąd przy dodawaniu produktu");
        } else {
            if(productRepository.findByName(addItems.getProductName()).size() == 0){
                Producer producer = producerRepository.findByName(addItems.getProductProducer()).get(0);
                Category category = categoryRepository.findByName(addItems.getProductCategory()).get(0);
                String pathDir = "src/main/resources/static/products/" + producer.getName() + "/";

                Product product = new Product(addItems.getProductName(), addItems.getProductQuantity(), addItems.getProductPrice(), addItems.getProductDescription(), category, producer);
                productRepository.save(product);

                product = productRepository.findByName(addItems.getProductName()).get(0);
                try{
                    Files.createDirectories(Paths.get(pathDir));
                    Path mainPath = Paths.get(pathDir + addItems.getProductName() + "-main" + ".jpeg");
                    String path2 = "products/" + producer.getName() + "/" + addItems.getProductName() + "-main" + ".jpeg";
                    Files.write(mainPath, main.getBytes());
                    Photo photo = new Photo(path2, true, product);
                    photoRepository.save(photo);
                    product.setPhoto(photo);
                    productRepository.save(product);

                    if(files.length > 1){
                        int i = 1;
                        for(MultipartFile file : files){
                            Path path = Paths.get(pathDir + addItems.getProductName() + "-" + i + ".jpeg");
                            try{
                                Files.write(path, file.getBytes());
                                String path3 = "products/" + producer.getName() + "/" + addItems.getProductName() + '-' + i + ".jpeg";
                                Photo photo1 = new Photo(path3, false, product);
                                photoRepository.save(photo1);
                            } catch (IOException e){e.printStackTrace();}
                            i++;
                        }
                    }

                    model.addAttribute("notification", "Pomyślnie dodano nowy produkt");
                } catch (IOException e){
                    e.printStackTrace();
                    productRepository.delete(product);
                    model.addAttribute("notification", "Wystąpił błąd przy dodawaniu produktu");
                }


            } else { model.addAttribute("notification", "Wystąpił błąd przy dodawaniu produktu"); }

        }
        return myController.staff(model);
    }
}
