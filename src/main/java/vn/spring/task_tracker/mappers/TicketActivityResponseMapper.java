package vn.spring.task_tracker.mappers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import vn.spring.task_tracker.dtos.responses.PageResponse;
import vn.spring.task_tracker.dtos.responses.TicketActivityResponse;
import vn.spring.task_tracker.entities.TicketActivity;

public class TicketActivityResponseMapper {

    public TicketActivityResponse build(TicketActivity ticketActivity) {
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

    public PageResponse<TicketActivityResponse> buildList(
            Page<TicketActivity> ticketActivities
    ) {
        Page<TicketActivityResponse> activities =
                ticketActivities.map(this::build);

        return new PageResponse<>(
                activities.getNumber(),
                activities.getSize(),
                activities.getTotalElements(),
                activities.getContent()
        );
    }
}
