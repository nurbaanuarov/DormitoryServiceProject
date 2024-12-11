package service.dormitory.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByBlockNumberAndFloorNumberAndRoomNumber(int blockNumber, int floorNumber, String roomNumber);
}
