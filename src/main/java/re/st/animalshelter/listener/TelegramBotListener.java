package re.st.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.*;
import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.service.CatService;
import re.st.animalshelter.service.DogService;
import re.st.animalshelter.service.FileService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.utility.AddButtonUtil;
import re.st.animalshelter.utility.AddCommand;
import re.st.animalshelter.model.Flashback;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@EnableScheduling
@Component
public class TelegramBotListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final FileService fileService;
    private final CatService catService;
    private final DogService dogService;
    private final AddCommand addCommand;
    private final AddButtonUtil addButtonUtil;
    private final Map<Integer, LinkedList<Flashback>> memory = new LinkedHashMap<>(10);
    public final static Logger LOGGER = Logger.getLogger(TelegramBotListener.class);

//    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
//    private final Pattern pattern = Pattern.compile("(\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{1,2})\\s+([А-я\\d\\s,.?!:]+)");
//    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Autowired
    public TelegramBotListener(TelegramBot telegramBot,
                               UserService userService,
                               CatService catService,
                               DogService dogService,
                               AddButtonUtil addButtonUtil,
                               AddCommand addCommand,
                               FileService fileService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.catService = catService;
        this.dogService = dogService;
        this.addButtonUtil = addButtonUtil;
        this.addCommand = addCommand;
        this.fileService = fileService;
    }

    @Override // главный метод для обработки запросов
    public int process(List<Update> updateList) {
        for (Update update : updateList) {
            Flashback flashback;
            if (Objects.nonNull(update.message())) { // если получили сообщение
                Message message = update.message();
                int messageId = message.messageId();
                String text = message.text();
                if (Objects.nonNull(text)) { // если у сообщения есть текст
                    if (Objects.equals(Command.START.getText(), text)) { // если равен /start
                        userService.createUser(message); // заносим в БД пользователя

                        userService.updateStatus(message.chat().id(), Status.REPORT_WAS_NOT_SENT);

                        remember(new Flashback(message, Dialogue.START, Shelter.NONE, Extension.MD));
                        sendNewTextResponse(message, Dialogue.START, Shelter.NONE, Extension.MD);
                    } else {
                        checkStatus(message, Dialogue.NONE, Shelter.NONE, Extension.MD);
                    }
                } else if (Objects.nonNull(message.photo())) { // если есть фото
                    checkStatus(message, Dialogue.NONE, Shelter.NONE, Extension.MD);
                } else if (Objects.nonNull(message.document())) { // если есть документ

                } else { // если тип message не может быть обработан
                    LOGGER.error("Необрабатываемый запрос");
                    //TODO не забыть добавить ещё логики
                }
            } else if (Objects.nonNull(update.callbackQuery())) { // если получили callbackQuery (пользователь нажал кнопку)
                Message message = update.callbackQuery().message(); // получаем message callbackQuery
                int messageId = message.messageId();
                Button button = Button.getAsEnum(update.callbackQuery().data()); // получаем enum на основе call back
                Shelter shelter;
                switch (button) {
                    case BACK:
                        rightResponse(messageId);
                        break;
                    case DOG_SHELTER:
                        remember(new Flashback(message, Dialogue.MENU, Shelter.DOG, Extension.MD));
                        sendEditedTextResponse(message, Dialogue.MENU, Shelter.DOG, Extension.MD);
                        break;
                    case CAT_SHELTER:
                        remember(new Flashback(message, Dialogue.MENU, Shelter.CAT, Extension.MD));
                        sendEditedTextResponse(message, Dialogue.MENU, Shelter.CAT, Extension.MD);
                        break;
                    case SHELTER_INFO:
                        shelter = getCurrentShelter(messageId);
                        remember(new Flashback(message, Dialogue.INFO, shelter, Extension.MD));
                        sendEditedTextResponse(message, Dialogue.INFO, shelter, Extension.MD);
                        break;
                    case LOOK_AT_THE_MAP:
                        shelter = getCurrentShelter(messageId);
                        sendNewPhotoResponse(message, Dialogue.MAP, shelter, Extension.MD, Extension.PNG);
                        break;
                    case LEAVE_CONTACT_INFORMATION:
//                        flashback = new Flashback(message, Dialogue.CONTACT_INFORMATION, Shelter.NONE, Extension.MD);
                        break;
                    case SEND_REPORT:
                        checkStatus(message, Dialogue.NONE, Shelter.NONE, Extension.MD);


                        break;
                    default:
                        LOGGER.error("Не существующая кнопка");
                        //TODO чё-то добавить?
                }
            } else { // если тип update не может быть обработан
                LOGGER.error("Необрабатываемый запрос");
                //TODO не забыть добавить ещё логики
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;  // возвращаем флаг полной обработки updates
    }

    private void checkStatus(Message message, Dialogue dialogue, Shelter shelter, Extension extension) {
        long chatId = message.chat().id();
        String text = message.text();
        PhotoSize[] photoSizes = message.photo();
        Status status = userService.getStatus(chatId);
        switch (status) {
            case REPORT_WAS_NOT_SENT:
                userService.updateStatus(chatId, Status.WAIT_FOR_REPORT_TEXT);
                sendNewTextResponse(message, Dialogue.REPORT_TEXT, shelter, extension);
                break;
            case WAIT_FOR_REPORT_TEXT:
                if (Objects.nonNull(text)) {
                    userService.updateStatus(chatId, Status.WAIT_FOR_REPORT_PHOTO);
                    sendNewTextResponse(message, Dialogue.REPORT_PHOTO, shelter, extension);
                    fileService.saveText(chatId, text);
                }
                break;
            case WAIT_FOR_REPORT_PHOTO:
                if (Objects.nonNull(photoSizes)) {
                    userService.updateStatus(chatId, Status.REPORT_WAS_SENT);
                    sendNewTextResponse(message, Dialogue.REPORTED, shelter, extension);
                    fileService.savePhoto(chatId, photoSizes);
                }
            case REPORT_WAS_SENT:
//                userService.updateStatus(chatId, Status.WAIT_FOR_REPORT_TEXT);
//                sendNewTextResponse(message, Dialogue.REPORT_TEXT, shelter, extension);
                break;
            case WAIT_FOR_CONTACT_INFORMATION:
                if (Objects.nonNull(text)) {

                }
                break;
            case NONE:
                break;
        }
    }

    private void rightResponse(int messageId) {
        Flashback flashback = previousFlashback(messageId);
        if (flashback.getMessage().messageId() == messageId - 1) {
            sendEditedFirstResponse(flashback.getMessage(), flashback.getDialogue(), flashback.getShelter(), flashback.getTextExtension());
        } else {
            sendEditedTextResponse(flashback.getMessage(), flashback.getDialogue(), flashback.getShelter(), flashback.getTextExtension());
        }
    }


//    @Override // главный метод для обработки запросов
//    public int process(List<Update> updates) {
//        try {
//            updates.stream().filter(update -> Objects.nonNull(update.message())).forEach(update -> {
//                logger.info("Handles update: {}", update);
//
//                Message message = update.message();
//                Long chatId = message.chat().id();
//                String text = message.text();
//
//                if ("/start".equals(text)) {
//                    SendMessage sendMessage = new SendMessage(chatId, "Привет! Я помогу тебе запланировать задачу, отправь её в формате 12.03.2022");

//                    InlineKeyboardButton button1 = new InlineKeyboardButton("Кнопка 1");
//                    button1.callbackData("Кнопка 1");
//                    InlineKeyboardButton button2 = new InlineKeyboardButton("Кнопка 2");
//                    button2.callbackData("Кнопка 2");

//                    Keyboard keyboard = new InlineKeyboardMarkup(button1, button2);

//                    sendMessage.replyMarkup(keyboard);
//                    telegramBot.execute(sendMessage);

//                    if (update.callbackQuery() != null) {
//                        CallbackQuery callbackQuery = update.callbackQuery();
//                        String data = callbackQuery.data();
//                        switch (data) {
//                            case "Кнопка 1":
//                                sendMessage(chatId, "Нажата кнопка 1");
//                                break;
//                            case "Кнопка 2":
//                                sendMessage(chatId, "Нажата кнопка 2");
//                                break;
//                        }
//                    }
//
//
//                    sendMessage(chatId, "Привет! Я помогу тебе запланировать задачу, отправь её в формате 12.03.2022");
//                    try {
//                        byte[] photo = Files.readAllBytes(Paths.get(TelegramBotUpdatesListener.class.getResource("/photo/Dragon.jpeg").toURI()));
//                        SendPhoto sendPhoto = new SendPhoto(chatId, photo);
//                        sendPhoto.caption("Привет! Я помогу тебе запланировать задачу, отправь её в формате 12.03.2022");
//                        telegramBot.execute(sendPhoto);
//                    } catch (IOException | URISyntaxException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                } else if (text != null) {
//                    Matcher matcher = pattern.matcher(text);
//                    if (matcher.find()) {
//                        LocalDateTime dateTime = parse(matcher.group(1));
//                        if (Objects.isNull(dateTime)) {
//                            sendMessage(chatId, "Некорректный формат даты или времени");
//                        } else { //если использовать цикл можно применить continue
//                            String text1 = matcher.group(2);
//                            NotificationTask notificationTask = new NotificationTask();
//                            notificationTask.setChatId(chatId);
//                            notificationTask.setMessage(text1);
//                            notificationTask.setNotificationDateTime(dateTime);
//                            notificationTaskService.save(notificationTask);
//                            sendMessage(chatId, "Задача занесена в таблицу");
//                        }
//                    } else {
//                        sendMessage(chatId, "Некорректный формат");
//                    }
//                } else if (message.photo() != null) {
//                    PhotoSize photoSize = message.photo()[message.photo().length - 1];
//                    GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));
//                    if (getFileResponse.isOk()) {
//                        try {
//                            byte[] fileContent = telegramBot.getFileContent(getFileResponse.file());
//                            String extension = StringUtils.getFilenameExtension(getFileResponse.file().filePath());
//                            Files.write(Paths.get(UUID.randomUUID().toString() + "." + extension), fileContent);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return UpdatesListener.CONFIRMED_UPDATES_ALL;
//    }
//

    private Shelter getCurrentShelter(int messageId) { // получение текущего приюта
        return memory.get(messageId).peek().getShelter();
    }

    private void remember(Flashback flashback) { // добавление в ячейку памяти новой записи
        int messageId = flashback.getMessage().messageId();
        LinkedList<Flashback> flashbacks;
        if (memory.containsKey(messageId)) {
            flashbacks = memory.get(messageId);
        } else {
            messageId++;
            flashbacks = new LinkedList<>();
        }
        flashbacks.push(flashback);
        memory.put(messageId, flashbacks);
    }

    private Flashback previousFlashback(int messageId) { // получение предыдущей записи в ячейке памяти (вызывается кнопкой BACK)
        LinkedList<Flashback> flashbacks = memory.get(messageId);
        flashbacks.pop();
        return flashbacks.peek();
    }

    private void sendNewTextResponse(Message message, Dialogue dialogue, Shelter shelter, Extension extension) {
        long chatId = message.chat().id();
        boolean isOwner = userService.isOwner(chatId);
        String text = dialogue.getText(shelter, isOwner, extension);
        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(shelter, isOwner, dialogue);
        SendMessage response = new SendMessage(chatId, text).replyMarkup(keyboard);
        if (!telegramBot.execute(response).isOk()) {
            LOGGER.error("Ответ не был отправлен", new RuntimeException());
            //TODO своё исключение
        }
    }

    private void sendEditedFirstResponse(Message message, Dialogue dialogue, Shelter shelter, Extension extension) {
        long chatId = message.chat().id();
        boolean isOwner = userService.isOwner(chatId);
        int messageId = message.messageId();
        String text = dialogue.getText(shelter, isOwner, extension);
        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(shelter, isOwner, dialogue);
        EditMessageText response = new EditMessageText(chatId, ++messageId, text).replyMarkup(keyboard);
        if (!telegramBot.execute(response).isOk()) {
            LOGGER.error("Ответ не был отправлен", new RuntimeException());
            //TODO своё исключение
        }
    }

    private void sendEditedTextResponse(Message message, Dialogue dialogue, Shelter shelter, Extension extension) {
        long chatId = message.chat().id();
        boolean isOwner = userService.isOwner(chatId);
        int messageId = message.messageId();
        String text = dialogue.getText(shelter, isOwner, extension);
        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(shelter, isOwner, dialogue);
        EditMessageText response = new EditMessageText(chatId, messageId, text).replyMarkup(keyboard);
        if (!telegramBot.execute(response).isOk()) {
            LOGGER.error("Ответ не был отправлен", new RuntimeException());
            //TODO своё исключение
        }
    }

    private void sendNewPhotoResponse(Message message, Dialogue dialogue, Shelter shelter, Extension textExtension, Extension photoExtension) {
        long chatId = message.chat().id();
        boolean isOwner = userService.isOwner(chatId);
        String text = dialogue.getText(shelter, isOwner, textExtension);
        File photo = dialogue.getPhoto(shelter, isOwner, photoExtension);
        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(shelter, isOwner, dialogue);
        SendPhoto response = new SendPhoto(chatId, photo).caption(text).replyMarkup(keyboard);
        if (!telegramBot.execute(response).isOk()) {
            LOGGER.error("Ответ не был отправлен", new RuntimeException());
            //TODO своё исключение
        }
    }


//        Message message = flashback.getMessage();
//        long chatId = message.chat().id();
//        Dialogue dialogue = flashback.getDialogue();
//        boolean isOwner = userService.isOwner(chatId);
//        Shelter shelter = flashback.getShelter();
//        Extension textExtension = flashback.getTextExtension();
//        boolean initialMessage = flashback.isInitialMessage();
//        String text = dialogue.getText(shelter, isOwner, textExtension); // получение заготовленного сообщения
//        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(dialogue, isOwner, shelter); // получение заготовленных кнопок
//        BaseResponse response;
//        if (messageId == -1) {
//            if (flashback instanceof Photo) {
//                Extension photoExtension = ((Photo) flashback).getPhotoExtension();
//                File photo = dialogue.getPhoto(shelter, isOwner, photoExtension);
//                response = telegramBot.execute(new SendPhoto(chatId, photo).caption(text).replyMarkup(keyboard));
//            } else {
//                response = telegramBot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
//            }
//        } else {
//            if (flashback instanceof Photo) {
//                Extension photoExtension = ((Photo) flashback).getPhotoExtension();
//                File photo = dialogue.getPhoto(shelter, isOwner, photoExtension);
//                if (((Photo) flashback).isEditedMessageMedia()) {
//                    response = telegramBot.execute(new EditMessageMedia(chatId, messageId, new InputMediaPhoto(photo)).replyMarkup(keyboard));
//                } else {
//                    response = telegramBot.execute(new SendPhoto(chatId, photo).caption(text).replyMarkup(keyboard));
//                }
//            } else {
//                response = telegramBot.execute(new EditMessageText(chatId, messageId, text).replyMarkup(keyboard));
//            }
//        }
//        if (!response.isOk()) {
//            LOGGER.error("Ответ не был отправлен", new RuntimeException());
//            System.out.println(response.description());
//            //TODO своё исключение
//        }


    @Scheduled(cron = "0 0 0 * * 0") // очистка memory пользователя, согласно условию
    private void cleanMemory() {
        if (memory.size() > 1) {
            memory.values().removeIf(e -> e.getLast().getCreationTime().isBefore(LocalDateTime.now().minusWeeks(2)));
        }
    }

    @PostConstruct // связывание объекта TelegramBotUpdatesListener и бота
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

//    @Nullable
//    private LocalDateTime parse(String dateTime) {
//        try {
//            return LocalDateTime.parse(dateTime, dateTimeFormatter);
//        } catch (DateTimeParseException e) {
//            logger.error(e.getMessage());
//            return null;
//        }
//    }
}
