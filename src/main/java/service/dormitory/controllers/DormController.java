package service.dormitory.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.dormitory.models.*;

import java.util.Optional;

@Controller
@RequestMapping("/dorm")
public class DormController {
    private final UsrRepository usrRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public DormController(UsrRepository usrRepository, BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.usrRepository = usrRepository;
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping()
    public String dorm(@CookieValue("email") String email){
        Optional<Usr> usr = usrRepository.findByEmail(email).stream().findAny();
        if (usr.isPresent()){
            Optional<Booking> book = bookingRepository.findByUserId(usr.get().getId());
            if (book.isPresent()){
                return book.get().isConfirmed()?"redirect:dorm/confirmed":"redirect:dorm/booked";
            }
        }
        return "redirect:dorm/not";
    }

    @GetMapping("/not")
    public String dont(){
        return "dont";
    }

    @GetMapping("/booking")
    public String booking(){
        return "ProBook";
    }

    @PostMapping("/booking")
    public String showRoom(
            @CookieValue("email") String email,
            @RequestParam int corpus,
            @RequestParam int floor,
            @RequestParam int roomNumber
    ) {
        Optional<Usr> usr = usrRepository.findByEmail(email).stream().findAny();
        if (usr.isPresent()) {
            Optional<Room> roomOptional = roomRepository
                    .findByBlockNumberAndFloorNumberAndRoomNumber(
                            corpus, floor, Integer.toString(floor * 100 + roomNumber));
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                return String.format("redirect:/dorm/show-room?roomId=%d",
                        room.getRoomId(), room.getCapacity());
            }
        }
        return "redirect:not";
    }


    @GetMapping("/show-room")
    public String showRoom(
            @RequestParam int roomId,
            Model model
    ) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            model.addAttribute("capacity", room.getCapacity());
            if (room.getCapacity() > 0) {
                model.addAttribute("roomId", roomId);
                model.addAttribute("showBookButton", true);
            } else model.addAttribute("showBookButton", false);
        }
        return "ProRoom";
    }

    @PostMapping("/show-room")
    public String showRoom(@CookieValue("email") String email,
                           @RequestParam int roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            if (room.getCapacity() > 0) {
                room.setCapacity(room.getCapacity() - 1);
                roomRepository.save(room);

                Optional<Usr> usr = usrRepository.findByEmail(email).stream().findAny();
                if (usr.isPresent()) {
                    Booking booking = new Booking();
                    booking.setUserId(usr.get().getId());
                    booking.setRoomId(room.getRoomId());
                    booking.setConfirmed(false);
                    bookingRepository.save(booking);

                }

                return "redirect:booked";
            }
        }
        return "redirect:not";
    }

    @GetMapping("/booked")
    public String booked(@CookieValue("email") String email, Model model){
        Optional<Usr> usr = usrRepository.findByEmail(email).stream().findAny();
        if (usr.isPresent()) {
            Optional<Booking> book = bookingRepository.findByUserId(usr.get().getId());
            if (book.isPresent()) {
                model.addAttribute("name", usr.get().getName());
                model.addAttribute("bookId", book.get().getId());
                Optional<Room> roomOptional = roomRepository.findById(book.get().getRoomId());
                if (roomOptional.isPresent()) {
                    Room room = roomOptional.get();
                    model.addAttribute("corpus", room.getBlockNumber());
                    model.addAttribute("floor", room.getFloorNumber());
                    model.addAttribute("number", room.getRoomNumber());
                }
            }
        }
        return "infoSelected";
    }

    @PostMapping("/cancel-booking")
    public String cancelBooking(@RequestParam int bookId) {
        Optional<Booking> book = bookingRepository.findById(bookId);
        if (book.isPresent()) {
            Optional<Room> roomOptional = roomRepository.findById(book.get().getRoomId());
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                room.setCapacity(room.getCapacity() + 1);
                roomRepository.save(room);
                bookingRepository.delete(book.get());
            }
        }
        return "redirect:not";
    }

    @PostMapping("/pay-in-full")
    public String payBooking(@RequestParam int bookId) {
        Optional<Booking> book = bookingRepository.findById(bookId);
        if (book.isPresent()) {
            book.get().setConfirmed(true);
            bookingRepository.save(book.get());
        }
        return "redirect:confirmed";
    }

    @GetMapping("/confirmed")
    public String confirmed(@CookieValue("email") String email, Model model){
        Optional<Usr> usr = usrRepository.findByEmail(email).stream().findAny();
        if (usr.isPresent()) {
            Optional<Booking> book = bookingRepository.findByUserId(usr.get().getId());
            if (book.isPresent()) {
                model.addAttribute("name", usr.get().getName());
                model.addAttribute("bookId", book.get().getId());
                Optional<Room> roomOptional = roomRepository.findById(book.get().getRoomId());
                if (roomOptional.isPresent()) {
                    Room room = roomOptional.get();
                    model.addAttribute("corpus", room.getBlockNumber());
                    model.addAttribute("floor", room.getFloorNumber());
                    model.addAttribute("number", room.getRoomNumber());
                }
            }
        }
        return "have_room";
    }
}
