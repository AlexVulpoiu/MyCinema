package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinema.dto.OrderDetailsDto;
import com.unibuc.fmi.mycinema.dto.OrderDto;
import com.unibuc.fmi.mycinema.entity.*;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.mapper.OrderMapper;
import com.unibuc.fmi.mycinema.repository.*;
import com.unibuc.fmi.mycinema.service.impl.OrderServiceImpl;
import com.unibuc.fmi.mycinema.utils.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.unibuc.fmi.mycinema.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MovieScheduleRepository movieScheduleRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private OrderMapper orderMapper;

    @Test
    public void addOrderTest() {
        Customer customer = CustomerMocks.mockCustomer();
        Movie movie = MovieMocks.mockMovie();
        Room room = RoomMocks.mockRoom();
        MovieSchedule movieSchedule = MovieScheduleMocks.mockMovieSchedule(movie, room);
        Ticket ticket = TicketMocks.mockTicket(movieSchedule);
        Order order = OrderMocks.mockOrder(movieSchedule.getPrice(), List.of(ticket));
        OrderDetailsDto orderDetailsDto = OrderMocks.mockOrderDetailsDto();
        OrderDto orderDto = OrderMocks.mockOrderDto();
        Ticket lastTicket = TicketMocks.mockTicket(movieSchedule);
        lastTicket.setSeat(room.getSeatsPerRow());

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(movieScheduleRepository.findById(movieSchedule.getId())).thenReturn(Optional.of(movieSchedule));
        when(ticketRepository.findLastTicketForMovie(movieSchedule)).thenReturn(Optional.of(lastTicket));
        when(orderRepository.save(any())).thenReturn(order);    // any() because using order would cause an error due to exact time
        when(orderMapper.mapToOrderDetailsDto(order)).thenReturn(orderDetailsDto);
        OrderDetailsDto result = orderService.add(orderDto);

        assertEquals(result, orderDetailsDto);
    }

    @Test
    public void addOrderThrowsCustomerEntityNotFoundException() {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> orderService.add(orderDto));
        assertEquals(String.format(ENTITY_NOT_FOUND, "customer", "email", orderDto.getCustomerEmail()), entityNotFoundException.getMessage());
    }

    @Test
    public void addOrderThrowsMovieNotFoundException() {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        Customer customer = CustomerMocks.mockCustomer();

        when(customerRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(customer));
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> orderService.add(orderDto));
        assertEquals(String.format(ENTITY_NOT_FOUND, "movie", "name", "Test movie"), entityNotFoundException.getMessage());
    }

    @Test
    public void addOrderThrowsRoomNotFoundException() {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        Customer customer = CustomerMocks.mockCustomer();
        Movie movie = MovieMocks.mockMovie();

        when(customerRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(customer));
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.of(movie));
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> orderService.add(orderDto));
        assertEquals(String.format(ENTITY_NOT_FOUND, "room", "name", "Test room"), entityNotFoundException.getMessage());
    }

    @Test
    public void addOrderThrowsNotScheduledBadRequestException() {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        Customer customer = CustomerMocks.mockCustomer();
        Movie movie = MovieMocks.mockMovie();
        Room room = RoomMocks.mockRoom();
        MovieScheduleId movieScheduleId = MovieScheduleMocks.mockMovieScheduleId();
        movieScheduleId.setDate(LocalDate.now().minusDays(2));

        when(customerRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(customer));
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> orderService.add(orderDto));
        assertEquals(MOVIE_NOT_SCHEDULED, badRequestException.getMessage());
    }

    @Test
    public void addOrderThrowsPastReservationBadRequestException() {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        orderDto.setMovieDate(LocalDate.now().minusDays(2));
        Customer customer = CustomerMocks.mockCustomer();
        Movie movie = MovieMocks.mockMovie();
        Room room = RoomMocks.mockRoom();
        MovieScheduleId movieScheduleId = MovieScheduleMocks.mockMovieScheduleId();
        movieScheduleId.setDate(LocalDate.now().minusDays(2));
        MovieSchedule movieSchedule = MovieScheduleMocks.mockMovieSchedule(movie, room);

        when(customerRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(customer));
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(movieScheduleRepository.findById(movieScheduleId)).thenReturn(Optional.of(movieSchedule));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> orderService.add(orderDto));
        assertEquals(PAST_RESERVATION, badRequestException.getMessage());
    }

    @Test
    public void addOrderThrowsNotEnoughTicketsBadRequestException() {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        orderDto.setNumberOfTickets(1000);
        Customer customer = CustomerMocks.mockCustomer();
        Movie movie = MovieMocks.mockMovie();
        Room room = RoomMocks.mockRoom();
        MovieScheduleId movieScheduleId = MovieScheduleMocks.mockMovieScheduleId();
        MovieSchedule movieSchedule = MovieScheduleMocks.mockMovieSchedule(movie, room);

        when(customerRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(customer));
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(movieScheduleRepository.findById(movieScheduleId)).thenReturn(Optional.of(movieSchedule));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> orderService.add(orderDto));
        assertEquals(NOT_ENOUGH_TICKETS, badRequestException.getMessage());
    }
}
