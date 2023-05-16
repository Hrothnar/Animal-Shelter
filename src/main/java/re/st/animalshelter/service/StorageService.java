package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.Cell;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.repository.StorageRepository;

import java.io.IOException;

@Service
public class StorageService {
    private final StorageRepository storageRepository;

    @Autowired
    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public String getText(ActionDTO actionDTO) {
        Shelter shelter = actionDTO.getShelter();
        Button button = actionDTO.getButton();
        boolean owner = actionDTO.isOwner();
        boolean dependsOnShelter = button.isShelterDependence();
        boolean dependsOnOwner = button.isOwnerDependence();
        String text;
        if (dependsOnShelter) {
            text = dependsOnOwner
                    ? storageRepository.findByButtonAndShelterAndOwner(button, shelter, owner).getText()
                    : storageRepository.findByButtonAndShelter(button, shelter).getText();
        } else {
            text = dependsOnOwner
                    ? storageRepository.findByButtonAndOwner(button, owner).getText()
                    : storageRepository.findByButton(button).getText();
        }
        return text;
    }

    public String getText(Status status) {
        return storageRepository.findByStatus(status).getText();
    }

    public byte[] getPhoto(ActionDTO actionDTO) {
        Shelter shelter = actionDTO.getShelter();
        Button button = actionDTO.getButton();
        boolean owner = actionDTO.isOwner();
        boolean dependsOnShelter = button.isShelterDependence();
        boolean dependsOnOwner = button.isOwnerDependence();
        byte[] photo;
        if (dependsOnShelter) {
            photo = dependsOnOwner
                    ? storageRepository.findByButtonAndShelterAndOwner(button, shelter, owner).getPhoto()
                    : storageRepository.findByButtonAndShelter(button, shelter).getPhoto();
        } else {
            photo = dependsOnOwner
                    ? storageRepository.findByButtonAndOwner(button, owner).getPhoto()
                    : storageRepository.findByButton(button).getPhoto();
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
        storageRepository.save(new Cell(button, shelter, status, owner, text, bytes));
    }
}
