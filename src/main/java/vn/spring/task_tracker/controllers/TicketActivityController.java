package vn.spring.task_tracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.TicketActivityResponse;
import vn.spring.task_tracker.entities.TicketActivity;
import vn.spring.task_tracker.mappers.TicketActivityResponseMapper;
import vn.spring.task_tracker.services.TicketActivityService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket-activities")
public class TicketActivityController {

    private final TicketActivityService ticketActivityService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<List<TicketActivityResponse>>> getTicketActivityByIdTicket(@PathVariable UUID ticketId) {
        List<TicketActivity> ticketActivities = ticketActivityService.getTicketActivityByIdTicket(ticketId);

        List<TicketActivityResponse> response = new TicketActivityResponseMapper().buildList(ticketActivities);

        return ResponseEntity.ok(ApiResponse.success("Get activities successfully.", response));
    }
}
