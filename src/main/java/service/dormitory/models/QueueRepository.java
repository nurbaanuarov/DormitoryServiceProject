package service.dormitory.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<DiningQueue, Integer> {
    Optional<DiningQueue> findByUserId(int userId);
}
