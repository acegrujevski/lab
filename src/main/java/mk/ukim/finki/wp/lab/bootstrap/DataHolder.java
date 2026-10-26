package mk.ukim.finki.wp.lab.bootstrap;
import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.lab.model.Registration;
import mk.ukim.finki.wp.lab.model.Workshop;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<Workshop> workshops = null;
    public static List<Registration> registrations = null;


    @PostConstruct
    public void init(){

        workshops = new ArrayList<>(10);
        registrations = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            workshops.add(new Workshop("Title"+i, "Category"+i, 505, i));
        }


    }
}
