package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.Cell;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.repository.InformationRepository;

import java.io.IOException;

@Service
public class InformationService {
    private final InformationRepository informationRepository;
    private final UserService userService;

    @Autowired
    public InformationService(InformationRepository informationRepository, UserService userService) {
        this.informationRepository = informationRepository;
        this.userService = userService;
    }

    public String getText(ActionDTO actionDTO) {
        Shelter shelter = actionDTO.getShelter();
        Button button = actionDTO.getButton();
        boolean owner = actionDTO.isOwner();
        boolean dependsOnShelter = button.isResponseDependsOnShelter();
        boolean dependsOnOwner = button.isResponseDependsOnOwner();
        String text;
        if (dependsOnShelter) {
            text = dependsOnOwner
                    ? informationRepository.findByButtonAndShelterAndOwner(button, shelter, owner).getText()
                    : informationRepository.findByButtonAndShelter(button, shelter).getText();
        } else {
            text = dependsOnOwner
                    ? informationRepository.findByButtonAndOwner(button, owner).getText()
                    : informationRepository.findByButton(button).getText();
        }
        return text;
    }

    public String getText(Status status) {
        return informationRepository.findByStatus(status).getText();
    }

    public byte[] getPhoto(ActionDTO actionDTO) {
        Shelter shelter = actionDTO.getShelter();
        Button button = actionDTO.getButton();
        boolean owner = actionDTO.isOwner();
        boolean dependsOnShelter = button.isResponseDependsOnShelter();
        boolean dependsOnOwner = button.isResponseDependsOnOwner();
        byte[] photo;
        if (dependsOnShelter) {
            photo = dependsOnOwner
                    ? informationRepository.findByButtonAndShelterAndOwner(button, shelter, owner).getPhoto()
                    : informationRepository.findByButtonAndShelter(button, shelter).getPhoto();
        } else {
            photo = dependsOnOwner
                    ? informationRepository.findByButtonAndOwner(button, owner).getPhoto()
                    : informationRepository.findByButton(button).getPhoto();
        }
        return photo;
    }

    public void saveInformation(Button button, Shelter shelter, Status status, boolean owner, String text, MultipartFile file) {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO
        }
        informationRepository.save(new Cell(button, shelter, status, owner, text, bytes));
    }
}
