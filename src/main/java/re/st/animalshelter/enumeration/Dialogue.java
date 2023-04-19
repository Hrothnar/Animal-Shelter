package re.st.animalshelter.enumeration;

import re.st.animalshelter.enumeration.animal.Shelter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public enum Dialogue {
    START,
    MENU,
    INFO;

//    DRIVER_PERMIT,
//    COMMUNICATION_DATA,
//    START_MENU,
//    CHOOSE_AN_ANIMAL,
//    CHOOSE_ITS_AGE,
//    RULES,
//    LICENCE,
//    DOCUMENTS,
//    PHOTO_WAITING,
//    TEXT_WAITING,

    private String text; // названия файлов должны соответствовать названиям Enums, с учётом префиксов типа приюта и статуса держателя
    private static final String OWNER_PREFIX = "owner_"; // префикс для метки файлов, текст которых, предназначен для держателей животных
    private static final String USER_PREFIX = "user_"; // префикс для метки файлов, текст которых, предназначен для простых пользователей
    private static final String PATH = "./src/main/resources/text/"; // просто путь...без цели...ъуъ...

    public String getText(Shelter shelter, boolean isOwner) {
        readFromFile(shelter, isOwner);
        return text;
    }

    private void readFromFile(Shelter shelter, boolean isOwner) {
        String fileName = this.toString().toLowerCase();
        try {
            if (Objects.equals(shelter, Shelter.CAT)) {
                if (isOwner) {
                    text = Files.readString(Path.of(PATH, "owner/cat", OWNER_PREFIX + Shelter.CAT.getPrefix() + fileName + ".md"));
                } else {
                    text = Files.readString(Path.of(PATH, "user/cat", USER_PREFIX + Shelter.CAT.getPrefix() + fileName + ".md"));
                }
            } else if (Objects.equals(shelter, Shelter.DOG)) {
                if (isOwner) {
                    text = Files.readString(Path.of(PATH, "owner/dog", OWNER_PREFIX + Shelter.DOG.getPrefix() + fileName + ".md"));
                } else {
                    text = Files.readString(Path.of(PATH, "user/dog", USER_PREFIX + Shelter.DOG.getPrefix() + fileName + ".md"));
                }
            } else if (Objects.equals(shelter, Shelter.NONE)) {
                if (isOwner) {
                    text = Files.readString(Path.of(PATH, "owner", OWNER_PREFIX + Shelter.NONE.getPrefix() + fileName + ".md"));
                } else {
                    text = Files.readString(Path.of(PATH, "user", USER_PREFIX + Shelter.NONE.getPrefix() + fileName + ".md"));
                }
            } else {
                //TODO ошибка
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
            //TODO своё исключение
        }
    }


}
