package com.unibuc.fmi.mycinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.fmi.mycinema.dto.OrderDetailsDto;
import com.unibuc.fmi.mycinema.dto.OrderDto;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.service.impl.OrderServiceImpl;
import com.unibuc.fmi.mycinema.utils.OrderMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {

    @MockBean
    private final OrderServiceImpl orderService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public OrderControllerTest(OrderServiceImpl orderService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void addOrderTest() throws Exception {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        OrderDetailsDto orderDetailsDto = OrderMocks.mockOrderDetailsDto();
        when(orderService.add(any())).thenReturn(orderDetailsDto);

        String orderDtoBody = objectMapper.writeValueAsString(orderDto);
        String orderDetailsDtoBody = objectMapper.writeValueAsString(orderDetailsDto);
        MvcResult result = mockMvc.perform(post("/orders")
                .content(orderDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieName").value(orderDetailsDto.getMovieName()))
                .andExpect(jsonPath("$.roomName").value(orderDetailsDto.getRoomName()))
                .andExpect(jsonPath("$.movieDate").value(orderDetailsDto.getMovieDate().toString()))
                .andExpect(jsonPath("$.purchaseDate").value(orderDetailsDto.getPurchaseDate().toString()))
                .andExpect(jsonPath("$.totalPrice").value(orderDetailsDto.getTotalPrice()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), orderDetailsDtoBody);
    }

    @Test
    public void addOrderThrowsCustomerEntityNotFoundExceptionTest() throws Exception {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        when(orderService.add(any())).thenThrow(new EntityNotFoundException("There is no customer with email " + orderDto.getCustomerEmail() + "!"));

        String orderDtoBody = objectMapper.writeValueAsString(orderDto);
        mockMvc.perform(post("/orders")
                .content(orderDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void addOrderThrowsMovieEntityNotFoundExceptionTest() throws Exception {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        when(orderService.add(any())).thenThrow(new EntityNotFoundException("There is no movie with name " + orderDto.getMovieName() + "!"));

        String orderDtoBody = objectMapper.writeValueAsString(orderDto);
        mockMvc.perform(post("/orders")
                        .content(orderDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void addOrderThrowsRoomEntityNotFoundExceptionTest() throws Exception {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        when(orderService.add(any())).thenThrow(new EntityNotFoundException("There is no room with name " + orderDto.getRoomName() + "!"));

        String orderDtoBody = objectMapper.writeValueAsString(orderDto);
        mockMvc.perform(post("/orders")
                        .content(orderDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void addOrderThrowsMovieScheduleBadRequestExceptionTest() throws Exception {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        when(orderService.add(any())).thenThrow(new BadRequestException("The selected movie is not scheduled at the requested date and time!"));

        String orderDtoBody = objectMapper.writeValueAsString(orderDto);
        mockMvc.perform(post("/orders")
                        .content(orderDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void addOrderThrowsPastDateBadRequestExceptionTest() throws Exception {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        when(orderService.add(any())).thenThrow(new BadRequestException("You can't make a reservation in the past!"));

        String orderDtoBody = objectMapper.writeValueAsString(orderDto);
        mockMvc.perform(post("/orders")
                        .content(orderDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void addOrderThrowsNotEnoughTicketsBadRequestExceptionTest() throws Exception {
        OrderDto orderDto = OrderMocks.mockOrderDto();
        when(orderService.add(any())).thenThrow(new BadRequestException("There are not enough tickets to process your order!"));

        String orderDtoBody = objectMapper.writeValueAsString(orderDto);
        mockMvc.perform(post("/orders")
                        .content(orderDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
