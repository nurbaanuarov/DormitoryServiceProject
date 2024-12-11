package service.dormitory.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name="time_slot")
@Data
public class TimeSlot {
    @Id
    @Column(name = "time_slot_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "is_past_time")
    private boolean isPastTime;
}
