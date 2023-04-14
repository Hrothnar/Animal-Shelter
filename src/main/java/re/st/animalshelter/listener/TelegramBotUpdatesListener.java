package re.st.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import re.st.animalshelter.entity.NotificationTask;
import re.st.animalshelter.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Pattern pattern = Pattern.compile("(\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{1,2})\\s+([А-я\\d\\s,.?!:]+)");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final NotificationTaskService notificationTaskService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskService notificationTaskService) {
        this.telegramBot = telegramBot;
        this.notificationTaskService = notificationTaskService;
    }

    // обработка запросов бота и их логирование
    @Override
    public int process(List<Update> updates) {
        try {
            updates.stream().filter(update -> Objects.nonNull(update.message())).forEach(update -> {

                logger.info("Handles update: {}", update);

                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();

                if ("/start".equals(text)) {
                    SendMessage sendMessage = new SendMessage(chatId, "Привет! Я помогу тебе запланировать задачу, отправь её в формате 12.03.2022");
                    InlineKeyboardButton button1 = new InlineKeyboardButton("Кнопка 1");
                    button1.callbackData("Кнопка 1");
                    InlineKeyboardButton button2 = new InlineKeyboardButton("Кнопка 2");
                    button2.callbackData("Кнопка 2");
                    Keyboard keyboard = new InlineKeyboardMarkup(button1, button2);
                    sendMessage.replyMarkup(keyboard);
                    telegramBot.execute(sendMessage);
                    if (update.callbackQuery() != null) {
                        CallbackQuery callbackQuery = update.callbackQuery();
                        String data = callbackQuery.data();
                        switch (data) {
                            case "Кнопка 1":
                                sendMessage(chatId, "Нажата кнопка 1");
                                break;
                            case "Кнопка 2":
                                sendMessage(chatId, "Нажата кнопка 2");
                                break;
                        }
                    }


//                    sendMessage(chatId, "Привет! Я помогу тебе запланировать задачу, отправь её в формате 12.03.2022");
//                    try {
//                        byte[] photo = Files.readAllBytes(Paths.get(TelegramBotUpdatesListener.class.getResource("/photo/Dragon.jpeg").toURI()));
//                        SendPhoto sendPhoto = new SendPhoto(chatId, photo);
//                        sendPhoto.caption("Привет! Я помогу тебе запланировать задачу, отправь её в формате 12.03.2022");
//                        telegramBot.execute(sendPhoto);
//                    } catch (IOException | URISyntaxException e) {
//                        throw new RuntimeException(e);
//                    }

                } else if (text != null) {
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        LocalDateTime dateTime = parse(matcher.group(1));
                        if (Objects.isNull(dateTime)) {
                            sendMessage(chatId, "Некорректный формат даты или времени");
                        } else { //если использовать цикл можно применить continue
                            String text1 = matcher.group(2);
                            NotificationTask notificationTask = new NotificationTask();
                            notificationTask.setChatId(chatId);
                            notificationTask.setMessage(text1);
                            notificationTask.setNotificationDateTime(dateTime);
                            notificationTaskService.save(notificationTask);
                            sendMessage(chatId, "Задача занесена в таблицу");
                        }
                    } else {
                        sendMessage(chatId, "Некорректный формат");
                    }
                } else if (message.photo() != null) {
                    PhotoSize photoSize = message.photo()[message.photo().length - 1];
                    GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));
                    if (getFileResponse.isOk()) {
                        try {
                            byte[] fileContent = telegramBot.getFileContent(getFileResponse.file());
                            String extension = StringUtils.getFilenameExtension(getFileResponse.file().filePath());
                            Files.write(Paths.get(UUID.randomUUID().toString() + "." + extension), fileContent);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    //привязывание объекта TelegramBotUpdatesListener к боту
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private void sendMessage(Long id, String message) {
        SendMessage sendMessage = new SendMessage(id, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error while sending message: {}", sendResponse.description());
        }
    }

    @Nullable
    private LocalDateTime parse(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            logger.error(e.getMessage());
            return null;
        }
    }


}
