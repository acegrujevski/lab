package mk.ukim.finki.wp.lab.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.lab.Service.WorkshopService;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "WorkshopListServlet", urlPatterns = {"", "/"})
public class WorkshopListServlet extends HttpServlet {

    private final WorkshopService workshopService;
    private final SpringTemplateEngine springTemplateEngine;


    public WorkshopListServlet(WorkshopService workshopService, SpringTemplateEngine springTemplateEngine) {
        this.workshopService = workshopService;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);

        WebContext webContext = new WebContext(webExchange);

        webContext.setVariable("workshops", workshopService.listAll());

        String text = req.getParameter("text");
        String maxPriceStr = req.getParameter("maxPrice");
        Double maxPrice = (maxPriceStr == null || maxPriceStr.isEmpty() ? null : Double.parseDouble(maxPriceStr));

        if ((text != null && !text.isEmpty()) || maxPrice != null) {
            webContext.setVariable("workshops", workshopService.search(text, maxPrice));
        } else {
            webContext.setVariable("workshops", workshopService.listAll());
        }

        String errorMessage = req.getParameter("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            webContext.setVariable("errorMessage", errorMessage);
        }

        springTemplateEngine.process("listWorkshops.html", webContext, resp.getWriter());

    }
}
