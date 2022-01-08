package projekt.projekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekt.projekt.entities.Category;
import projekt.projekt.entities.Product;
import projekt.projekt.models.AddItems;
import projekt.projekt.models.EditItems;
import projekt.projekt.models.Session;
import projekt.projekt.repositories.CategoryRepository;
import projekt.projekt.repositories.PhotoRepository;
import projekt.projekt.repositories.ProducerRepository;
import projekt.projekt.repositories.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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

    private Session session = new Session();

    @RequestMapping(value = {"/index", "/"})
    public String index(Model model, Principal principal){
        if(principal != null) model.addAttribute("auth", true);
        model.addAttribute("catUp", categoryRepository.findBySubcategoryIsNull());
        model.addAttribute("catDown", categoryRepository.findBySubcategoryNotNull());
        return "/index";
    }

    @GetMapping("/list")
    public String list(Model model, Principal principal, @RequestParam(value = "cat") long cat, @RequestParam(value = "page", required = false, defaultValue = "0") int page){
        if(principal != null) model.addAttribute("auth", true);

        if(categoryRepository.findAllById(Collections.singleton(cat)).size() == 0){
            model.addAttribute("error", "Nie znaleziono produktów");

            model.addAttribute("cat", cat);
            return "list";
        }

        Category category = categoryRepository.findAllById(Collections.singleton(cat)).get(0);
        Page<Product> products0 = productRepository.findByCategory(category, PageRequest.of(0, 7));
        if(products0.getTotalElements() > 0){
            session.setCount(products0.getTotalPages());
            if(page > 0 && page <= session.getCount()) session.setPage(page - 1);
            Page<Product> products = productRepository.findByCategory(category, PageRequest.of(session.getPage(), 7));
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
        } else model.addAttribute("error", "Nie znaleziono produktów");

        return "list";
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
        return "staff";
    }


    public String admin(Model model){
        return "admin";
    }
    public String client(Model model){
        return "client";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, Principal principal){
        if(request.isUserInRole("ROLE_ADMIN")) return admin(model);
        if(request.isUserInRole("ROLE_STAFF")) return staff(model);
        if(request.isUserInRole("ROLE_CLIENT")) return client(model);
        else {
            return "login";
        }
    }

    @ExceptionHandler
    public String handlerException(Model model, Exception exception) {
        String message = exception.getMessage();
        model.addAttribute("error", message);
        return "error";
    }
}
