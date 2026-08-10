package vn.spring.task_tracker.mappers;

import vn.spring.task_tracker.entities.ActivityEventType;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketActivity;
import vn.spring.task_tracker.entities.User;


public class TicketActivityCreateMapper {
    public TicketActivity build(
            Ticket ticket,
            ActivityEventType eventType,
            User performedBy,
            String oldValue,
            String newValue
    ) {
        return new TicketActivity(
                oldValue,
                newValue,
                ticket,
                eventType,
                performedBy
        );
    }


}
