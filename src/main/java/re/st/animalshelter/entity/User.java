package re.st.animalshelter.model.entity;

import re.st.animalshelter.enumeration.Stage;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "chat_id", unique = true)
    private long chatId;
    @Column(name = "volunteer_chat_id", unique = true)
    private long volunteerChatId;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "user_name")
    private String userName;
    @Column(unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Dialog> dialogs;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Animal> animal;
    private boolean owner;
    private String passport;

}