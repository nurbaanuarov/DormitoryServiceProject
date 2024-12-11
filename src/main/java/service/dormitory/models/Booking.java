package service.dormitory.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="booking")
@Data
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "room_id")
    private int roomId;

    @Column(name = "is_confirmed")
    private boolean isConfirmed;
}
