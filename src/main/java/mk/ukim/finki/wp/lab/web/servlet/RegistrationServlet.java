package mk.ukim.finki.wp.lab.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.lab.Service.RegistrationService;
import mk.ukim.finki.wp.lab.model.Registration;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    private final RegistrationService registrationService;
    private final SpringTemplateEngine springTemplateEngine;

    public RegistrationServlet(RegistrationService registrationService, SpringTemplateEngine springTemplateEngine) {
        this.registrationService = registrationService;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String workshopTitle = req.getParameter("workshopTitle");
        String participantName = req.getParameter("participantName");
        String participantEmail = req.getParameter("participantEmail");
        String numberOfSeatsStr = req.getParameter("numSeats");
        int numberOfSeats = (numberOfSeatsStr == null || numberOfSeatsStr.isEmpty()) ? 0 : Integer.parseInt(numberOfSeatsStr);


        try {
            Registration registration = registrationService.register(workshopTitle, participantName, participantEmail, numberOfSeats);

            IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);

            WebContext webContext = new WebContext(webExchange);

            webContext.setVariable("registration", registration);
            webContext.setVariable("clientIp", req.getRemoteAddr());
            webContext.setVariable("userAgent", req.getHeader("User-Agent"));

            springTemplateEngine.process("registrationConfirmation.html", webContext, resp.getWriter());

        }
        catch (IllegalArgumentException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Choose a workshop";
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            resp.sendRedirect("/?errorMessage=" + encodedMessage);
        }
    }
}
