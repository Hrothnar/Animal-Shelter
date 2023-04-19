package re.st.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Command;
import re.st.animalshelter.enumeration.Dialogue;
import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.service.CatService;
import re.st.animalshelter.service.DogService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.utility.AddButtonUtil;
import re.st.animalshelter.utility.AddCommand;
import re.st.animalshelter.utility.Flashback;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@EnableScheduling
@Component
public class TelegramBotListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final CatService catService;
    private final DogService dogService;
    private final AddCommand addCommand;
    private final AddButtonUtil addButtonUtil;
    private Map<Integer, LinkedList<Flashback>> memory = new LinkedHashMap<>(10);
    public final static Logger LOGGER = Logger.getLogger(TelegramBotListener.class);


//        private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
//    private final Pattern pattern = Pattern.compile("(\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{1,2})\\s+([А-я\\d\\s,.?!:]+)");
//    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//    private final NotificationTaskService notificationTaskService;

    @Autowired
    public TelegramBotListener(TelegramBot telegramBot,
                               UserService userService,
                               CatService catService,
                               DogService dogService,
                               AddButtonUtil addButtonUtil,
                               AddCommand addCommand) {
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.catService = catService;
        this.dogService = dogService;
        this.addButtonUtil = addButtonUtil;
        this.addCommand = addCommand;
    }

    @Override // главный метод для обработки запросов
    public int process(List<Update> updateList) {
        for (Update update : updateList) {
            if (Objects.nonNull(update.message())) { // если получили сообщение
                Flashback flashback;
                Message message = update.message();
                long chatId = message.chat().id();
                int messageId = message.messageId();
                String text = message.text();
                if (Objects.nonNull(text)) { // если у сообщения есть текст
                    if (Objects.equals(Command.START.getText(), text)) { // если равен /start
                        if (!userService.isExist(chatId)) {
                            userService.createUser(message); // заносим в БД пользователя, если его ещё там нет
                        }
                        flashback = new Flashback(message, Dialogue.START, Button.START, Shelter.NONE);
                        createMemoryCell(flashback, ++messageId);
                        sendResponse(flashback);
                    }
                } else if (Objects.nonNull(message.photo())) { // если есть фото

                } else if (Objects.nonNull(message.document())) { // если есть документ

                } else { // если тип message не может быть обработан
                    LOGGER.error("Необрабатываемый запрос");
                    //TODO не забыть добавить ещё логики
                }
            } else if (Objects.nonNull(update.callbackQuery())) { // если получили callbackQuery (пользователь нажал кнопку)
                CallbackQuery callbackQuery = update.callbackQuery();
                Message message = callbackQuery.message(); // получаем message callbackQuery
                int messageId = callbackQuery.message().messageId();
                Button asEnum = Button.getAsEnum(callbackQuery.data()); // получаем enum на основе call back
                Flashback flashback;
                switch (asEnum) {
                    case BACK:
                        flashback = previousFlashback(messageId);
                        sendEditResponse(flashback, messageId);
                        break;
                    case DOG_SHELTER:
                        flashback = new Flashback(message, Dialogue.MENU, Button.DOG_SHELTER, Shelter.DOG);
                        remember(flashback);
                        sendEditResponse(flashback, messageId);
                        break;
                    case CAT_SHELTER:
                        flashback = new Flashback(message, Dialogue.MENU, Button.CAT_SHELTER, Shelter.CAT);
                        remember(flashback);
                        sendEditResponse(flashback, messageId);
                        break;
                    case SHELTER_INFO:
                        flashback = new Flashback(message, Dialogue.INFO, Button.SHELTER_INFO, getCurrentShelter(messageId));
                        remember(flashback);
                        sendEditResponse(flashback, messageId);
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
        LinkedList<Flashback> flashbacks = memory.get(messageId);
        flashbacks.push(flashback);
        memory.put(messageId, flashbacks);
    }

    private void createMemoryCell(Flashback flashback, int messageId) { // создание новой ячейки памяти (по команде /start)
        LinkedList<Flashback> stack = new LinkedList<>();
        stack.push(flashback);
        memory.put(messageId, stack);
    }

    private Flashback previousFlashback(int messageId) { // получение предыдущей записи в ячейке памяти (вызывается кнопкой BACK)
        LinkedList<Flashback> flashbacks = memory.get(messageId);
        flashbacks.pop();
        return flashbacks.peek();
    }

    private void sendResponse(Flashback flashback) {
        long chatId = flashback.getMessage().chat().id();
        Dialogue dialogue = flashback.getDialogue();
        boolean isOwner = userService.isOwner(chatId);
        Shelter shelter = flashback.getShelter();
        String text = dialogue.getText(shelter, isOwner); // получение заготовленного сообщения
        SendMessage sendMessage = new SendMessage(chatId, text); // присвоение сообщения ответу
        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(dialogue, isOwner, shelter); // получение заготовленных кнопок
        sendMessage.replyMarkup(keyboard); // присвоение кнопок ответу
        if (!telegramBot.execute(sendMessage).isOk()) {
            LOGGER.error("Ответ не был отправлен", new RuntimeException());
            //TODO своё исключение
        }
    }

    private void sendEditResponse(Flashback flashback, int messageId) {
        Message message = flashback.getMessage();
        long chatId = message.chat().id();
        Dialogue dialogue = flashback.getDialogue();
        boolean isOwner = userService.isOwner(chatId);
        Shelter shelter = flashback.getShelter();
        String text = dialogue.getText(shelter, isOwner); // получение заготовленного сообщения
        EditMessageText editMessage = new EditMessageText(chatId, messageId, text); // присвоение нового сообщения диалоговому окну
        InlineKeyboardMarkup keyboard = addButtonUtil.getKeyboard(dialogue, isOwner, shelter); // получение заготовленных кнопок
        editMessage.replyMarkup(keyboard); // присвоение новых кнопок диалоговому окну
        if (!telegramBot.execute(editMessage).isOk()) {
            LOGGER.error("Ответ не был отправлен", new RuntimeException());
            //TODO своё исключение
        }
    }

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

//    private void rememberCommand(Message message, Command command) {
//        long chatId = message.chat().id();
//        switch (command) {
//            case START:
//                LocalDateTime date = LocalDateTime.now();
//                Dialogue dialogue = Dialogue.START;
//                Shelter shelter = Shelter.NONE;
//                CallbackQuery callbackQuery = new CallbackQuery();
//                Flashback flashback = new Flashback(null, command, date, dialogue, shelter, callbackQuery);
//                Stack<Flashback> flashbacks = new Stack<>();
//                flashbacks.push(flashback);
//                stacks.put(chatId, flashbacks);
//                break;
//            default:
//                //TODO add my own exception
//        }
//    }

//    private void rememberChosenShelter(CallbackQuery callbackQuery, Shelter shelter) {
//        long chatId = callbackQuery.message().chat().id();
//        Stack<Flashback> flashbacks = stacks.get(chatId);
//        Button button = Button.WRONG;
//        if (Objects.equals(shelter, Shelter.CAT)) {
//            button = Button.CAT_SHELTER;
//        } else if (Objects.equals(shelter, Shelter.DOG)) {
//            button = Button.DOG_SHELTER;
//        }
//        LocalDateTime date = LocalDateTime.now();
//        Dialogue dialogue = Dialogue.START;
//        Flashback flashback = new Flashback(button, null, date, dialogue, shelter, callbackQuery);
//        flashbacks.push(flashback);
//        stacks.put(chatId, flashbacks);
//    }
//
//
//    private Flashback getLastButtonFlashback(long chatId) {
//        Stack<Flashback> flashbacks = stacks.get(chatId);
//        for (Flashback flashback : flashbacks) {
//            if (Objects.nonNull(flashback.getButton())) {
//                return flashback;
//            }
//        }
//        return null;
//    }
//
//    private Flashback getLastCommandFlashback(long chatId) {
//        Stack<Flashback> flashbacks = stacks.get(chatId);
//        for (Flashback flashback : flashbacks) {
//            if (Objects.nonNull(flashback.getCommand())) {
//                return flashback;
//            }
//        }
//        return null;
//    }
//
//
//    private void rememberButton(CallbackQuery callbackQuery, Dialogue dialogue, Button button) {
//        long chatId = callbackQuery.message().chat().id();
//        Stack<Flashback> flashbacks = stacks.get(chatId);
//        Shelter shelter = flashbacks.peek().getShelter();
//        LocalDateTime date = LocalDateTime.now();
//        Flashback flashback = new Flashback(button, null, date, dialogue, shelter, callbackQuery);
//        flashbacks.push(flashback);
//        stacks.put(chatId, flashbacks);
//    }

//    private Button getLastButton(CallbackQuery callbackQuery) {
//        int messageId = callbackQuery.message().messageId();
//        if (stacks.containsKey(messageId)) {
//            return stacks.get(messageId).getStack().pop();
//        }
//        return Button.WRONG;
//    }

//
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
