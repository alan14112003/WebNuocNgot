package com.alan.webnuocngot.admin_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.ManyToOne;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private AdminService service;
    private Admin adminLogin;

    private Admin superAdmin;
    public Admin getAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(Admin adminLogin) {
        this.adminLogin = adminLogin;
    }

    //    đăng nhập
    @GetMapping("/login")
    public String admin_login(Model model) {
        model.addAttribute("admin", new Admin());

        return "admin_manager/admin_login";
    }
    
    @PostMapping("/login_check")
    public String checkLogin(Admin admin, Model model, RedirectAttributes ra) {
        List<Admin> list = service.listAdmin();
        for (Admin a : list) {
            if (a.getEmail().equals(admin.getEmail()) && a.getPassword().equals(admin.getPassword())) {
                if (a.isEnabled()) {
                    model.addAttribute("message", "login is successfully.");
                    this.adminLogin = service.getMe(admin.getEmail());
                    System.out.println("admin: "+ adminLogin.getName()+", email: "+ adminLogin.getEmail());
                    return "redirect:/admin";
                } else {
                    ra.addFlashAttribute("message", "you need approval.");
                    return "redirect:/admin/login";
                }
            }
        }
        ra.addFlashAttribute("message", "login is false.");

        return "redirect:/admin/login";
    }

//    đăng kí
    @GetMapping("/signup")
    public String admin_signup(Model model) {
        model.addAttribute("admin", new Admin());

        return "admin_manager/admin_signup";
    }
    @PostMapping("/signup_check")
    public String checkSignup(Admin admin, Model model, RedirectAttributes ra) {
        if (((!admin.getName().isEmpty()) && (admin.getName() != null))
        && ((!admin.getSdt().isEmpty()) && (admin.getSdt() != null))
        && ((!admin.getEmail().isEmpty()) && (admin.getEmail() != null))
        && ((!admin.getPassword().isEmpty()) && (admin.getPassword() != null))) {
            service.save(admin);
            if (service.searchEmail(admin.getEmail()) == 0) {
                model.addAttribute("admin", new Admin());
                model.addAttribute("message", "sign up is false.");
                return "admin_manager/admin_signup";
            }
            model.addAttribute("admin", new Admin());
            model.addAttribute("message", "sign up is successfully.\n Please wait for approval");
            return "admin_manager/admin_login";
        }
        model.addAttribute("admin", new Admin());
        model.addAttribute("message", "sign up is false.");

        return "admin_manager/admin_signup";
    }

//  trả ra danh sách admin và trả ra một danh sách tên admin
    @GetMapping("/manager-admin")
    public String adminListMng(Model model) {
        superAdmin = service.getMe("superadmin@gmail.com");
        Admin accMe = new Admin();
        if (this.adminLogin == null) {
            return "redirect:/admin/login";
        }
        accMe = service.getMe(this.adminLogin.getEmail());
        model.addAttribute("accMe", accMe);
        System.out.println("adminId: "+ accMe.getIdA() +", admin name: "+ accMe.getName());
        List<Admin> list = service.listAdminNotId(accMe.getIdA());
        List<String> listName = service.listNameAdmin(superAdmin.getIdA());
        listName.remove(superAdmin.getName());

        model.addAttribute("adminList", list);
        model.addAttribute("nameAdminList", listName);
        model.addAttribute("admin", new Admin());

        return "admin_manager/manager-admin";
    }

//  phương thức search
    @PostMapping("/manager-admin/search")
    public String searchAdmin(Admin admin, Model model) {
        Admin accMe = service.getMe(this.adminLogin.getEmail());
        model.addAttribute("accMe", accMe);
        List<Admin> listSearch = service.searchAllNameAdmin(admin.getName(), superAdmin.getIdA());
        List<String> listName = service.listNameAdmin(superAdmin.getIdA());
        model.addAttribute("adminList", listSearch);
        model.addAttribute("nameAdminList", listName);
        model.addAttribute("admin", new Admin());

        return "admin_manager/manager-admin";
    }

//  xóa một đối tượng
    @GetMapping("/manager-admin/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Integer id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "the user ID "+ id +" has been deleted.");

        return "redirect:/admin/manager-admin";
    }

//    duyệt một đối tượng
    @GetMapping("/manager-admin/update/{id}")
    public String udateAdmin(@PathVariable("id") Integer id, RedirectAttributes ra) {
        Admin admin = service.getAdmin(id);
        if (admin.isEnabled())
            admin.setEnabled(false);
        else admin.setEnabled(true);
        service.save(admin);
        ra.addFlashAttribute("message", "the user ID "+ id +" is approved.");

        return "redirect:/admin/manager-admin";
    }

//   thay đổi thông tin admin
    @GetMapping("/edit")
    public String adminEdit(Model model, RedirectAttributes ra) {
        if (adminLogin == null) {
            ra.addFlashAttribute("mesage", "your account is null");
            return "redirect:/admin/manager-admin";
        }
        Admin accMe = service.getMe(this.adminLogin.getEmail());
        model.addAttribute("accMe", accMe);
        model.addAttribute("pageTitle", "Edit admin (ID: " + accMe.getIdA() + " )");

        return "admin_manager/admin_form";
    }
    @PostMapping("/admin_manager/save")
    public String adminSaveInfo(@RequestParam("id") Integer id, @RequestParam("name") String name,
                                @RequestParam("email") String email, @RequestParam("sdt") String sdt,
                                @RequestParam("avatar") String avatar, RedirectAttributes ra) {
        Admin admin = service.getAdmin(id);
        admin.setName(name);
        admin.setEmail(email);
        admin.setSdt(sdt);
        admin.setAvatar(avatar);
        service.save(admin);
        ra.addFlashAttribute("message", "saved is successfully.");

        return "redirect:/admin/manager-admin";
    }

//    thay đổi mật khẩu admin.
    @GetMapping("/editPassword")
    public String newFormPass(Model model) {
        Admin accMe = service.getMe(this.adminLogin.getEmail());
        model.addAttribute("accMe", accMe);
        model.addAttribute("pageTitle", "Edit password");

        return "admin_manager/admin_changepass";
    }
    @PostMapping("/admin_manager/changePass")
    public String chagePassword(@RequestParam("oldPass") String oldPass,
                                @RequestParam("newPass") String newPass, @RequestParam("rePass") String rePass,
                                RedirectAttributes ra) {
        System.out.println("oldPass: "+ oldPass +", newPass: "+ newPass +", rePass: "+ rePass);
        Admin me = service.getMe(this.adminLogin.getEmail());
        if (!oldPass.equals(me.getPassword())) {
            ra.addFlashAttribute("message", "Your old password is false");
            return "redirect:/admin/editPassword";
        } else {
            if (!newPass.equals(rePass)) {
                ra.addFlashAttribute("message", "The password you entered is in conflict");
                return "redirect:/admin/editPassword";
            } else {
                me.setPassword(newPass);
                service.save(me);
                ra.addFlashAttribute("message", "save is successfully");
            }
        }

        return "redirect:/admin/manager-admin";
    }

//    hàm trả về ràng buộc phải đăng nhập.
    public boolean checkLogin() {
        return this.adminLogin == null;
    }

    public boolean checkSuperAdmin() {
        return this.adminLogin.getEmail().equals("superadmin@gmail.com");
    }
}
