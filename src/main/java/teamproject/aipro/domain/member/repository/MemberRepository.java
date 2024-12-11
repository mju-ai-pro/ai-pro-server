package teamproject.aipro.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import teamproject.aipro.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUserid(String id);

	@Query("SELECT m.userid FROM Member m GROUP BY m.userid HAVING COUNT(m.userid) > 1")
	List<String> findDuplicateUserIds();

	List<Member> findAllByUserid(String userid);
}
