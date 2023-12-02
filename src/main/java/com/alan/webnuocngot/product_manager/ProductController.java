package com.alan.webnuocngot.product_manager;

import com.alan.webnuocngot.admin_manager.AdminController;
import com.alan.webnuocngot.category_manager.Category;
import com.alan.webnuocngot.category_manager.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class ProductController {
//    field
    @Autowired private ProductService service;

    @Autowired private CategoryService categoryService;
    @Autowired private AdminController adminController;

//    method
//    trả về danh sách product.
    @GetMapping("/products_manager")
    public String showProductList(Model model) {
        if (adminController.checkLogin())
            return "redirect:/admin/login";
        model.addAttribute("accMe", adminController.getAdminLogin());
        List<Product> listProduct = service.listAll();
        model.addAttribute("listProduct", listProduct);
        return "product_manager/products_manager";
    }

//   tạo mới thông tin sản phẩm
    @GetMapping("/products_manager/new")
    public String showNewForm(Model model) {
        model.addAttribute("product",new Product());

        List<Category> listAllCategory = categoryService.findAllCategory();
        model.addAttribute("category", listAllCategory);

        model.addAttribute("pageTitle", "Add new product");
        return"product_manager/product_form";
    }

//    lưu sản phẩm vào cơ sở dữ liệu
    @PostMapping("/products_manager/save")
    public String saveProduct(@RequestParam("idP") Integer id, @RequestParam("pCode") String pCode,
                              @RequestParam("name") String name, @RequestParam("price") double price,
                              @RequestParam("sale") String sale, @RequestParam("expired") String expired,
                              @RequestParam("gas") String gas, @RequestParam("quantity") int quantity,
                              @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes ra) throws IOException {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Product product = new Product();
        if (service.kiemTra(id))
            product = service.getIdProduct(id);

        product.setpCode(pCode);
        product.setNameP(name);
        product.setPrice(price);
        product.setSale(sale);
        product.setExpired(expired);
        product.setGas(gas);
        product.setQuantity(quantity);
        if (!fileName.isEmpty()) {
            product.setImage(fileName);
            System.out.println("url: " + fileName);
            product.setImage(fileName);
            Product saveProduct = service.save(product);
            String uploadDir = "./product-images/" + saveProduct.getIdP();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                System.out.println(filePath.toFile().getAbsolutePath());
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("could not save uploaded file: "+ fileName);
            }
        }
        service.save(product);
        ra.addFlashAttribute("message", "saved is successfully.");
        return "redirect:/admin/products_manager";
    }

//    thay đổi thông tin sản phẩm.
    @GetMapping("/products_manager/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        Product product = null;
        try {
            product = service.get(id);
            model.addAttribute("product", product);

            List<Category> listAllCategory = categoryService.findAllCategory();
            model.addAttribute("category", listAllCategory);

            model.addAttribute("pageTitle", "Edit product (ID: " + id + " )");
            return "product_manager/product_form";
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/products_manager";
        }
    }

    @GetMapping("/products_manager/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("message", "the user ID "+ id +" has been deleted.");
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/products_manager";
    }
}
