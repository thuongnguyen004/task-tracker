package vn.spring.task_tracker.mappers.tickets;

import vn.spring.task_tracker.dtos.requests.tickets.TicketUpdateRequest;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;

public class TicketUpdateMapper {
    public Ticket build(TicketUpdateRequest request) {

        TicketPriority priority = new TicketPriority();
        priority.setId(request.getPriorityId());

        TicketStatus status = new TicketStatus();
        status.setId(request.getStatusId());

        User assignee = null;

        if (request.getAssigneeId() != null) {
            assignee = new User();
            assignee.setId(request.getAssigneeId());
        }

        return new Ticket(
                request.getTitle(),
                request.getDescription(),
                priority,
                status,
                assignee
        );
    }

    public void update(Ticket oldValue, Ticket newValue) {
        oldValue.setTitle(newValue.getTitle());
        oldValue.setDescription(newValue.getDescription());
        oldValue.setPriority(newValue.getPriority());
        oldValue.setStatus(newValue.getStatus());
        oldValue.setAssignee(newValue.getAssignee());
    }
}
