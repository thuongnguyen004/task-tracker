package vn.spring.task_tracker;

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
import vn.spring.task_tracker.repositories.*;
import vn.spring.task_tracker.services.TicketPriorityService;
import vn.spring.task_tracker.services.TicketStatusService;
import vn.spring.task_tracker.services.impl.TicketServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TicketCreateLogicTest {
    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketStatusRepository ticketStatusRepository;

    @Mock
    private TicketPriorityRepository ticketPriorityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityHelper securityHelper;

    @Mock
    private TicketPriorityService ticketPriorityService;

    @Mock
    private TicketStatusService ticketStatusService;

    private TicketPriority createPriority(short id, String name) {
        TicketPriority priority = new TicketPriority();
        priority.setId(id);
        priority.setName(name);
        return priority;
    }

    private TicketStatus createStatus(short id, String name) {
        TicketStatus status = new TicketStatus();
        status.setId(id);
        status.setName(name);
        return status;
    }

    private User createUser(UUID id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    private void mockCommonDependencies(
            User currentUser,
            TicketPriority priority,
            TicketStatus status
    ) {
        given(securityHelper.getCurrentUser())
                .willReturn(currentUser);

        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));

        given(ticketStatusRepository.findById(status.getId()))
                .willReturn(Optional.of(status));
    }

    @Test
    public void give_create_ticket_data_when_create_ticket_then_return_created_ticket(){

        //given

        TicketPriority priority = createPriority((short) 1, "High");
        TicketStatus status = createStatus((short) 1, "To Do");

        UUID assigneeId = UUID.randomUUID();
        User assignee = createUser(assigneeId, "Alex Lee");

        User currentUser = createUser(UUID.randomUUID(), "John Doe");
        Ticket ticket = new Ticket(
                "Fix login bug on invalid password",
                "Login API returns 500 instead of 401",
                priority,
                status,
                assignee
        );

        mockCommonDependencies(currentUser, priority, status);

        given(userRepository.findById(assigneeId))
                .willReturn(Optional.of(assignee));

        given(ticketRepository.save(ticket))
                .willReturn(ticket);

        // When

        Ticket result = ticketService.createTicket(ticket);

        // Then

        assertEquals("Fix login bug on invalid password", result.getTitle());
        assertEquals("Login API returns 500 instead of 401", result.getDescription());
        assertEquals("High", result.getPriority().getName());
        assertEquals("To Do", result.getStatus().getName());
        assertEquals("Alex Lee", result.getAssignee().getUsername());

    }

    @Test
    public void given_current_user_as_assignee_when_create_ticket_then_return_created_ticket() {

        // Given

        TicketPriority priority = createPriority((short) 1, "High");
        TicketStatus status = createStatus((short) 1, "To Do");

        UUID assigneeId = UUID.randomUUID();
        User assignee = createUser(assigneeId, "John Doe");

        User currentUser = createUser(UUID.randomUUID(), "John Doe");

        Ticket ticket = new Ticket(
                "Refactor prompts.py module",
                null,
                priority,
                status,
                assignee
        );

        mockCommonDependencies(currentUser, priority, status);

        given(userRepository.findById(assigneeId))
                .willReturn(Optional.of(assignee));

        given(ticketRepository.save(ticket))
                .willReturn(ticket);

        // When

        Ticket result = ticketService.createTicket(ticket);

        // Then

        assertEquals("Refactor prompts.py module", result.getTitle());
        assertNull(result.getDescription());
        assertEquals("High", result.getPriority().getName());
        assertEquals("To Do", result.getStatus().getName());
        assertEquals("John Doe", result.getAssignee().getUsername());
        assertEquals("John Doe", result.getCreatedBy().getUsername());
    }

    @Test
    public void given_missing_priority_when_create_ticket_then_throw_resource_not_found_exception() {

        // Given

        TicketPriority priority = createPriority((short) 1, "High");
        TicketStatus status = createStatus((short) 1, "To Do");

        UUID assigneeId = UUID.randomUUID();
        User assignee = createUser(assigneeId, "Alex Lee");

        User currentUser = createUser(UUID.randomUUID(), "John Doe");

        Ticket ticket = new Ticket(
                "Fix login bug",
                "Description",
                priority,
                status,
                assignee
        );

        given(securityHelper.getCurrentUser())
                .willReturn(currentUser);

        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.empty());

        // When

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.createTicket(ticket)
        );

        // Then

        assertEquals("Ticket priority not found.", exception.getMessage());
    }

    @Test
    public void given_missing_status_when_create_ticket_then_throw_resource_not_found_exception() {

        // Given

        TicketPriority priority = createPriority((short) 1, "High");
        TicketStatus status = createStatus((short) 1, "To Do");

        UUID assigneeId = UUID.randomUUID();
        User assignee = createUser(assigneeId, "Alex Lee");

        User currentUser = createUser(UUID.randomUUID(), "John Doe");

        Ticket ticket = new Ticket(
                "Fix login bug",
                "Description",
                priority,
                status,
                assignee
        );

        given(securityHelper.getCurrentUser())
                .willReturn(currentUser);

        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));

        given(ticketStatusRepository.findById(status.getId()))
                .willReturn(Optional.empty());

        // When

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.createTicket(ticket)
        );

        // Then

        assertEquals("Ticket status not found.", exception.getMessage());
    }

    @Test
    public void given_missing_assignee_when_create_ticket_then_throw_resource_not_found_exception() {

        // Given

        TicketPriority priority = createPriority((short) 1, "High");
        TicketStatus status = createStatus((short) 1, "To Do");

        UUID assigneeId = UUID.randomUUID();
        User assignee = createUser(assigneeId, "Alex Lee");

        User currentUser = createUser(UUID.randomUUID(), "John Doe");

        Ticket ticket = new Ticket(
                "Fix login bug",
                "Description",
                priority,
                status,
                assignee
        );

        given(securityHelper.getCurrentUser())
                .willReturn(currentUser);

        given(ticketPriorityRepository.findById(priority.getId()))
                .willReturn(Optional.of(priority));

        given(ticketStatusRepository.findById(status.getId()))
                .willReturn(Optional.of(status));

        given(userRepository.findById(assigneeId))
                .willReturn(Optional.empty());

        // When

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.createTicket(ticket)
        );

        // Then

        assertEquals("Assignee not found", exception.getMessage());
    }
}
