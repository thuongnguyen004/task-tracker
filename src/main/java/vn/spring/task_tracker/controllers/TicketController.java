package vn.spring.task_tracker.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.task_tracker.dtos.requests.tickets.TicketCreateRequest;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.tickets.TicketResponse;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.mappers.tickets.TicketCreateMapper;
import vn.spring.task_tracker.mappers.tickets.TicketResponseMapper;
import vn.spring.task_tracker.services.*;

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

        User currentUser = this.securityHelper.getCurrentUser();

        TicketCreateMapper ticketMapper =  new TicketCreateMapper();
        TicketResponseMapper ticketResponseMapper = new TicketResponseMapper();

        TicketPriority ticketPriority = this.ticketPriorityService.getTicketPriorityById(ticketCreateRequest.getPriorityId());
        TicketStatus ticketStatus = this.ticketStatusService.getTicketPriorityById(ticketCreateRequest.getStatusId());
        User user = this.userService.getUserById(ticketCreateRequest.getAssigneeId());

        Ticket ticket = ticketMapper.build(ticketCreateRequest, ticketPriority, ticketStatus, user);

        Ticket newTicket = this.ticketService.createTicket(ticket);

        TicketResponse ticketResponse = ticketResponseMapper.build(newTicket);



        return ResponseEntity.ok(ApiResponse.created("Create ticket successfully", ticketResponse));
    }

}
