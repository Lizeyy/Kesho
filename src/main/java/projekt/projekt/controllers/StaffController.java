package projekt.projekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projekt.projekt.entities.Category;
import projekt.projekt.entities.Photo;
import projekt.projekt.entities.Producer;
import projekt.projekt.entities.Product;
import projekt.projekt.models.AddItems;
import projekt.projekt.repositories.CategoryRepository;
import projekt.projekt.repositories.PhotoRepository;
import projekt.projekt.repositories.ProducerRepository;
import projekt.projekt.repositories.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private MyController myController;


    @PostMapping("/staff/addCategory")
    public String addCategory(Model model, AddItems addItems){
        if(addItems.getCategoryName().isEmpty()){
            model.addAttribute("notification", "Wystąpił błąd przy dodawaniu kategorii");
        } else {
            Category category;
            if(addItems.getCategorySub().equals("BRAK")){
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
        if(addItems.getProducerName().isEmpty()){
            model.addAttribute("notification", "Wystąpił błąd przy dodawaniu producenta");
        } else {
            Producer producer = new Producer(addItems.getProducerName());
            producerRepository.save(producer);
            model.addAttribute("notification", "Pomyślnie dodano nowego producenta");
        }
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
                    Files.write(mainPath, main.getBytes());
                    Photo photo = new Photo(mainPath.toString(), true, product);
                    photoRepository.save(photo);

                    if(files.length > 0){
                        int i = 1;
                        for(MultipartFile file : files){
                            Path path = Paths.get(pathDir + addItems.getProductName() + "-" + i + ".jpeg");
                            try{
                                Files.write(path, file.getBytes());
                                Photo photo1 = new Photo(path.toString(), false, product);
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
