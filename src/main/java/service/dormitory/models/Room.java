package service.dormitory.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="Room")
@Data
public class Room {
    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roomId;

    @Column(name = "block_number")
    private int blockNumber;

    @Column(name = "floor_number")
    private int floorNumber;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "capacity")
    private int capacity;
}
