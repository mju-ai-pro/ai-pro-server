package teamproject.aipro.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamproject.aipro.domain.chat.entity.ChatInventory;

@Repository
public interface ChatInventoryRepository extends JpaRepository<ChatInventory, Long> {
}
