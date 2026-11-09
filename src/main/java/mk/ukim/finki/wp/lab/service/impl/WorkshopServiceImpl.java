package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.service.WorkshopService;
import mk.ukim.finki.wp.lab.model.Instructor;
import mk.ukim.finki.wp.lab.model.Workshop;
import mk.ukim.finki.wp.lab.repository.InstructorRepository;
import mk.ukim.finki.wp.lab.repository.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final InstructorRepository instructorRepository;

    public WorkshopServiceImpl(WorkshopRepository workshopRepository, InstructorRepository instructorRepository) {
        this.workshopRepository = workshopRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public List<Workshop> listAll() {
        return workshopRepository.findAll();
    }

    @Override
    public List<Workshop> search(String text, Double maxPrice) {
        return workshopRepository.search(text, maxPrice);
    }

    @Override
    public Optional<Workshop> findById(Long id) {
        return workshopRepository.findById(id);
    }

    @Override
    public Workshop create(String title, String category, Double price, Integer capacity, Long instructorId) {
        if (title == null || title.isBlank() || category == null || category.isBlank()) {
            throw new IllegalArgumentException("Title and category cannot be empty.");
        }
        if (price == null || price < 0 || capacity == null || capacity < 0) {
            throw new IllegalArgumentException("Price and capacity cannot be null or negative.");
        }
        if (instructorId == null) {
            throw new IllegalArgumentException("Instructor ID cannot be null.");
        }

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found."));

        Long newId = (long) (Math.random() * 1000);
        Workshop workshop = new Workshop(newId, title, category, price, capacity, instructor);
        return workshopRepository.save(workshop);
    }

    @Override
    public Workshop update(Long id, String title, String category, Double price, Integer capacity, Long instructorId) {
        if (title == null || title.isBlank() || category == null || category.isBlank()) {
            throw new IllegalArgumentException("Title and category cannot be empty.");
        }
        if (price == null || price < 0 || capacity == null || capacity < 0) {
            throw new IllegalArgumentException("Price and capacity cannot be null or negative.");
        }
        if (instructorId == null) {
            throw new IllegalArgumentException("Instructor ID cannot be null.");
        }

        Workshop workshop = workshopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("WorkshopNotFound"));

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found."));

        workshop.setTitle(title);
        workshop.setCategory(category);
        workshop.setPrice(price);
        workshop.setCapacity(capacity);
        workshop.setInstructor(instructor);

        return workshopRepository.save(workshop);
    }

    @Override
    public void deleteById(Long id) {
        workshopRepository.deleteById(id);
    }
}