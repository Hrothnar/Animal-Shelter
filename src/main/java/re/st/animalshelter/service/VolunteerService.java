package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.entity.Volunteer;
import re.st.animalshelter.repository.VolunteerRepository;

import java.util.LinkedList;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    @Autowired
    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public LinkedList<Volunteer> getAllVolunteers() {
        return new LinkedList<>(volunteerRepository.findAll());
    }

    public Volunteer getVolunteerById(long id) {
       return volunteerRepository.findById(id).orElseThrow(RuntimeException::new); //TODO
    }

    public void saveVolunteer(Volunteer volunteer) {
        volunteerRepository.save(volunteer);
    }
}
