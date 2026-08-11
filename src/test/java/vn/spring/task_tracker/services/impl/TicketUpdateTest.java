package vn.spring.task_tracker.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.repositories.TicketPriorityRepository;
import vn.spring.task_tracker.repositories.TicketRepository;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TicketUpdateTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketStatusRepository ticketStatusRepository;

    @Mock
    private TicketPriorityRepository ticketPriorityRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private TicketServiceImpl ticketService;

    private UUID ticketId;
    private UUID userId;
    private Ticket oldTicket;
    private Ticket newTicket;
    private TicketPriority priority;
    private TicketStatus status;
    private User assignee;

    @BeforeEach
    void setUp() {
        ticketId = UUID.randomUUID();
        userId = UUID.randomUUID();

        priority = new TicketPriority();
        priority.setId((short) 1);
        priority.setName("High");

        status = new TicketStatus();
        status.setId((short) 1);
        status.setName("TO DO");

        assignee = new User();
        assignee.setId(userId);
        assignee.setUsername("john");

        oldTicket = new Ticket(
                "Old title",
                "Old description",
                priority,
                status,
                assignee
        );
        oldTicket.setId(ticketId);

        newTicket = new Ticket(
                "New title",
                "New description",
                priority,
                status,
                assignee
        );
    }

    @Test
    void givenValidTicket_whenUpdateTicket_thenUpdateSuccessfully() {
        given(ticketRepository.findById(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));
        given(ticketStatusRepository.findById(status.getId()))
                .willReturn(Optional.of(status));
        given(userRepository.findById(userId))
                .willReturn(Optional.of(assignee));
        given(ticketRepository.save(any(Ticket.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.updateTicket(ticketId, newTicket);

        assertThat(result.getTitle()).isEqualTo("New title");
        assertThat(result.getDescription()).isEqualTo("New description");
    }

    @Test
    void givenTicketNotFound_whenUpdateTicket_thenThrowResourceNotFoundException() {
        given(ticketRepository.findById(ticketId))
                .willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(ticketId, newTicket)
        );

        assertThat(exception.getMessage()).isEqualTo("Ticket not found.");
    }


    @Test
    void givenInvalidPriority_whenUpdateTicket_thenThrowResourceNotFoundException() {
        given(ticketRepository.findById(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(ticketId, newTicket)
        );

        assertThat(exception.getMessage()).isEqualTo("Ticket priority not found.");
    }


    @Test
    void givenInvalidStatus_whenUpdateTicket_thenThrowResourceNotFoundException() {
        given(ticketRepository.findById(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));
        given(ticketStatusRepository.findById(status.getId()))
                .willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(ticketId, newTicket)
        );

        assertThat(exception.getMessage()).isEqualTo("Ticket status not found.");
    }


    @Test
    void givenNonExistingAssignee_whenUpdateTicket_thenThrowResourceNotFoundException() {
        given(ticketRepository.findById(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));
        given(ticketStatusRepository.findById(status.getId()))
                .willReturn(Optional.of(status));
        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(ticketId, newTicket)
        );

        assertThat(exception.getMessage()).isEqualTo("User not found.");
        then(ticketRepository).should(never()).save(any());
    }


    @Test
    void givenNullAssignee_whenUpdateTicket_thenUpdateSuccessfully() {
        newTicket.setAssignee(null);

        given(ticketRepository.findById(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));
        given(ticketStatusRepository.findById(status.getId()))
                .willReturn(Optional.of(status));
        given(ticketRepository.save(any(Ticket.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.updateTicket(ticketId, newTicket);

        assertThat(result.getAssignee()).isNull();
        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    void givenCodeReviewStatus_whenUpdateStatusToReadyForQA_thenStatusUpdated() {
        TicketStatus readyForQA = new TicketStatus();
        readyForQA.setId((short) 2);
        readyForQA.setName("Ready for QA");

        oldTicket.setStatus(status);
        newTicket.setStatus(readyForQA);

        given(ticketRepository.findById(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));
        given(ticketStatusRepository.findById(readyForQA.getId()))
                .willReturn(Optional.of(readyForQA));
        given(userRepository.findById(userId))
                .willReturn(Optional.of(assignee));
        given(ticketRepository.save(any(Ticket.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.updateTicket(ticketId, newTicket);

        assertThat(result.getStatus().getName()).isEqualTo("Ready for QA");
    }

    @Test
    void givenStatusChanged_whenUpdateTicket_thenActivityShouldBeCreated() {
    }


    @Test
    void givenNoFieldChanged_whenUpdateTicket_thenNoActivityShouldBeCreated() {
    }


    @Test
    void givenArchivedTicket_whenUpdateTicket_thenUpdateShouldBeRejected() {
    }
}
