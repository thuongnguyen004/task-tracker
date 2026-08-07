package vn.spring.task_tracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.services.TicketPriorityService;
import vn.spring.task_tracker.services.TicketStatusService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketStatusController {

    private final TicketStatusService ticketStatusService;

//    @GetMapping("/ticket-statuses")
//    public ResponseEntity<ApiResponse<List<TicketStatus>>> getTicketStatuses() {
//
//        List<TicketStatus> statuses =
//                ticketStatusService.getTicketStatuses();
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Get ticket statuses successfully", statuses)
//        );
//    }
}