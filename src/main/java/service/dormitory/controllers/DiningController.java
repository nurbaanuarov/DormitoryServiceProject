package service.dormitory.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.dormitory.models.*;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/dining")
public class DiningController {

    private final UsrRepository usrRepository;
    private final BookingRepository bookingRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final QueueRepository queueRepository;

    public DiningController(UsrRepository usrRepository,
                            BookingRepository bookingRepository,
                            TimeSlotRepository timeSlotRepository,
                            QueueRepository queueRepository) {
        this.usrRepository = usrRepository;
        this.bookingRepository = bookingRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.queueRepository = queueRepository;
    }

    @GetMapping
    public String dining(@CookieValue("email") String email, Model model){
        Optional<Usr> usr = usrRepository.findByEmail(email).stream().findAny();
        if (usr.isPresent()){
            Optional<Booking> book = bookingRepository.findByUserId(usr.get().getId());
            if (book.isEmpty()){
                return "redirect:/dorm/not";
            }
            List<TimeSlot> timeSlots = timeSlotRepository.findAll();

            Optional<DiningQueue> userQueue = queueRepository.findByUserId(usr.get().getId());
            boolean isInQueue = userQueue.isPresent();

            timeSlots.sort(Comparator.comparing(TimeSlot::getStartTime));
            for (TimeSlot slot : timeSlots) {
                LocalTime now = LocalTime.now();
                slot.setPastTime(slot.getEndTime().isBefore(now));
            }

            model.addAttribute("timeSlots", timeSlots);
            model.addAttribute("isInQueue", isInQueue);
            userQueue.ifPresent(queue -> model.addAttribute("queueTimeSlot", queue.getTimeSlot()));
            return "2Dining_slot";
        }
        return "redirect:/dorm";
    }

    @PostMapping
    public String diningPost(@CookieValue("email") String email,
                             @RequestParam(value = "time-slot", defaultValue = "00:00") LocalTime timeSlot) {
        Optional<Usr> usr = usrRepository.findByEmail(email).stream().findAny();
        if (usr.isPresent()) {
            Optional<DiningQueue> userQueue = queueRepository.findByUserId(usr.get().getId());
            if (userQueue.isEmpty()) {
                Optional<TimeSlot> slot = timeSlotRepository.findByStartTime(timeSlot);
                if (slot.isPresent()) {

                    DiningQueue queue = new DiningQueue();
                    queue.setUserId(usr.get().getId());
                    queue.setTimeSlot(slot.get());
                    queueRepository.save(queue);

                    slot.get().setCapacity(slot.get().getCapacity() - 1);
                    slot.get().setPastTime(true);
                    timeSlotRepository.save(slot.get());
                }
            } else {
                TimeSlot slot = userQueue.get().getTimeSlot();
                if (!slot.getEndTime().isBefore(LocalTime.now())) {
                    queueRepository.delete(userQueue.get());
                    slot.setCapacity(slot.getCapacity() + 1);
                    slot.setPastTime(false);
                    timeSlotRepository.save(slot);
                }
            }
        }
        return "redirect:/dining";
    }
}
