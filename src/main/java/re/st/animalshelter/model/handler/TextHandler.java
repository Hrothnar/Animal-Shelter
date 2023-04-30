package re.st.animalshelter.model.handler;

import org.springframework.stereotype.Component;

@Component
public class TextHandler {


    //    public void sendStartResponse(long chatId) {
//        User user = userService.getUser(chatId);
//        boolean isOwner = user.isOwner();
//        String text = Stage.START.getText(Shelter.NONE, isOwner);
//        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(Shelter.NONE, isOwner, Stage.START);
//        SendMessage response = new SendMessage(chatId, text).replyMarkup(keyboard);
//        if (!telegramBot.execute(response).isOk()) {
//            LOGGER.error("Ответ не был отправлен", new RuntimeException());
//            //TODO своё исключение
//        }
//    }
//
//    public void sendNewTextResponse(long chatId, Stage stage, Shelter shelter) {
//        User user = userService.getUser(chatId);
//        boolean isOwner = user.isOwner();
//        String text = stage.getText(shelter, isOwner);
//        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(shelter, isOwner, stage);
//        SendMessage response = new SendMessage(chatId, text).replyMarkup(keyboard);
//        SendResponse execute = telegramBot.execute(response);
//        if (!execute.isOk()) {
//            System.out.println(execute.description());
//            LOGGER.error("Ответ не был отправлен", new RuntimeException());
//            //TODO своё исключение
//        }
//    }

}

