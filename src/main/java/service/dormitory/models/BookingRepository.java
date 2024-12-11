package service.dormitory.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByUserId(int bookingId);
    Optional<Booking> findByRoomId(int roomId);
}
