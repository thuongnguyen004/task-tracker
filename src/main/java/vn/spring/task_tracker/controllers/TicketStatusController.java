package vn.spring.task_tracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.task_tracker.constants.TicketStatusMessage;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.TicketStatusResponse;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.mappers.TicketStatusResponseMapper;
import vn.spring.task_tracker.services.TicketStatusService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket-statuses")
public class TicketStatusController {
    private final TicketStatusService ticketStatusService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketStatusResponse>>> getAllTicketStatuses() {
        List<TicketStatus> ticketStatuses = ticketStatusService.getAllTicketStatuses();

        List<TicketStatusResponse> ticketStatusResponses = new TicketStatusResponseMapper().build(ticketStatuses);

        return ResponseEntity.ok(ApiResponse.success(TicketStatusMessage.GET_ALL_SUCCESS, ticketStatusResponses));
    }
}
