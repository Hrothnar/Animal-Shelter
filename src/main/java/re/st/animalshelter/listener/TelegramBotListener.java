package re.st.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import re.st.animalshelter.model.Distributor;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.utility.AddCommandUtil;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@EnableScheduling
@Component
public class TelegramBotListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Distributor distributor;
    public final static Logger LOGGER = Logger.getLogger(TelegramBotListener.class);

//    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
//    private final Pattern pattern = Pattern.compile("(\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{1,2})\\s+([А-я\\d\\s,.?!:]+)");
//    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Autowired
    public TelegramBotListener(TelegramBot telegramBot, Distributor distributor) {
        this.telegramBot = telegramBot;
        this.distributor = distributor;
    }

    @Override
    public int process(List<Update> updateList) {
        for (Update update : updateList) {
            if (Objects.nonNull(update.message())) {
                Message message = update.message();
                if (Objects.nonNull(message.text())) {

                    distributor.processTextMessage(message);
                } else if (Objects.nonNull(message.photo())) {
                    distributor.processPhotoMessage(message);
                } else if (Objects.nonNull(message.document())) {
                    distributor.processDocumentMessage(message);
                } else {
                    LOGGER.error("Необрабатываемый запрос"); //TODO не забыть добавить ещё логики
                }
            } else if (Objects.nonNull(update.callbackQuery())) {
                distributor.processCallBackQuery(update.callbackQuery());
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
//
//    private void processPhoto(Message message) {
//        long chatId = message.chat().id();
//        PhotoSize[] photos = message.photo();
//        User user = userService.getUser(chatId);
//        Stage stage = user.getPhase();
//        switch (stage) {
//            case REPORT_PHOTO:
//                userService.updatePhase(chatId, Stage.REPORTED);
//                userService.updateReportTime(user);
//                fileService.savePhoto(chatId, photos);
//                sendNewTextResponse(chatId, Stage.REPORTED, Shelter.NONE);
//                break;
//        }
//    }
//
//    private void processCallbackQuery(CallbackQuery callbackQuery) {
//        long chatId = callbackQuery.message().chat().id();
//        int messageId = callbackQuery.message().messageId();
//        Button button = Button.getButton(callbackQuery.data()); // получаем enum на основе call back
//        switch (button) {
//            case BACK:
//                processBack(chatId, messageId);
//                break;
//            case DOG_SHELTER:
//            case CAT_SHELTER:
//                dialogService.attachShelter(messageId, button, Stage.MENU);
//                sendEditedTextResponse(chatId, messageId);
//                break;
//            case LEAVE_CONTACT_INFORMATION:
//            case SEND_REPORT:
//                userService.updatePhase(chatId, button.getStage());
//                sendNewTextResponse(chatId, button.getStage(), Shelter.NONE);
//                break;
//            case LOOK_AT_THE_MAP:
//                sendNewPhotoResponse(chatId, messageId, Stage.MAP);
//                break;
//            case DOCUMENTS_FOR_ANIMAL:
//            case DISABLED_ANIMAL:
//                dialogService.forwardDialog(messageId, button.getStage());
//                sendEditedTextResponse(chatId, messageId, Shelter.NONE);
//                break;
//            case SHELTER_INFO:
//            case DRIVER_PERMIT:
//            case RULES:
//            case TAKE_AN_ANIMAL:
//            case CYNOLOGIST:
//                dialogService.forwardDialog(messageId, button.getStage());
//                sendEditedTextResponse(chatId, messageId);
//                break;
//            default:
//                LOGGER.error("Не существующая кнопка");
//                //TODO чё-то добавить?
//        }
//    }
//
//    private void processBack(long chatId, int messageId) {
//        Dialog dialog = dialogService.getDialog(messageId);
//        Stage currentStage = dialog.getCurrentStage();
//        Stage previousStage = dialog.getPreviousStage();
//        switch (currentStage) {
//            case MENU:
//                dialogService.resetDialog(messageId);
//                sendEditedTextResponse(chatId, messageId);
//                break;
//            case INFO:
//            case ANIMAL:
//                dialogService.backwardDialog(messageId, Stage.START);
//                sendEditedTextResponse(chatId, messageId);
//                break;
//            case CYNOLOGIST:
//            case DISABLED_ANIMAL:
//            case DOCUMENTS:
//            case RULES:
//                dialogService.backwardDialog(messageId, Stage.MENU);
//                sendEditedTextResponse(chatId, messageId);
//                break;
//            case DRIVER_PERMIT:
//                switch (previousStage) {
//                    case INFO:
//                        dialogService.backwardDialog(messageId, Stage.MENU);
//                        sendEditedTextResponse(chatId, messageId);
//                        break;
//                    case DOCUMENTS:
//                        dialogService.backwardDialog(messageId, Stage.ANIMAL);
//                        sendEditedTextResponse(chatId, messageId);
//                        break;
//                }
//                break;
//        }
//    }
//
//    private void processText(Message message) {
//        long chatId = message.chat().id();
//        boolean exist = userService.isExist(chatId);
//        int messageId = message.messageId();
//        String text = message.text();
//        if (Objects.equals(Response.START.getText(), text)) {
//            ++messageId;
//            if (exist) {
//                userService.createDialog(chatId, messageId);
//            } else {
//                userService.createUserAndStartDialog(chatId, messageId, message);
//            }
//            sendStartResponse(chatId);
//        } else {
//            Stage stage = userService.getUser(chatId).getPhase();
//            switch (stage) {
//                case REPORT_TEXT:
//                    userService.updatePhase(chatId, Stage.REPORT_PHOTO);
//                    fileService.saveText(chatId, text);
//                    sendNewTextResponse(chatId, Stage.REPORT_PHOTO, Shelter.NONE);
//                    break;
//                case CONTACT_INFO:
//                    if (text.length() < 15 && text.length() > 6) {
//                        userService.updatePhase(chatId, Stage.CONTACT_INFO_RECEIVED);
//                        userService.updatePhoneNumber(chatId, text);
//                        sendNewTextResponse(chatId, Stage.CONTACT_INFO_RECEIVED, Shelter.NONE);
//                    }
//                    break;
//            }
//        }
//    }

//    private void sendStartResponse(long chatId) {
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
//    private void sendNewTextResponse(long chatId, Stage stage, Shelter shelter) {
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
//
//    private void sendEditedTextResponse(long chatId, int messageId) {
//        User user = userService.getUser(chatId);
//        Dialog dialog = dialogService.getDialog(messageId);
//        boolean isOwner = user.isOwner();
//        Shelter shelter = dialog.getShelter();
//        send(chatId, messageId, dialog, isOwner, shelter);
//    }
//
//    private void sendEditedTextResponse(long chatId, int messageId, Shelter shelter) {
//        User user = userService.getUser(chatId);
//        Dialog dialog = dialogService.getDialog(messageId);
//        boolean isOwner = user.isOwner();
//        send(chatId, messageId, dialog, isOwner, shelter);
//    }
//
//    private void send(long chatId, int messageId, Dialog dialog, boolean isOwner, Shelter shelter) {
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
//    }
//
//
//    private void sendNewPhotoResponse(long chatId, int messageId, Stage stage) {
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

    @PostConstruct // связывание объекта TelegramBotUpdatesListener и бота
    private void init() {
        telegramBot.setUpdatesListener(this);
        new AddCommandUtil().addCommands(telegramBot);
    }
}
