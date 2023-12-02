package com.alan.webnuocngot.cart;

import com.alan.webnuocngot.customer_manager.CustomerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CartConTroller {
    @Autowired private CartService service;

    @Autowired private CustomerController customerController;

//    tạo trang giỏ hàng
    @GetMapping("/cart")
    public String cart(Model model) {
        Integer id = customerController.getCustomerLogin().getId();

        model.addAttribute("customerLogin", customerController.getCustomerLogin());

        double total = 0;
        List<String> listCart = service.findAllProductByIdC(id);
        List<List<String>> listCartConvert = new ArrayList<>();
        
        for (String s : listCart) {
            List<String> cart = null;
            cart = List.of(s.split(","));
            listCartConvert.add(cart);
        }

        model.addAttribute("listCartConvert", listCartConvert);

        for (List<String> item : listCartConvert) {
            double totalTemp = Double.parseDouble(item.get(3)) * Double.parseDouble(item.get(8));
            total += totalTemp;
            for (String cart: item) {
                System.out.print(cart);
            }
            System.out.println();
        }
        model.addAttribute("total", total);

        int checkCart = 0;
        if (!listCartConvert.isEmpty()) {
            checkCart = service.countAllByIdC(customerController.getCustomerLogin().getId());
        }
        model.addAttribute("checkCart", checkCart);

        return "customer_view/cart";
    }

//    cộng thêm
    @GetMapping("/cart/plus/{id}")
    public String plusTotal(@PathVariable("id") Integer id, RedirectAttributes ra) {
        Optional<Cart> cart = service.getCard(id);
        cart.get().setTotal(cart.get().getTotal() + 1);
        service.save(cart.get());
        return "redirect:/cart";
    }
//    trừ đi
    @GetMapping("/cart/minus/{id}")
    public String minusTotal(@PathVariable("id") Integer id, RedirectAttributes ra) {
        Optional<Cart> cart = service.getCard(id);
        if ((cart.get().getTotal() - 1) != 0) {
            cart.get().setTotal(cart.get().getTotal() - 1);
            service.save(cart.get());
        }
        return "redirect:/cart";
    }

//    xóa sản phẩm
    @GetMapping("/cart/delete/{id}")
    public String deleteItem(@PathVariable("id") Integer id) {
        service.delete(id);
        return "redirect:/cart";
    }
}
