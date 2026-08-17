package vn.spring.task_tracker.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.task_tracker.constants.TicketMessage;
import vn.spring.task_tracker.dtos.requests.TicketCreateRequest;
import vn.spring.task_tracker.dtos.requests.TicketUpdateRequest;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.TicketResponse;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.mappers.TicketCreateMapper;
import vn.spring.task_tracker.mappers.TicketResponseMapper;
import vn.spring.task_tracker.mappers.TicketUpdateMapper;
import vn.spring.task_tracker.services.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(
            @Valid
            @RequestBody
            TicketCreateRequest ticketCreateRequest
    ) {
        TicketCreateMapper ticketMapper = new TicketCreateMapper();
        TicketResponseMapper ticketResponseMapper = new TicketResponseMapper();

        Ticket ticket = ticketMapper.build(ticketCreateRequest);

        Ticket newTicket = ticketService.createTicket(ticket);

        TicketResponse ticketResponse = ticketResponseMapper.build(newTicket);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(TicketMessage.CREATE_SUCCESS, ticketResponse));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketByCode(
            @PathVariable("code")
            String code
    ) {

        TicketResponseMapper ticketResponseMapper = new TicketResponseMapper();

        Ticket ticket = this.ticketService.getTicketByCode(code);

        TicketResponse ticketResponse = ticketResponseMapper.build(ticket);

        return ResponseEntity.ok(ApiResponse.success(TicketMessage.GET_BY_CODE_SUCCESS, ticketResponse));
    }

    @GetMapping()
    public ResponseEntity<
        ApiResponse<List<TicketResponse>>
    > getAllActiveTickets(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) List<Short> priorityIds,
        @RequestParam(required = false) UUID assigneeId,
        @RequestParam(
            required = false,
            defaultValue = "false"
        ) boolean unassigned
    ) {
        List<Ticket> tickets;
        if (
            title != null ||
            priorityIds != null ||
            assigneeId != null ||
            unassigned
        ) {
            tickets = ticketService.findWithFilters(
                title,
                priorityIds,
                assigneeId,
                unassigned
            );
        } else {
            tickets = ticketService.getAllActiveTickets();
        }

        List<TicketResponse> ticketResponse =
            new TicketResponseMapper().buildList(tickets);

        return ResponseEntity.ok(
            ApiResponse.success(
                TicketMessage.GET_ALL_ACTIVE_SUCCESS,
                ticketResponse
            )
        );
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(
        @PathVariable UUID ticketId,
        @Valid @RequestBody TicketUpdateRequest ticketUpdateRequest
    ) {
        Ticket ticket = new TicketUpdateMapper().build(ticketUpdateRequest);

        Ticket savedTicket = ticketService.updateTicket(ticketId, ticket);

        TicketResponse ticketResponse = new TicketResponseMapper().build(
            savedTicket
        );

        return ResponseEntity.ok(
            ApiResponse.success(TicketMessage.UPDATE_SUCCESS, ticketResponse)
        );
    }

    @PatchMapping("/{ticketId}/status/{statusId}")
    public ResponseEntity<ApiResponse<TicketResponse>> changeStatusTicket(
        @PathVariable UUID ticketId,
        @PathVariable short statusId
    ) {
        Ticket savedTicket = ticketService.changeStatusTicket(ticketId, statusId);

        TicketResponse ticketResponse = new TicketResponseMapper().build(savedTicket);

        return ResponseEntity.ok(ApiResponse.success(TicketMessage.CHANGE_SUCCESS, ticketResponse));
    }

    @GetMapping("/archives")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllArchiveTickets() {
        List<Ticket> ticket = ticketService.getAllArchiveTickets();

        List<TicketResponse> ticketResponse = new TicketResponseMapper().buildList(ticket);

        return ResponseEntity.ok(ApiResponse.success(TicketMessage.GET_ALL_ARCHIVE_SUCCESS, ticketResponse));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archiveTicket(
            @PathVariable
            UUID id
    ) {
        ticketService.archiveTicket(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreTicket(
            @PathVariable
            UUID id
    ) {
        ticketService.restoreTicket(id);

        return ResponseEntity.noContent().build();
    }
}
