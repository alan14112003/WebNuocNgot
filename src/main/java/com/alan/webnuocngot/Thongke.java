package com.alan.webnuocngot;


import com.alan.webnuocngot.admin_manager.AdminController;
import com.alan.webnuocngot.admin_manager.AdminService;
import com.alan.webnuocngot.category_manager.CategoryService;
import com.alan.webnuocngot.customer_manager.CustomerService;
import com.alan.webnuocngot.oder_manager.OderService;
import com.alan.webnuocngot.product_manager.Product;
import com.alan.webnuocngot.product_manager.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class Thongke {
//    field
    @Autowired private CustomerService customerService;
    @Autowired private ProductService productService;
    @Autowired private AdminService adminService;
    @Autowired private OderService oderService;
    @Autowired private AdminController adminController;
    @Autowired private CategoryService categoryService;
    
//   Khởi tạo ra trang thống kê.
    @GetMapping("")
    public String statistical(Model model) {
        Integer userNumber = customerService.countAllCustomer();
        Integer adminNumber = adminService.countAllAdmin();
        Integer pCodeNumber = productService.countDistinctPCode();
        Integer countAllQuantity = productService.countQuantity();
        Integer oderNumber = oderService.countOder();
        if (adminController.checkLogin())
            return "redirect:/admin/login";
        model.addAttribute("accMe", adminController.getAdminLogin());

        List<String> listQuantityByCode = new ArrayList<>();
        List<String> listCode = productService.findDistinctCode();
        for (String code : listCode) {
            int num = productService.countQuantityByCode(code);
            String codeName = categoryService.findNameCatByIdCat(code);
            String quantity = codeName +": "+ num;
            listQuantityByCode.add(quantity);
        }
        
        model.addAttribute("userNumber", userNumber);
        model.addAttribute("adminNumber", adminNumber);
        model.addAttribute("productNumber", countAllQuantity);
        model.addAttribute("pCodeNumber", pCodeNumber);
        model.addAttribute("listQuantityByCode", listQuantityByCode);
        model.addAttribute("oderNumber", oderNumber);

        if (adminController.checkSuperAdmin())
            model.addAttribute("superAdmin", "ok");


        return "statistical_manager/statistical";
    }
}
