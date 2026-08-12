package vn.spring.task_tracker.mappers;

import vn.spring.task_tracker.dtos.requests.TicketCreateRequest;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;

public class TicketCreateMapper {
    public Ticket build(TicketCreateRequest request) {

        Ticket ticket = new Ticket();

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());

        if (request.getPriorityId() != null) {
            TicketPriority priority = new TicketPriority();

            priority.setId(request.getPriorityId());
            ticket.setPriority(priority);
        }

        if (request.getStatusId() != null) {
            TicketStatus status = new TicketStatus();

            status.setId(request.getStatusId());
            ticket.setStatus(status);
        }

        if (request.getAssigneeId() != null) {
            User assignee = new User();

            assignee.setId(request.getAssigneeId());
            ticket.setAssignee(assignee);
        }

        return ticket;
    }
}
