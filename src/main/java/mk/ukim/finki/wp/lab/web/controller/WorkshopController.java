package mk.ukim.finki.wp.lab.web.controller;

import mk.ukim.finki.wp.lab.service.InstructorService;
import mk.ukim.finki.wp.lab.service.WorkshopService;
import mk.ukim.finki.wp.lab.model.Workshop;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/workshops")
public class WorkshopController {
    private final WorkshopService workshopService;
    private final InstructorService instructorService;

    public WorkshopController(WorkshopService workshopService, InstructorService instructorService) {
        this.workshopService = workshopService;
        this.instructorService = instructorService;
    }

    @GetMapping("/")
    public String redirectToWorkshops() {
        return "redirect:/workshops";
    }

    @GetMapping
    public String getWorkshopsPage(@RequestParam(required = false) String text,
                                   @RequestParam(required = false) Double maxPrice,
                                   @RequestParam(required = false) String errorMessage,
                                   Model model){

        List<Workshop> workshops;

        if ((text != null && !text.isEmpty()) || maxPrice != null) {
            workshops = workshopService.search(text, maxPrice);
        } else {
            workshops = workshopService.listAll();
        }

        model.addAttribute("workshops", workshops);

        if (errorMessage != null && !errorMessage.isEmpty())
            model.addAttribute("errorMessage", errorMessage);

        return "listWorkshops";
    }

    @PostMapping("/add")
    public String saveWorkshop(@RequestParam String title,
                               @RequestParam String category,
                               @RequestParam Double price,
                               @RequestParam Integer capacity,
                               @RequestParam Long instructorId) {
        try {
            workshopService.create(title, category, price, capacity, instructorId);
            return "redirect:/workshops";
        } catch (IllegalArgumentException e) {
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/workshops?errorMessage=" + encodedMessage;
        }
    }

    @PostMapping("/edit/{workshopId}")
    public String editWorkshop(@PathVariable Long workshopId,
                               @RequestParam String title,
                               @RequestParam String category,
                               @RequestParam Double price,
                               @RequestParam Integer capacity,
                               @RequestParam Long instructorId) {
        try {
            workshopService.update(workshopId, title, category, price, capacity, instructorId);
            return "redirect:/workshops";
        } catch (IllegalArgumentException e) {
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/workshops?errorMessage=" + encodedMessage;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteWorkshop(@PathVariable Long id) {
        workshopService.deleteById(id);
        return "redirect:/workshops";
    }

    @GetMapping("/workshop-form")
    public String getAddWorkshopPage(Model model) {
        model.addAttribute("instructors", instructorService.findAll());
        return "workshop-form";
    }

    @GetMapping("/workshop-form/{id}")
    public String getEditWorkshopForm(@PathVariable Long id, Model model) {
        if (workshopService.findById(id).isPresent()) {
            model.addAttribute("workshop", workshopService.findById(id).get());
            model.addAttribute("instructors", instructorService.findAll());
            return "workshop-form";
        }
        return "redirect:/workshops?errorMessage=WorkshopNotFound";
    }
}