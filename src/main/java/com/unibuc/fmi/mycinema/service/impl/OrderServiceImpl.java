package com.unibuc.fmi.mycinema.service.impl;

import com.unibuc.fmi.mycinema.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinema.dto.OrderDetailsDto;
import com.unibuc.fmi.mycinema.dto.OrderDto;
import com.unibuc.fmi.mycinema.entity.*;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.mapper.OrderMapper;
import com.unibuc.fmi.mycinema.repository.*;
import com.unibuc.fmi.mycinema.service.CommonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.unibuc.fmi.mycinema.constants.Constants.*;

@Service
public class OrderServiceImpl implements CommonService<OrderDetailsDto, OrderDto> {

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final MovieRepository movieRepository;

    private final RoomRepository roomRepository;

    private final MovieScheduleRepository movieScheduleRepository;

    private final TicketRepository ticketRepository;

    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, MovieRepository movieRepository, RoomRepository roomRepository, MovieScheduleRepository movieScheduleRepository, TicketRepository ticketRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.movieScheduleRepository = movieScheduleRepository;
        this.ticketRepository = ticketRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderDetailsDto add(OrderDto orderDto) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(orderDto.getCustomerEmail());
        if(optionalCustomer.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "customer", "email", orderDto.getCustomerEmail()));
        }

        Optional<Movie> optionalMovie = movieRepository.findByName(orderDto.getMovieName());
        if(optionalMovie.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "movie", "name", orderDto.getMovieName()));
        }

        Optional<Room> optionalRoom = roomRepository.findByName(orderDto.getRoomName());
        if(optionalRoom.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "room", "name", orderDto.getRoomName()));
        }

        Movie movie = optionalMovie.get();
        Room room = optionalRoom.get();
        MovieScheduleId movieScheduleId = MovieScheduleId.builder()
                .movieId(movie.getId())
                .roomId(room.getId())
                .date(orderDto.getMovieDate())
                .hour(orderDto.getMovieHour())
                .build();
        Optional<MovieSchedule> optionalMovieSchedule = movieScheduleRepository.findById(movieScheduleId);
        if(optionalMovieSchedule.isEmpty()) {
            throw new BadRequestException(MOVIE_NOT_SCHEDULED);
        }

        if(LocalDateTime.of(orderDto.getMovieDate(), orderDto.getMovieHour()).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(PAST_RESERVATION);
        }

        MovieSchedule movieSchedule = optionalMovieSchedule.get();
        int roomCapacity = room.getNumberOfRows() * room.getSeatsPerRow();
        int soldTickets = movieSchedule.getTickets().size();
        if(roomCapacity - soldTickets < orderDto.getNumberOfTickets()) {
            throw new BadRequestException(NOT_ENOUGH_TICKETS);
        }

        Optional<Ticket> optionalLastTicket = ticketRepository.findLastTicketForMovie(movieSchedule);
        int row = 1;
        int seat = 0;
        if(optionalLastTicket.isPresent()) {
            row = optionalLastTicket.get().getNumberOfRow();
            seat = optionalLastTicket.get().getSeat();
        }

        List<Ticket> tickets = new ArrayList<>();
        for(int i = 0; i < orderDto.getNumberOfTickets(); i++) {
            seat++;
            if(seat > room.getSeatsPerRow()) {
                row++;
                seat = 1;
            }
            Ticket ticket = Ticket.builder()
                    .numberOfRow(row)
                    .seat(seat)
                    .movieSchedule(movieSchedule)
                    .build();
            tickets.add(ticket);
        }

        Order order = Order.builder()
                .totalPrice(movieSchedule.getPrice() * tickets.size())
                .date(LocalDate.now())
                .hour(LocalTime.now())
                .tickets(tickets)
                .build();
        return orderMapper.mapToOrderDetailsDto(orderRepository.save(order));
    }
}
