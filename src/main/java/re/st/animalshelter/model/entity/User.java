package re.st.animalshelter.model.entity;

import re.st.animalshelter.enumeration.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "chat_id", unique = true, nullable = false)
    private long chatId;
    @Column(name = "full_name")
    private String fullName;
    @Column(unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String data;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
    @Column(name = "last_report_date")
    private LocalDateTime lastReportDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean owner;

    public User(long chatId,
                String fullName,
                String email,
                String phoneNumber,
                String data,
                LocalDateTime expirationDate,
                LocalDateTime lastReportDate,
                Status status,
                boolean isOwner) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.data = data;
        this.expirationDate = expirationDate;
        this.lastReportDate = lastReportDate;
        this.status = status;
        this.owner = isOwner;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getLastReportDate() {
        return lastReportDate;
    }

    public void setLastReportDate(LocalDateTime lastReportDate) {
        this.lastReportDate = lastReportDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean status) {
        this.owner = status;
    }
}
