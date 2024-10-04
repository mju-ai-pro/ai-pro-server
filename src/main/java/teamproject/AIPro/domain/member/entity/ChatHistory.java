package teamproject.AIPro.domain.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;  // 유저 아이디

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;  // 질문 내용

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;  // 답변 내용

    public ChatHistory(String userId, String question, String response) {
        this.userId = userId;
        this.question = question;
        this.response = response;
    }
}
