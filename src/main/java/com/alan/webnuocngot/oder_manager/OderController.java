package com.alan.webnuocngot.oder_manager;

import com.alan.webnuocngot.admin_manager.AdminController;
import com.alan.webnuocngot.cart.Cart;
import com.alan.webnuocngot.cart.CartService;
import com.alan.webnuocngot.product_manager.Product;
import com.alan.webnuocngot.product_manager.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class OderController {
    @Autowired OderService service;

    @Autowired private OderLineService oderLineService;
    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;
    @Autowired AdminController adminController;


//    khởi tạo trang danh sách oder.
    @GetMapping("/admin/oder_manager")
    public String listOrder(Model model) {
        if (adminController.checkLogin())
            return "redirect:/admin/login";
        model.addAttribute("accMe", adminController.getAdminLogin());
        List<Oder> oderList = service.findAllOrder();
        model.addAttribute("oderList", oderList);
        return "oder_manager/oder_manager";
    }
//    Khởi tạo trang hóa đơn.
    @GetMapping("/admin/bill/{id}")
    public String billOCP(@PathVariable("id") Integer id, Model model) {
        double total = 0;
        if (adminController.checkLogin())
            return "redirect:/admin/login";
        String billO = service.findO(id);
        String[] billOArr = billO.split(",");
        model.addAttribute("billOArr", billOArr);

        String billC = service.findC(id);
        String[] billCArr = billC.split(",");
        model.addAttribute("billCArr", billCArr);

        List<String> billOlP = service.findOlP(id);
        List<List<String>> billOlPArr = new ArrayList<>();
        for (int j = 0; j < billOlP.size(); j ++) {
            String bill = billOlP.get(j);
            String[] billtemp = bill.split(",");
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(j+1));
            list.addAll(Arrays.asList(billtemp));
            if (!Objects.equals(list.get(4), ""))
                list.set(4, list.get(4)+"%");
            billOlPArr.add(list);
        }
        for (int i = 0; i<billOlPArr.size(); i++) {
            List<String> listBill = billOlPArr.get(i);
            String thanhTien = String.valueOf(Double.parseDouble(listBill.get(3)) * Integer.parseInt(listBill.get(5)));
            listBill.add(thanhTien);
            billOlPArr.set(i, listBill);
        }
        model.addAttribute("billOlPArr", billOlPArr);
        for (List<String> strings : billOlPArr) {
            double totalP = Double.parseDouble(strings.get(3)) * Integer.parseInt(strings.get(5));
            total += totalP;
        }
        model.addAttribute("total", total+15000);
        return "oder_manager/bill";
    }

//    khi khách hàng mua sản phẩm.
    @GetMapping("/order/{idC}")
    public String customerOrderProducts(@PathVariable("idC") Integer idC, RedirectAttributes ra) {
        int idOrder, check = 0;
        if (cartService.countAllByIdC(idC) == 0) {
            ra.addFlashAttribute("message", "bạn chưa có sản phẩm nào trong giỏ hàng.\n" +
                    "vui lòng hãy chọn cho mình những sản phẩm ưng ý nhé ^-^ ");
            return "redirect:/cart";
        }
//        lấy danh sách product trong cart
        List<String> listAllCart = cartService.findAllIdPByIdC(idC);
        List<List<String>> listAllCartConvert = new ArrayList<>();
        for (String cart : listAllCart) {
            String[] cartArrTemp = cart.split(",");
            listAllCartConvert.add(List.of(cartArrTemp));
        }

//        kiểm tra xem số lượng mỗi sản phẩm có lớn hơn sản phẩm hiện có không
        StringBuilder productName = new StringBuilder();
        int checkProduct = 0, checkOder = 0;
        for (List<String> cartItem: listAllCartConvert) {
            Product idProduct = productService.getIdProduct(Integer.valueOf(cartItem.get(0)));
            if (Integer.parseInt(cartItem.get(1)) <= idProduct.getQuantity()) {
                idProduct.setQuantity(idProduct.getQuantity() - Integer.parseInt(cartItem.get(1)));
            } else {
                checkOder = 1;
                if (checkProduct == 0) {
                    productName = new StringBuilder(idProduct.getNameP());
                    checkProduct = 1;
                }
                else
                    productName.append(" và ").append(idProduct.getNameP());
            }
        }
        if (checkOder == 1) {
            ra.addFlashAttribute("message", "đặt hàng không thành công,\n số lượng sản phẩm ["+ productName +"] trong kho không đủ.");
            return "redirect:/cart";
        }
//        tạo ra một id oder
        do {
            idOrder = ThreadLocalRandom.current().nextInt(10000, 99999);
            List<Oder> allOrder = service.findAllOrder();
            for (Oder orderItem : allOrder) {
                if (idOrder == orderItem.id) {
                    check = 1;
                    break;
                }
            }
        } while (check != 0);
//      tạo mới một oder
        Oder oder = new Oder();
        oder.id = idOrder;
        oder.customer = idC;
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        oder.oderDate = current.format(formatter);
        service.saveOrder(oder);

//        orderline

        for (List<String> cart : listAllCartConvert) {
            OderLine orderLine = new OderLine();
            orderLine.oderId = idOrder;
            orderLine.productId = Integer.valueOf(cart.get(0));
            orderLine.oderQuantity = Integer.parseInt(cart.get(1));
            oderLineService.save(orderLine);
        }
//      xóa danh sách sản phẩm trong cart.
        List<Cart> allCartByIdC = cartService.findAllCartByIdC(idC);
        for (Cart cartItem : allCartByIdC) {
            cartService.delete(cartItem.getId());
        }

        ra.addFlashAttribute("message", "đặt hàng thành công,\n đơn hàng sẽ được gửi đi trong ngày.");
        return "redirect:/cart";
    }
}
