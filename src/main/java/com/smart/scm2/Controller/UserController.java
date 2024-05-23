package com.smart.scm2.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.scm2.Entities.Contact;
import com.smart.scm2.Entities.User;
import com.smart.scm2.Helper.Message;
import com.smart.scm2.dao.ContactRepository;
import com.smart.scm2.dao.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute("user")
    public User addCommonData(Principal principal) {
        String username = principal.getName();
        return this.userRepository.getUserByUsername(username);
    }

    @RequestMapping("/index")
    public String dashboard(Model model) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String addContact(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@Valid @ModelAttribute Contact contact, BindingResult result,
            @RequestParam("profileImage") MultipartFile multipartFile, Model model,
            Principal principal, HttpSession session) {

        try {

            if (result.hasErrors()) {
                System.out.println("ERROR : " + result.toString());
                model.addAttribute("contact", contact);
            }

            String name = principal.getName();
            User user = this.userRepository.getUserByUsername(name);

            if (multipartFile.isEmpty()) {
                // if the file is empty then try your message
                System.out.println("File is empty !!");
                contact.setImage("user.jpg");
            } else {
                // upload the file to folder and update the name to contact
                contact.setImage(multipartFile.getOriginalFilename());

                File file = new ClassPathResource("/static/image").getFile();
                Path path = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image is uploaded");
            }

            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepository.save(user);
            System.out.println("Contact added to database !!");
            System.out.println("\n\n" + contact + "\n\n");

            session.setAttribute("message2", new Message("Contact added successfully ! Add more . . .", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message2",
                    new Message("Failed to add contact !! " + e.getMessage() + " ! Try again !!", "danger"));
        }
        return "normal/add_contact_form";
    }

    // show contacts
    // @GetMapping("/show-contacts")
    // public String showContacts(Model model, Principal principal){
    // model.addAttribute("title", "Show Contacts");
    // contacts ki list ko vejni hai

    // option 1

    // String username = principal.getName();
    // User user = this.userRepository.getUserByUsername(username);
    // List<Contact> contacts = user.getContacts();

    // option 2

    // but we made contact repository

    // return "normal/show_contacts";
    // }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") int page, Model model, Principal principal) {

        model.addAttribute("title", "Show Contacts");

        String username = principal.getName();
        int userId = this.userRepository.getUserByUsername(username).getId();

        // page request requires two things -
        // current page, page size (per page)

        Pageable pageable = PageRequest.of(page, 5);

        Page<Contact> contacts = this.contactRepository.findContactsByUser(userId, pageable);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normal/show_contacts";
    }

    // show particular contact detail

    @GetMapping("/contact/{cId}")
    public String contacDetails(@PathVariable("cId") int cId, Model model, Principal principal, HttpSession session) {

        System.out.println("cid " + cId);
        Optional<Contact> contactsOptional = contactRepository.findById(cId);
        Contact contact = contactsOptional.get();

        String username = principal.getName();
        User user = this.userRepository.getUserByUsername(username);

        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("contacts", contact);
            session.setAttribute("message3", new Message("Contact details fetched successfully !!", "primary"));
            model.addAttribute("title", contact.getName());
        }

        return "normal/contact_details";
    }

    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable("cId") int cId, Principal principal, HttpSession session) {
        Optional<Contact> contactOptional = this.contactRepository.findById(cId);

        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            String username = principal.getName();
            User user = this.userRepository.getUserByUsername(username);

            if (user.getId() == contact.getUser().getId()) {
                contact.setUser(null);
                this.contactRepository.delete(contact);
                session.setAttribute("message4", new Message("Contact deleted successfully !!", "success"));
            } else {
                session.setAttribute("message4", new Message("You are not authorized to delete this contact.", "danger"));
            }
        }

        return "redirect:/user/show-contacts/0";
    }

    // update form handler
    @PostMapping("/update-contact/{cid}")
    public String UpdateContact(@PathVariable("cid") int cid, Model model)
    {
        model.addAttribute("title", "Update Contact");
        Contact contact = this.contactRepository.findById(cid).get();
        model.addAttribute("contact", contact);
        return "normal/update_contact";
    }

    // update contact
    @PostMapping("/process-update")  
    public String processUpdate(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile multipartFile, Principal principal, Model model, HttpSession session){

        try {

            // old contact details
            Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();
            
            // image
            if (!multipartFile.isEmpty()) {
                // file work...
                // rewrite
                // delete old photo
                


                // update new photo
                File saveFile = new ClassPathResource("/static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                contact.setImage(multipartFile.getOriginalFilename());

            } else {
                contact.setImage(oldContactDetail.getImage());
            }
            User user = this.userRepository.getUserByUsername(principal.getName());
            contact.setUser(user);
            this.contactRepository.save(contact);
            session.setAttribute("message4", new Message("Contact Updated Successfully !!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("contact name : "+contact.getName());
        System.out.println("contact id : "+contact.getcId());

        return "redirect:/user/contact/"+contact.getcId();
    }


    // your profile handler
    @GetMapping("/profile")
    public String yourProfile(Model model){
        model.addAttribute("title", "Profile page");
        return "normal/profile";
    }
}
