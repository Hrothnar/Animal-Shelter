package re.st.animalshelter.dto;

import re.st.animalshelter.enumeration.Shelter;

public class Answer {
    private final long chatId;
    private final int messageId;
    private final boolean owner;
    private final String code;
    private final Shelter shelter;

    public Answer(long chatId, int messageId, boolean owner, String code, Shelter shelter) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.owner = owner;
        this.code = code;
        this.shelter = shelter;
    }

    public long getChatId() {
        return chatId;
    }

    public int getMessageId() {
        return messageId;
    }

    public boolean isOwner() {
        return owner;
    }

    public String getCode() {
        return code;
    }

    public Shelter getShelter() {
        return shelter;
    }
}
