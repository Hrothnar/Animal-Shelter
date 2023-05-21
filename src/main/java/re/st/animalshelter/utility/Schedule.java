package re.st.animalshelter.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import re.st.animalshelter.service.ActionService;

import java.time.LocalDateTime;

@EnableScheduling
@Component
public class Schedule {
    private final ActionService actionService;

    @Autowired
    public Schedule(ActionService actionService) {
        this.actionService = actionService;
    }

    @Scheduled(cron = "0 0 0 * 1-12 *")
    public void cleanActions() {
        actionService.getAllActions().stream()
                .filter(action -> action.getLastUpdate().isBefore(LocalDateTime.now().plusDays(30)))
                .forEach(action -> actionService.removeAction(action));
    }
}
