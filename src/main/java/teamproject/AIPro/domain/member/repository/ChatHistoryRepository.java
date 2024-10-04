package teamproject.AIPro.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamproject.AIPro.domain.member.entity.ChatHistory;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory,Long> {
    List<ChatHistory> findByUserId(String userId);
}
