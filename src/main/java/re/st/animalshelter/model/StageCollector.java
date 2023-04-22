package re.st.animalshelter.model;

import re.st.animalshelter.enumeration.Status;

public class StageCollector {
    private Status currentStatus = Status.NONE;
    private Status previousStatus = Status.NONE;

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Status getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(Status previousStatus) {
        this.previousStatus = previousStatus;
    }


}
