package teamproject.aipro.domain.chat.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatCatalog {
    @Id
    @GeneratedValue
    public Long id;

    @OneToMany(mappedBy = "chatCatalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatHistory> chatHistories;

    @Column(name = "user_id", nullable = false)
    private String userId; // 유저 아이디

    @Column(name = "chatSummary", nullable = false, columnDefinition = "TEXT")
    public String chatSummary; //요약 내용

    public ChatCatalog(String userId, String chatSummary) {
        this.userId = userId;
        this.chatSummary = chatSummary;
    }
}
