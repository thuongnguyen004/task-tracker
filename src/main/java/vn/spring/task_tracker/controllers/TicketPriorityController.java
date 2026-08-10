package vn.spring.task_tracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.task_tracker.constants.TicketPriorityMessage;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.TicketPriorityResponse;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.mappers.TicketPriorityResponseMapper;
import vn.spring.task_tracker.services.TicketPriorityService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket-priorities")
public class TicketPriorityController {
    private final TicketPriorityService ticketPriorityService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketPriorityResponse>>> getAllTicketPriorities() {
        List<TicketPriority> ticketPriorities = ticketPriorityService.getAllTicketPriorities();

        List<TicketPriorityResponse> ticketPriorityResponses =
                new TicketPriorityResponseMapper().build(ticketPriorities);

        return ResponseEntity
                .ok(ApiResponse.success(TicketPriorityMessage.GET_ALL_SUCCESS, ticketPriorityResponses));
    }
}
