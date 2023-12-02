package com.alan.webnuocngot.customer_manager;

import com.alan.webnuocngot.admin_manager.AdminController;
import com.alan.webnuocngot.cart.Cart;
import com.alan.webnuocngot.cart.CartService;
import com.alan.webnuocngot.category_manager.Category;
import com.alan.webnuocngot.category_manager.CategoryService;
import com.alan.webnuocngot.product_manager.Product;
import com.alan.webnuocngot.product_manager.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Controller
public class CustomerController {
    @Autowired private CustomerService service;

    @Autowired private CategoryService categoryService;

    @Autowired private ProductService productService;

    @Autowired private CartService cartService;

    @Autowired private AdminController adminController;

    private Customer customerLogin;

    @GetMapping("/admin/customer_manager")
    public String customerList(Model model) {
        if (adminController.checkLogin())
            return "redirect:/admin/login";
        model.addAttribute("accMe", adminController.getAdminLogin());

        List<Customer> listCustomer = service.listCustomer();
        model.addAttribute("listCustomer", listCustomer);

        return "customer_manager/customer_manager";
    }

    @GetMapping("/admin/customer_manager/delete/{id}")
    public String CustomerDelete(@PathVariable("id") Integer id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "the user ID "+ id +" has been deleted.");

        return "redirect:/admin/customer_manager";
    }

    public Customer getCustomerLogin() {
        return customerLogin;
    }

    public void setCustomerLogin(Customer customerLogin) {
        this.customerLogin = customerLogin;
    }

//  -------------------------------  customer view
//    show login
    @GetMapping("/signing")
    public String signing() {
        return "customer_view/login";
    }
//    login
    @PostMapping("/login")
    //nhận form
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        Model model, RedirectAttributes ra) {
        List<Customer> listCustomer = service.listCustomer();
        for (Customer item : listCustomer) {
            if (email.equals(item.getEmail())) {
                if (password.equals(item.getPassword())) {
                    customerLogin = service.findByEmail(email);
                    ra.addFlashAttribute("message", "đăng nhập thành công");
                    return "redirect:/";
                }
            }
        }
        model.addAttribute("message", "Đăng nhập không thành công.");
        return "customer_view/login";
    }
//    show view user
    @GetMapping("")
    public String beverage(Model model) {
        int countSale = productService.countAllBySale();
        List<Product> listProduct = productService.listAll();
        model.addAttribute("listProduct", listProduct);

        model.addAttribute("countSale", countSale);

        List<String> allName = productService.findAllName();
        model.addAttribute("allName", allName);

        model.addAttribute("customerLogin", customerLogin);

        int checkCart = 0;
        if (customerLogin != null)
            checkCart = cartService.countAllByIdC(customerLogin.id);
        model.addAttribute("checkCart", checkCart);

        List<Category> listAllCategory = categoryService.findAllCategory();
        model.addAttribute("listAllCategory", listAllCategory);

        model.addAttribute("pageTitle", "Beverage");

        return "customer_view/index";
    }

//    tìm sản phẩm với code, gas và sale
    @GetMapping("/filter/{code}")
    public String code(@PathVariable("code") String code, Model model ) {
        int countSale = productService.countAllBySale();

        String name = code;

        if (code.equals("coGa") || code.equals("khongGa")) {
            List<Product> listProductByGas = productService.findAllByGas(code);
            model.addAttribute("listProduct", listProductByGas);
            if (code.equals("coGa")) name = "có ga";
            else name = "không có ga";
        }
        else if (code.equals("showSale")) {
            List<Product> listProductBySale = productService.findAllSaleNotNull();
            model.addAttribute("listProduct", listProductBySale);
            model.addAttribute("check", "ok");
            name = "các sản phẩm khuyến mãi";
        }
        else {
            List<Product> listProductByCode = productService.findAllByCode(code);
            model.addAttribute("listProduct", listProductByCode);
            name = categoryService.findNameCatByIdCat(code);
        }

        model.addAttribute("countSale", countSale);
        
        System.out.println("name: "+ name);
        String linkBread = "<a href= '/filter/"+ code +"'>"+ name +"</a>";
        model.addAttribute("linkBread", linkBread);
        model.addAttribute("linkWeb", code);

        List<String> allName = productService.findAllName();
        model.addAttribute("allName", allName);

        model.addAttribute("customerLogin", customerLogin);

        int checkCart = 0;
        if (customerLogin != null)
            checkCart = cartService.countAllByIdC(customerLogin.id);
        model.addAttribute("checkCart", checkCart);

        List<Category> listAllCategory = categoryService.findAllCategory();
        model.addAttribute("listAllCategory", listAllCategory);

        String pageTitle = name;
        for (Category category : listAllCategory) {
            if (name.equals(category.getId())) {
               pageTitle = categoryService.findNameCatByIdCat(name);
            }
        }
        model.addAttribute("pageTitle", pageTitle);
        return "customer_view/index";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam("nameSearch") String nameSearch, Model model) {
        int countSale = productService.countAllBySale();

        List<Product> listAllByName = productService.findAllByName(nameSearch);
        model.addAttribute("listProduct", listAllByName);

        List<String> allName = productService.findAllName();
        model.addAttribute("allName", allName);

        model.addAttribute("countSale", countSale);

        model.addAttribute("customerLogin", customerLogin);

        int checkCart = 0;
        if (customerLogin != null)
            checkCart = cartService.countAllByIdC(customerLogin.id);
        model.addAttribute("checkCart", checkCart);

        List<Category> listAllCategory = categoryService.findAllCategory();
        model.addAttribute("listAllCategory", listAllCategory);

        return "customer_view/index";
    }

//    mua hàng
    @GetMapping("/addCart/{idC}/{idP}/{link}")
    public String addCart(@PathVariable("idC") Integer idC, @PathVariable("idP") Integer idP,
                          @PathVariable("link") String link ,RedirectAttributes ra) {
        if (idC == 0) {
            ra.addFlashAttribute("message", "bạn chưa đăng nhập");

            return "redirect:/signing";
        }

        int checkProduct = cartService.countProductByCustomer(idC, idP);
        if (checkProduct != 0) {
            ra.addFlashAttribute("message", "sản phẩm đã tồn tại trong giỏ hàng");
            if (link.equals("0"))
                return "redirect:/";
            return "redirect:/"+ link;
        }

        Cart cart = new Cart();
        cart.setIdC(idC);
        cart.setIdP(idP);
        cart.setTotal(1);
        cartService.save(cart);

        if (link.equals("0"))
            return "redirect:/";
        return "redirect:/"+ link;
    }
//    vào giỏ hàng.
    @GetMapping("/show-cart")
    public String showCart(RedirectAttributes ra) {
        if (customerLogin == null) {
            ra.addFlashAttribute("message", "bạn chưa đăng nhập");
            return "redirect:/signing";
        }
        return "redirect:/cart";
    }

//    đăng kí
    @GetMapping("/show-signup")
    public String showSingup(Model model) {
        model.addAttribute("customer", new Customer());

        return "customer_view/customer_signup";
    }

    @PostMapping("/signup")
    public String signupCheck(Customer customer, RedirectAttributes ra) {
        List<Customer> customers = service.listCustomer();
        for (Customer customerTemp : customers) {
            if (customerTemp.email.equals(customer.getEmail())) {
                ra.addFlashAttribute("message","Đăng kí không thành công.\n" +
                        " Email trên đã tồn tại");
                return "redirect:/show-signup";
            }
        }
        service.save(customer);
        ra.addFlashAttribute("message","Đăng kí thành công");
        return "redirect:/signing";
    }

//    đăng xuất
    @GetMapping("/logout")
    public String logout() {
        this.customerLogin = null;
        return "redirect:/signing";
    }
//    trang thông tin của khách hàng
    @GetMapping("/info")
    public String showInfoCustomer( Model model) {
        int checkCart = 0;
        if (customerLogin != null)
            checkCart = cartService.countAllByIdC(customerLogin.id);
        model.addAttribute("checkCart", checkCart);

        model.addAttribute("customerLogin", customerLogin);

        return "customer_view/info";
    }
//    sửa thông tin khách hàng
    @PostMapping("/info/edit")
    public String editInfoCustomer(@RequestParam("id") Integer id,
                                   @RequestParam("avatar") MultipartFile multipartFile,
                                   @RequestParam("name") String name, @RequestParam("email") String email,
                                   @RequestParam("phone") String phone, @RequestParam("address") String address,
                                   RedirectAttributes ra) throws IOException {
        Customer customer = service.findById(id);
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        if (!fileName.isEmpty()) {
            customer.setAvatar(fileName);
            Customer savedCustomer = service.save(customer);
            String uploadDir = "./customer-avatars/"+ savedCustomer.getId();

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try(InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                throw new IOException("Could not save uploaded file: "+ fileName);
            }
        }
        service.save(customer);
        customerLogin = service.findById(customerLogin.getId());
        ra.addFlashAttribute("message", "Thay đổi thông tin thành công");
        return "redirect:/info";
    }

//    đổi mật khẩu
    @GetMapping("/info/change-password")
    public String showChangePassword(Model model) {
        int checkCart = 0;
        if (customerLogin != null)
            checkCart = cartService.countAllByIdC(customerLogin.id);
        model.addAttribute("checkCart", checkCart);

        model.addAttribute("customerLogin", customerLogin);

        return "customer_view/change-password";
    }

    @PostMapping("/info/save_change-pass")
    public String changePassword(@RequestParam("id") Integer id, @RequestParam("old-pass") String oldPass,
                                 @RequestParam("new-pass") String newPass, @RequestParam("re-pass") String rePass,
                                 RedirectAttributes ra) {
        Customer customer = service.findById(id);
        if (!oldPass.equals(customer.password)) {
            ra.addFlashAttribute("message", "Bạn nhập sai mật khẩu cũ");
            return "redirect:/info/change-password";
        }
        if (!newPass.equals(rePass)) {
            ra.addFlashAttribute("message", "Mật khẩu nhập lại không khớp với mật khẩu mới của bạn\n hãy nhập lại");
            return "redirect:/info/change-password";
        }
        customer.setPassword(newPass);
        service.save(customer);
        ra.addFlashAttribute("message", "Thay đổi mật khẩu thành công.");
        return "redirect:/info";
    }
}
