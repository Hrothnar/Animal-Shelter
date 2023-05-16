package re.st.animalshelter.dto;

import re.st.animalshelter.enumeration.Status;

public class NotifyDTO {
    private long userChatId;
    private long companionChatId;
    private Status status;

    public NotifyDTO(long userChatId, long companionChatId, Status status) {
        this.userChatId = userChatId;
        this.companionChatId = companionChatId;
        this.status = status;
    }

    public NotifyDTO(long userChatId, Status status) {
        this.userChatId = userChatId;
        this.status = status;
    }

    public NotifyDTO() {

    }

    public long getUserChatId() {
        return userChatId;
    }

    public long getCompanionChatId() {
        return companionChatId;
    }

    public Status getStatus() {
        return status;
    }

    public void setUserChatId(long userChatId) {
        this.userChatId = userChatId;
    }

    public void setCompanionChatId(long companionChatId) {
        this.companionChatId = companionChatId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
