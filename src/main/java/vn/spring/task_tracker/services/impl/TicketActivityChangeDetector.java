package vn.spring.task_tracker.services.impl;

import org.springframework.stereotype.Component;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.enums.ActivityEventCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class TicketActivityChangeDetector {

    public List<ActivityChange> detect(Ticket oldTicket, Ticket newTicket) {
        List<ActivityChange> changes = new ArrayList<>();

        addChange(changes, ActivityEventCode.TITLE_CHANGED,
                oldTicket.getTitle(), newTicket.getTitle(),
                oldTicket.getTitle(), newTicket.getTitle());

        addChange(changes, ActivityEventCode.DESCRIPTION_CHANGED,
                oldTicket.getDescription(), newTicket.getDescription(),
                oldTicket.getDescription(), newTicket.getDescription());

        addChange(changes, ActivityEventCode.STATUS_CHANGED,
                oldTicket.getStatus().getId(), newTicket.getStatus().getId(),
                oldTicket.getStatus().getName(), newTicket.getStatus().getName());

        addChange(changes, ActivityEventCode.PRIORITY_CHANGED,
                oldTicket.getPriority().getId(), newTicket.getPriority().getId(),
                oldTicket.getPriority().getName(), newTicket.getPriority().getName());

        UUID oldAssigneeId = oldTicket.getAssignee() == null
                ? null
                : oldTicket.getAssignee().getId();

        UUID newAssigneeId = newTicket.getAssignee() == null
                ? null
                : newTicket.getAssignee().getId();

        String oldAssigneeName = oldTicket.getAssignee() == null
                ? null
                : oldTicket.getAssignee().getFullName();

        String newAssigneeName = newTicket.getAssignee() == null
                ? null
                : newTicket.getAssignee().getFullName();

        addChange(changes, ActivityEventCode.ASSIGNEE_CHANGED,
                oldAssigneeId, newAssigneeId,
                oldAssigneeName, newAssigneeName);

        return changes;
    }

    private void addChange(
            List<ActivityChange> changes,
            ActivityEventCode eventCode,
            Object oldCompareValue,
            Object newCompareValue,
            String oldValue,
            String newValue
    ) {
        if (!Objects.equals(oldCompareValue, newCompareValue)) {
            changes.add(new ActivityChange(eventCode, oldValue, newValue));
        }
    }

    public static class ActivityChange {
        private final ActivityEventCode eventCode;
        private final String oldValue;
        private final String newValue;

        public ActivityChange(
                ActivityEventCode eventCode,
                String oldValue,
                String newValue
        ) {
            this.eventCode = eventCode;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public ActivityEventCode getEventCode() {
            return eventCode;
        }

        public String getOldValue() {
            return oldValue;
        }

        public String getNewValue() {
            return newValue;
        }
    }
}
