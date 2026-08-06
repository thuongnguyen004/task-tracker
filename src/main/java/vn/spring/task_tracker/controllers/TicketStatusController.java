package vn.spring.task_tracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.ticket_status.TicketStatusResponse;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.mappers.ticket_status.TicketStatusResponseMapper;
import vn.spring.task_tracker.services.TicketStatusService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket-statuses")
public class TicketStatusController {
    private final TicketStatusService ticketStatusService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketStatusResponse>>> getAllTicketStatuses() {
        List<TicketStatus> ticketPriorities = ticketStatusService.getAllTicketStatuses();
        List<TicketStatusResponse> ticketPriorityResponses = new TicketStatusResponseMapper().build(ticketPriorities);
        return ResponseEntity.ok(ApiResponse.success("Get data ticket priority successfully", ticketPriorityResponses));
    }
}
