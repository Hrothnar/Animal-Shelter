package re.st.animalshelter.model.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.Volunteer;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.VolunteerService;
import re.st.animalshelter.utility.ButtonUtil;

import java.util.LinkedList;
import java.util.Optional;

@Component
public class TextResponse {
    private final StorageService storageService;
    private final VolunteerService volunteerService;
    private final ButtonUtil buttonUtil;
    private final TelegramBot telegramBot;

    @Autowired
    public TextResponse(StorageService storageService, VolunteerService volunteerService, ButtonUtil buttonUtil, TelegramBot telegramBot) {
        this.storageService = storageService;
        this.volunteerService = volunteerService;
        this.buttonUtil = buttonUtil;
        this.telegramBot = telegramBot;
    }

    public void sendNewTextResponse(ActionDTO actionDTO) {
        long chatId = actionDTO.getChatId();
        String text = storageService.getText(actionDTO);
        InlineKeyboardMarkup keyboard = buttonUtil.getKeyboard(actionDTO);
        SendMessage response = new SendMessage(chatId, text).replyMarkup(keyboard);
        BaseResponse execute = telegramBot.execute(response);
        if (!execute.isOk()) {
            System.out.println(execute.description());
        }
    }

    public void sendNewTextResponseByStatus(long chatId, Status status) {
        if (status != Status.NONE) {
            String text = storageService.getText(status);
            SendMessage response = new SendMessage(chatId, text);
            BaseResponse execute = telegramBot.execute(response);
            if (!execute.isOk()) {
                System.out.println(execute.description());
            }
        }
    }

    public Status connectVolunteerAndUser(User user, String text) {
        long userChatId = user.getChatId();
        LinkedList<Volunteer> volunteers = volunteerService.getAllVolunteers();
        Optional<Volunteer> optional = volunteers.stream()
                .filter(volunteer -> volunteer.getUserChatId() == userChatId )
                .findFirst();
        if (optional.isPresent()) {
            Volunteer volunteer = optional.get();
            long volunteerChatId = volunteer.getChatId();
            SendMessage response = new SendMessage(volunteerChatId, text);
            SendResponse execute = telegramBot.execute(response);
            if (!execute.isOk()) {
                System.out.println(execute.description());
            }
        }
        return Status.NONE;
    }
}
