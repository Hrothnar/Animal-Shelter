package re.st.animalshelter.enumeration;

import re.st.animalshelter.enumeration.animal.Shelter;

import java.util.Arrays;
import java.util.Objects;

public enum Status {
    NONE,

    CONTACT_INFO,
    CONTACT_INFO_RECEIVED,

    REPORTED,
    REPORT_TEXT,
    REPORT_PHOTO,
    REPORT_WAS_NOT_SENT;


    public static Status getStatus(String string) {
        return Arrays.stream(Status.values())
                .filter(e -> Objects.equals(e.toString(), string))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

}
