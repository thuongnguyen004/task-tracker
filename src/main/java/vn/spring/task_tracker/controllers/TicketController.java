package vn.spring.task_tracker.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.task_tracker.dtos.requests.tickets.TicketCreateRequest;
import vn.spring.task_tracker.dtos.requests.tickets.TicketUpdateRequest;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.tickets.TicketResponse;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.mappers.tickets.TicketCreateMapper;
import vn.spring.task_tracker.mappers.tickets.TicketResponseMapper;
import vn.spring.task_tracker.mappers.tickets.TicketUpdateMapper;
import vn.spring.task_tracker.services.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketPriorityService ticketPriorityService;
    private final TicketStatusService ticketStatusService;
    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final AuthService authService;
    private final SecurityHelper securityHelper;



    @PostMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> getActiveTicketById(@PathVariable("id") UUID id){

        TicketResponseMapper ticketResponseMapper = new TicketResponseMapper();

        Ticket ticket = this.ticketService.getActiveTicketById(id);

        TicketResponse ticketResponse = ticketResponseMapper.build(ticket);

        return ResponseEntity.ok(ApiResponse.success("Get ticket", ticketResponse));
    }
    @PutMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(@PathVariable UUID ticketId,@Valid @RequestBody TicketUpdateRequest ticketUpdateRequest) {
        Ticket ticket = new TicketUpdateMapper().build(ticketUpdateRequest);

        Ticket savedTicket = ticketService.updateTicket(ticketId, ticket);

        TicketResponse ticketResponse = new TicketResponseMapper().build(savedTicket);
        return ResponseEntity.ok(ApiResponse.success("Update ticket successfully", ticketResponse));
    }
}
