package vn.spring.task_tracker.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.task_tracker.dtos.requests.tickets.TicketCreateRequest;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.AssigneeResponse;
import vn.spring.task_tracker.dtos.responses.tickets.TicketResponse;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.mappers.tickets.TicketCreateMapper;
import vn.spring.task_tracker.mappers.tickets.TicketResponseMapper;
import vn.spring.task_tracker.services.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketPriorityService ticketPriorityService;
    private final TicketStatusService ticketStatusService;
    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final AuthService authService;
    private final SecurityHelper securityHelper;

    @PostMapping("/tickets")
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(@Valid @RequestBody TicketCreateRequest ticketCreateRequest)
    {
        TicketCreateMapper ticketMapper =  new TicketCreateMapper();
        TicketResponseMapper ticketResponseMapper = new TicketResponseMapper();

        Ticket ticket = ticketMapper.build(ticketCreateRequest);

        Ticket newTicket = ticketService.createTicket(ticket);

        TicketResponse ticketResponse = ticketResponseMapper.build(newTicket);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Create ticket successfully", ticketResponse));
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> getActiveTicketById(@PathVariable("id") UUID id){

        TicketResponseMapper ticketResponseMapper = new TicketResponseMapper();

        Ticket ticket = this.ticketService.getActiveTicketById(id);

        TicketResponse ticketResponse = ticketResponseMapper.build(ticket);

        return ResponseEntity.ok(ApiResponse.success("Get ticket", ticketResponse));
    }

    @GetMapping("/ticket-priorities")
    public ResponseEntity<ApiResponse<List<TicketPriority>>> getTicketPriorities() {

        List<TicketPriority> priorities =
                ticketPriorityService.getTicketPriorities();

        return ResponseEntity.ok(
                ApiResponse.success("Get ticket priorities successfully", priorities)
        );
    }

    @GetMapping("/ticket-statuses")
    public ResponseEntity<ApiResponse<List<TicketStatus>>> getTicketStatuses() {

        List<TicketStatus> statuses =
                ticketStatusService.getTicketStatuses();

        return ResponseEntity.ok(
                ApiResponse.success("Get ticket statuses successfully", statuses)
        );
    }

    @GetMapping("/assignees")
    public ResponseEntity<ApiResponse<List<AssigneeResponse>>> getUsers() {

        List<AssigneeResponse> assigneeResponseList = userService.getAssignees();

        return ResponseEntity.ok(
                ApiResponse.success("Get Assignee", assigneeResponseList));
    }
}
