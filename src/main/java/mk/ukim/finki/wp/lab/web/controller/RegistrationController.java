package mk.ukim.finki.wp.lab.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.wp.lab.model.Registration;
import mk.ukim.finki.wp.lab.service.RegistrationService;
import mk.ukim.finki.wp.lab.service.WorkshopService;
import mk.ukim.finki.wp.lab.model.Workshop;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;
    private final WorkshopService workshopService;

    public RegistrationController(RegistrationService registrationService, WorkshopService workshopService) {
        this.registrationService = registrationService;
        this.workshopService = workshopService;
    }

    @PostMapping("/registration")
    public String register(@RequestParam Long workshopId,
                           @RequestParam String participantName,
                           @RequestParam String participantEmail,
                           @RequestParam Integer numSeats,
                           HttpServletRequest request,
                           @RequestHeader("User-Agent") String userAgent,
                           Model model) {
        try {
            Workshop workshop = workshopService.findById(workshopId)
                    .orElseThrow(() -> new IllegalArgumentException("Workshop not found"));

            Registration registration = registrationService.register(
                    workshop.getTitle(),
                    participantName,
                    participantEmail,
                    numSeats
            );

            model.addAttribute("registration", registration);
            model.addAttribute("clientIp", request.getRemoteAddr());
            model.addAttribute("userAgent", userAgent);

            return "registrationConfirmation";
        } catch (IllegalArgumentException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Invalid registration data";
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            return "redirect:/workshops?errorMessage=" + encodedMessage;
        }
    }
}