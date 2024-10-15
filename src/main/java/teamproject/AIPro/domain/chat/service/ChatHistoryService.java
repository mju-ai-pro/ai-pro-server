package teamproject.AIPro.domain.chat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamproject.AIPro.domain.chat.entity.ChatHistory;
import teamproject.AIPro.domain.chat.repository.ChatHistoryRepository;

@Service
public class ChatHistoryService {
  @Autowired private ChatHistoryRepository chatHistoryRepository;

  public ChatHistory saveChatHistory(String userId, String question, String response) {
    ChatHistory chatHistory = new ChatHistory(userId, question, response);
    return chatHistoryRepository.save(chatHistory);
  }

  public List<ChatHistory> getChatHistory(String userId) {
    return chatHistoryRepository.findByUserId(userId);
  }
}
