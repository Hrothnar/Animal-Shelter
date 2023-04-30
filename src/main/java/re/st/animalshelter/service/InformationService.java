package re.st.animalshelter.service;

import org.springframework.stereotype.Service;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.repository.InformationRepository;

@Service
public class InformationService {
    private final InformationRepository informationRepository;
    private final UserService userService;

    public InformationService(InformationRepository informationRepository, UserService userService) {
        this.informationRepository = informationRepository;
        this.userService = userService;
    }

    public String getText(Action action, boolean owner) {
        Shelter shelter = action.getShelter();
        Button button = action.getButton();
        boolean dependsOnShelter = button.isResponseDependsOnShelter();
        boolean dependsOnOwner = button.isResponseDependsOnOwner();
        String text = "";
        if (dependsOnShelter) {
            if (dependsOnOwner) {
                text = informationRepository.getByButtonAndShelterAndOwner(button, shelter, owner).getText();
            } else {
                text = informationRepository.getByButtonAndShelter(button, shelter).getText();
            }
        } else {
            if (dependsOnOwner) {
                text = informationRepository.getByButtonAndOwner(button, owner).getText();
            } else {
                text = informationRepository.getByButton(button).getText();
            }
        }
        return text;
    }

}
