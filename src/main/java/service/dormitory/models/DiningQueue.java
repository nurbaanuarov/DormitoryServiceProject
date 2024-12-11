package service.dormitory.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="queue")
@Data
public class DiningQueue {
    @Id
    @Column(name = "queue_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;
}
