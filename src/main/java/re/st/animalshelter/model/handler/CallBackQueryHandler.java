package re.st.animalshelter.model.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.service.InformationService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.utility.AddButtonUtil;

@Component
public class CallBackQueryHandler {
    private final InformationService informationService;
    private final AddButtonUtil addButtonUtil;
    private final UserService userService;
    private final TelegramBot telegramBot;

    public CallBackQueryHandler(InformationService informationService, AddButtonUtil addButtonUtil, UserService userService, TelegramBot telegramBot) {
        this.informationService = informationService;
        this.addButtonUtil = addButtonUtil;
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

//    public void sendEditedTextResponse(Action action) {
//        long chatId = action.getChatId();
//        int messageId = action.getMessageId();
//        boolean owner = userService.isOwner(chatId);
//        String text = informationService.getText(action, owner);
//        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(action, owner);
//        EditMessageText response = new EditMessageText(chatId, messageId, text).replyMarkup(keyboard);
//        BaseResponse execute = telegramBot.execute(response);
//        if (!execute.isOk()) {
//            System.out.println(execute.description());
//        }
//    }

//    public void sendEditedTextResponse(long chatId, int messageId, Shelter shelter) {
//        User user = userService.getUser(chatId);
//        Dialog dialog = dialogService.getDialog(messageId);
//        boolean isOwner = user.isOwner();
//        send(chatId, messageId, dialog, isOwner, shelter);
//    }
//
//    private void send(Dialog dialog) {
//        String text1 = informationService.getText(dialog);
//
//
//        Stage stage = dialog.getCurrentStage();
//        String text = stage.getText(shelter, isOwner);
//        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(shelter, isOwner, stage);
//        EditMessageText response = new EditMessageText(chatId, messageId, text).replyMarkup(keyboard);
//        BaseResponse execute = telegramBot.execute(response);
//        if (!execute.isOk()) {
//            System.out.println(execute.description());
//            LOGGER.error("Ответ не был отправлен", new RuntimeException());
//            //TODO своё исключение
//        }
//
//        public void sendEditedTextResponse(long chatId, int messageId) {
//            User user = userService.getUser(chatId);
//            Dialog dialog = dialogService.getDialog(messageId);
//            boolean isOwner = user.isOwner();
//            Shelter shelter = dialog.getShelter();
//            send(chatId, messageId, dialog, isOwner, shelter);
//        }
//
//        public void sendEditedTextResponse(long chatId, int messageId, Shelter shelter) {
//            User user = userService.getUser(chatId);
//            Dialog dialog = dialogService.getDialog(messageId);
//            boolean isOwner = user.isOwner();
//            send(chatId, messageId, dialog, isOwner, shelter);
//        }
//
//        private void send(long chatId, int messageId, Dialog dialog, boolean isOwner, Shelter shelter) {
//            Stage stage = dialog.getCurrentStage();
//            String text = stage.getText(shelter, isOwner);
//            InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(shelter, isOwner, stage);
//            EditMessageText response = new EditMessageText(chatId, messageId, text).replyMarkup(keyboard);
//            BaseResponse execute = telegramBot.execute(response);
//            if (!execute.isOk()) {
//                System.out.println(execute.description());
//                LOGGER.error("Ответ не был отправлен", new RuntimeException());
//                //TODO своё исключение
//            }
//        }
//
//    }
//
//
//    public void sendNewPhotoResponse(long chatId, int messageId, Stage stage) {
//        User user = userService.getUser(chatId);
//        Dialog dialog = dialogService.getDialog(messageId);
//        boolean isOwner = user.isOwner();
//        Shelter shelter = dialog.getShelter();
//        String text = stage.getText(shelter, isOwner);
//        File photo = stage.getPhoto(shelter, isOwner);
//        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(shelter, isOwner, stage);
//        SendPhoto response = new SendPhoto(chatId, photo).caption(text).replyMarkup(keyboard);
//        if (!telegramBot.execute(response).isOk()) {
//            LOGGER.error("Ответ не был отправлен", new RuntimeException());
//            //TODO своё исключение
//        }
//    }

}
