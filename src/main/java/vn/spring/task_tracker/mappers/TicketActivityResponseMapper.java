package vn.spring.task_tracker.mappers;

import vn.spring.task_tracker.dtos.responses.TicketActivityResponse;
import vn.spring.task_tracker.entities.ActivityEventType;
import vn.spring.task_tracker.entities.TicketActivity;

import java.util.List;

public class TicketActivityResponseMapper {
    public TicketActivityResponse build(TicketActivity ticketActivity) {
        ActivityEventType activityEventType = new ActivityEventType();
        return new TicketActivityResponse(
                ticketActivity.getId(),
                ticketActivity.getOldValue(),
                ticketActivity.getNewValue(),
                ticketActivity.getCreatedAt(),
                ticketActivity.getUpdatedAt(),
                ticketActivity.getEventType().getCode(),
                ticketActivity.getPerformedBy().getFullName()
        );
    }

    public List<TicketActivityResponse> buildList(List<TicketActivity> ticketActivities) {
        return ticketActivities.stream()
                .map(this::build)
                .toList();
    }
}
