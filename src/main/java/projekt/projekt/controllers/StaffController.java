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
import projekt.projekt.models.EditItems;
import projekt.projekt.repositories.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private MyController myController;



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
            model.addAttribute("notification", "Pomyślnie dodano nową kategorię");
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
    public String addSale(Model model, @RequestParam("id") long id){
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

        return "staff-editProduct";
    }
    @PostMapping("/staff/editProduct")
    public String addSale(Model model, EditItems editItems, @RequestParam("id") long id) throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        Product product = productRepository.findAllById(Collections.singleton(id)).get(0);
        if(editItems.getNewPrice() > 0){
            product.setPrice(editItems.getNewPrice());
            productRepository.save(product);
            model.addAttribute("notification", "Pomyślnie edytowano produkt");
        }
        if(editItems.getNewQuantity() > 0){
            product.setQuantity(editItems.getNewQuantity());
            productRepository.save(product);
            model.addAttribute("notification", "Pomyślnie edytowano produkt");
        }

        if(!editItems.getNewDate().isEmpty()){
            if(editItems.getNewSalePrice() < product.getPrice()){
                Sale activeSale = saleRepository.findActiveSale(product.getId(), dtf.format(now)).get(0);
                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(activeSale.getDateFrom());
                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(editItems.getNewDate());
                if(saleRepository.findActiveSale(product.getId(), editItems.getNewDate()).size() == 0 || saleRepository.findActiveSale(product.getId(), editItems.getNewDate()).get(0).getId() == activeSale.getId()){
                    if(start.compareTo(end) < 0){
                        if(saleRepository.findNextSale(product.getId(), dtf.format(now)).size() == 0){
                            Date start1 = new SimpleDateFormat("yyyy-MM-dd").parse(saleRepository.findNextSale(product.getId(), dtf.format(now)).get(0).getDateFrom());
                            if (start1.compareTo(end) > 0) {
                                activeSale.setDateTo(editItems.getNewDate());
                                activeSale.setPrice(editItems.getNewSalePrice());
                                saleRepository.save(activeSale);
                                System.out.println(editItems.getNewDate());
                                model.addAttribute("notification", "Pomyślnie edytowano produkt");
                            } else {
                                model.addAttribute("error", "Wybrana data koliduje z inną promocją");
                                return addSale(model, product.getId());
                            }
                        } else {
                            activeSale.setDateTo(editItems.getNewDate());
                            activeSale.setPrice(editItems.getNewSalePrice());
                            saleRepository.save(activeSale);
                            model.addAttribute("notification", "Pomyślnie edytowano produkt");
                        }
                    } else {
                        model.addAttribute("error", "Wybrana data koliduje z inną promocją");
                        return addSale(model, product.getId());
                    }
                } else {
                    model.addAttribute("error", "Wybrana data koliduje z inną promocją");
                    return addSale(model, product.getId());
                }
            } else {
                model.addAttribute("error", "Cena promocji jest wyższa od regularnej ceny");
                return addSale(model, product.getId());
            }
        }

        try {
            if (!editItems.getNewNextDateFrom().isEmpty() && !editItems.getNewNextDateTo().isEmpty()) {
                if (saleRepository.findActiveSale(product.getId(), editItems.getNewNextDateFrom()).size() == 0) {
                    Date start = new SimpleDateFormat("yyyy-MM-dd").parse(editItems.getNewNextDateFrom());
                    Date end = new SimpleDateFormat("yyyy-MM-dd").parse(editItems.getNewNextDateTo());
                    if (start.compareTo(end) <= 0) {
                        if (editItems.getNewNextSalePrice() < product.getPrice()) {
                            saleRepository.save(new Sale(editItems.getNewNextDateFrom(), editItems.getNewNextDateTo(), editItems.getNewNextSalePrice(), product));
                            model.addAttribute("notification", "Pomyślnie edytowano produkt");
                        } else {
                            model.addAttribute("error", "Cena promocji jest wyższa od regularnej ceny");
                            return addSale(model, product.getId());
                        }
                    } else {
                        model.addAttribute("error", "Wybrane daty są niepoprawne");
                        return addSale(model, product.getId());
                    }
                } else {
                    model.addAttribute("error", "Wybrana data koliduje z inną promocją");
                    return addSale(model, product.getId());
                }
            }
        } catch (Exception e){}


        return myController.staff(model);
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
