package re.st.animalshelter.enumeration.animal;

import re.st.animalshelter.enumeration.Button;

import java.util.Arrays;
import java.util.Objects;

public enum Shelter {
    NONE, CAT, DOG;


    public static Shelter getShelter(String string) {
        return Arrays.stream(Shelter.values())
                .filter(e -> Objects.equals(e.toString(), string))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

}
