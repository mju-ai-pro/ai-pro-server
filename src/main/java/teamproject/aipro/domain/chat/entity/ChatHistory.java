package teamproject.aipro.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
public class ChatHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "chat_inventory_id")
	@JsonIgnoreProperties("chatHistories") // 이 필드를 JSON 직렬화에서 제외
	private ChatInventory chatInventory;

	@Column(name = "question", nullable = false, columnDefinition = "TEXT")
	private String question; // 질문 내용

	@Column(name = "response", columnDefinition = "TEXT")
	private String response; // 답변 내용

	public ChatHistory(String question, String response) {
		this.question = question;
		this.response = response;
	}

	public ChatInventory getChatInventory() {
		return chatInventory;
	}

	public void setChatInventory(ChatInventory chatInventory) {
		this.chatInventory = chatInventory;
	}

}
