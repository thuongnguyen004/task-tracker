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
public class TicketPriorityController {

    private final TicketPriorityService ticketPriorityService;

//    @GetMapping("/ticket-priorities")
//    public ResponseEntity<ApiResponse<List<TicketPriority>>> getTicketPriorities() {
//
//        List<TicketPriority> priorities =
//                ticketPriorityService.getTicketPriorities();
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Get ticket priorities successfully", priorities)
//        );
//    }
}