package re.st.animalshelter.utility;

import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Command;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.particular.button.*;
import re.st.animalshelter.response.particular.command.FinishCommand;
import re.st.animalshelter.response.particular.command.StartCommand;
import re.st.animalshelter.response.particular.status.ContactInfoStatus;
import re.st.animalshelter.response.particular.status.NoneStatus;
import re.st.animalshelter.response.particular.status.ReportPhotoStatus;
import re.st.animalshelter.response.particular.status.ReportTextStatus;
import re.st.animalshelter.response.particular.connect.Controller;
import re.st.animalshelter.response.particular.connect.Sender;

import java.util.HashMap;
import java.util.Optional;

@Component
public class Initializer {
    private final CatShelterButton catShelterButton;
    private final DogShelterButton dogShelterButton;
    private final CynologistButton cynologistButton;
    private final DisabledAnimalButton disabledAnimalButton;
    private final DocumentsButton documentsButton;
    private final DriverPermitButton driverPermitButton;
    private final ReportButton reportButton;
    private final RulesButton rulesButton;
    private final ShelterInfoButton shelterInfoButton;
    private final TakeAnimalButton takeAnimalButton;
    private final NoneStatus noneStatus;
    private final ContactInfoStatus contactInfoStatus;
    private final ReportPhotoStatus reportPhotoStatus;
    private final ReportTextStatus reportTextStatus;
    private final StartCommand startCommand;
    private final FinishCommand finishCommand;
    private final FirstButton firstButton;
    private final CallVolunteerButton callVolunteerButton;
    private final MapButton mapButton;
    private final LeaveContactInfoButton leaveContactInfoButton;

    public Initializer(CallVolunteerButton callVolunteerButton,
                       CatShelterButton catShelterButton,
                       DogShelterButton dogShelterButton,
                       CynologistButton cynologistButton,
                       DisabledAnimalButton disabledAnimalButton,
                       DocumentsButton documentsButton,
                       DriverPermitButton driverPermitButton,
                       LeaveContactInfoButton leaveContactInfoButton,
                       MapButton mapButton,
                       ReportButton reportButton,
                       RulesButton rulesButton,
                       ShelterInfoButton shelterInfoButton,
                       TakeAnimalButton takeAnimalButton,
                       NoneStatus noneStatus,
                       ContactInfoStatus contactInfoStatus,
                       ReportPhotoStatus reportPhotoStatus,
                       ReportTextStatus reportTextStatus,
                       StartCommand startCommand,
                       FinishCommand finishCommand,
                       FirstButton firstButton) {
        this.catShelterButton = catShelterButton;
        this.dogShelterButton = dogShelterButton;
        this.cynologistButton = cynologistButton;
        this.disabledAnimalButton = disabledAnimalButton;
        this.documentsButton = documentsButton;
        this.driverPermitButton = driverPermitButton;
        this.reportButton = reportButton;
        this.rulesButton = rulesButton;
        this.shelterInfoButton = shelterInfoButton;
        this.takeAnimalButton = takeAnimalButton;
        this.noneStatus = noneStatus;
        this.contactInfoStatus = contactInfoStatus;
        this.reportPhotoStatus = reportPhotoStatus;
        this.reportTextStatus = reportTextStatus;
        this.startCommand = startCommand;
        this.finishCommand = finishCommand;
        this.firstButton = firstButton;
        this.callVolunteerButton = callVolunteerButton;
        this.mapButton = mapButton;
        this.leaveContactInfoButton = leaveContactInfoButton;
    }

    public Optional<Sender> getSender(String code) {
        HashMap<String, Sender> buttons = new HashMap<>();
        buttons.put(Command.START.getCode(), firstButton);
        buttons.put(Button.CAT_SHELTER.getCode(), catShelterButton);
        buttons.put(Button.DOG_SHELTER.getCode(), dogShelterButton);
        buttons.put(Button.DRIVER_PERMIT.getCode(), driverPermitButton);
        buttons.put(Button.DOCUMENTS.getCode(), documentsButton);
        buttons.put(Button.SHELTER_INFO.getCode(), shelterInfoButton);
        buttons.put(Button.TAKE_ANIMAL.getCode(), takeAnimalButton);
        return Optional.of(buttons.get(code));
    }

    public Optional<Controller> getController(String code) {
        HashMap<String, Controller> buttons = new HashMap<>();
        buttons.put(Button.CALL_VOLUNTEER.getCode(), callVolunteerButton);
        buttons.put(Button.CAT_SHELTER.getCode(), catShelterButton);
        buttons.put(Button.DOG_SHELTER.getCode(), dogShelterButton);
        buttons.put(Button.CYNOLOGIST.getCode(), cynologistButton);
        buttons.put(Button.DISABLED_ANIMAL.getCode(), disabledAnimalButton);
        buttons.put(Button.DOCUMENTS.getCode(), documentsButton);
        buttons.put(Button.DRIVER_PERMIT.getCode(), driverPermitButton);
        buttons.put(Button.LEAVE_CONTACT_INFO.getCode(), leaveContactInfoButton);
        buttons.put(Button.MAP.getCode(), mapButton);
        buttons.put(Button.REPORT.getCode(), reportButton);
        buttons.put(Button.RULES.getCode(), rulesButton);
        buttons.put(Button.SHELTER_INFO.getCode(), shelterInfoButton);
        buttons.put(Button.TAKE_ANIMAL.getCode(), takeAnimalButton);
        return Optional.ofNullable(buttons.get(code));
    }

    public Controller getStatus(String code) {
        HashMap<String, Controller> statuses = new HashMap<>();
        statuses.put(Status.NONE.getCode(), noneStatus);
        statuses.put(Status.CONTACT_INFO.getCode(), contactInfoStatus);
        statuses.put(Status.REPORT_PHOTO.getCode(), reportPhotoStatus);
        statuses.put(Status.REPORT_TEXT.getCode(), reportTextStatus);
        return statuses.get(code);
    }

    public Optional<Controller> getCommandByValue(String code) {
        HashMap<String, Controller> commands = new HashMap<>();
        commands.put(Command.START.getValue(), startCommand);
        commands.put(Command.FINISH.getValue(), finishCommand);
        return Optional.ofNullable(commands.get(code));
    }
}
