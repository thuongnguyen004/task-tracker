package vn.spring.task_tracker.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.services.TicketActivityService;
import vn.spring.task_tracker.repositories.TicketPriorityRepository;
import vn.spring.task_tracker.repositories.TicketRepository;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Mock
    private TicketActivityService ticketActivityService;

    @Mock
    private SecurityHelper securityHelper;


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
        assignee.setFullName("John");

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
        given(securityHelper.getCurrentUser()).willReturn(assignee);
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
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
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
                .willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(ticketId, newTicket)
        );

        assertThat(exception.getMessage()).isEqualTo("Ticket not found.");
    }


    @Test
    void givenInvalidPriority_whenUpdateTicket_thenThrowResourceNotFoundException() {
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
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
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
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
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
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
    }


    @Test
    void givenNullAssignee_whenUpdateTicket_thenUpdateSuccessfully() {
        newTicket.setAssignee(null);

        given(securityHelper.getCurrentUser()).willReturn(assignee);
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));
        given(ticketStatusRepository.findById(status.getId()))
                .willReturn(Optional.of(status));
        given(ticketRepository.save(any(Ticket.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.updateTicket(ticketId, newTicket);

        assertThat(result.getAssignee()).isNull();
    }

    @Test
    void givenCodeReviewStatus_whenUpdateStatusToReadyForQA_thenStatusUpdated() {
        TicketStatus readyForQA = new TicketStatus();
        readyForQA.setId((short) 2);
        readyForQA.setName("Ready for QA");

        oldTicket.setStatus(status);
        newTicket.setStatus(readyForQA);

        given(securityHelper.getCurrentUser()).willReturn(assignee);
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
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
        TicketStatus readyForQA = new TicketStatus();
        readyForQA.setId((short) 2);
        readyForQA.setName("Ready for QA");
        newTicket.setStatus(readyForQA);

        given(securityHelper.getCurrentUser()).willReturn(assignee);
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));
        given(ticketStatusRepository.findById(readyForQA.getId()))
                .willReturn(Optional.of(readyForQA));
        given(userRepository.findById(userId)).willReturn(Optional.of(assignee));
        given(ticketRepository.save(any(Ticket.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        ticketService.updateTicket(ticketId, newTicket);
    }

    @Test
    void givenTicketOnBoard_whenMoveTicketToAnotherColumn_thenStatusIsUpdated() {
        TicketStatus readyForQA = new TicketStatus();
        readyForQA.setId((short) 2);
        readyForQA.setName("Ready for QA");

        given(securityHelper.getCurrentUser()).willReturn(assignee);
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketStatusRepository.findById(readyForQA.getId()))
                .willReturn(Optional.of(readyForQA));

        ticketService.changeStatusTicket(ticketId, readyForQA.getId());

        assertThat(oldTicket.getStatus()).isEqualTo(readyForQA);
        then(ticketRepository).should().save(oldTicket);
    }

    @Test
    void givenTicketMovedToAnotherColumn_whenStatusUpdateSucceeds_thenStatusActivityIsCreated() {
        TicketStatus readyForQA = new TicketStatus();
        readyForQA.setId((short) 2);
        readyForQA.setName("Ready for QA");

        given(securityHelper.getCurrentUser()).willReturn(assignee);
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketStatusRepository.findById(readyForQA.getId()))
                .willReturn(Optional.of(readyForQA));

        ticketService.changeStatusTicket(ticketId, readyForQA.getId());

        ArgumentCaptor<Ticket> newTicketCaptor = ArgumentCaptor.forClass(Ticket.class);
        then(ticketActivityService).should().createTicketActivity(
                eq(oldTicket),
                newTicketCaptor.capture(),
                eq(assignee)
        );
        assertThat(newTicketCaptor.getValue().getStatus()).isEqualTo(readyForQA);
    }

    @Test
    void givenStatusUpdateFails_whenMoveTicketToAnotherColumn_thenStatusRemainsUnchanged() {
        TicketStatus originalStatus = oldTicket.getStatus();
        short invalidStatusId = 99;

        given(securityHelper.getCurrentUser()).willReturn(assignee);
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
                .willReturn(Optional.of(oldTicket));
        given(ticketStatusRepository.findById(invalidStatusId))
                .willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.changeStatusTicket(ticketId, invalidStatusId)
        );

        assertThat(exception.getMessage()).isEqualTo("Ticket status not found.");
        assertThat(oldTicket.getStatus()).isEqualTo(originalStatus);
        then(ticketRepository).should(never()).save(any());
        then(ticketActivityService).shouldHaveNoInteractions();
    }


    @Test
    void givenNoFieldChanged_whenUpdateTicket_thenNoActivityShouldBeCreated() {
    }


    @Test
    void givenArchivedTicket_whenUpdateTicket_thenUpdateShouldBeRejected() {
        oldTicket.setArchived(true);
        given(ticketRepository.findByIdAndArchivedFalse(ticketId))
                .willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(ticketId, newTicket)
        );

        assertThat(exception.getMessage()).isEqualTo("Ticket not found.");
    }

    @Test
    void givenEmptyTitle_whenUpdateTicket_thenUpdateShouldBeRejected() {
    }

    @Test
    void givenSupportedPriorityValues_whenOpenPriorityDropdown_thenValuesShouldBeDisplayed() {
    }

    @Test
    void givenSupportedStatusValues_whenOpenStatusDropdown_thenValuesShouldBeDisplayed() {
    }

    @Test
    void givenExistingUsers_whenOpenAssigneeDropdown_thenUsersShouldBeDisplayed() {
    }

    @Test
    void givenTitleWith150Characters_whenUpdateTicket_thenUpdateSuccessfully() {
    }

    @Test
    void givenTitleExceeding150Characters_whenUpdateTicket_thenUpdateShouldBeRejected() {
    }

    @Test
    void givenDescriptionWith5000Characters_whenUpdateTicket_thenUpdateSuccessfully() {
    }

    @Test
    void givenDescriptionExceeding5000Characters_whenUpdateTicket_thenUpdateShouldBeRejected() {
    }

    @Test
    void givenAnotherUserCreatedTheTicket_whenUpdateTicket_thenUpdateSuccessfully() {
    }

    @Test
    void givenUnauthenticatedRequest_whenUpdateTicket_thenRequestShouldBeRejected() {
    }

    @Test
    void givenWhitespaceOnlyTitle_whenUpdateTicket_thenUpdateShouldBeRejected() {
    }

    @Test
    void givenDescriptionWithLeadingAndTrailingSpaces_whenUpdateTicket_thenSpacesShouldBeTrimmed() {
    }

    @Test
    void givenWhitespaceOnlyDescription_whenUpdateTicket_thenUpdateShouldBeRejected() {
    }

    @Test
    void givenTicketDraggedWithinSameColumn_whenDropTicket_thenManualOrderIsNotStored() {
    }

    @Test
    void givenMultipleTicketsInSameColumn_whenGetBoard_thenTicketsAreSortedByUpdatedAtDesc() {
    }
}