package vn.spring.task_tracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.task_tracker.constants.TicketActitivtyMessage;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.PageResponse;
import vn.spring.task_tracker.dtos.responses.TicketActivityResponse;
import vn.spring.task_tracker.entities.TicketActivity;
import vn.spring.task_tracker.mappers.TicketActivityResponseMapper;
import vn.spring.task_tracker.services.TicketActivityService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket-activities")
public class TicketActivityController {

    private final TicketActivityService ticketActivityService;

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<PageResponse<TicketActivityResponse>>> getTicketActivityByTicketCode(@PathVariable String code, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Page<TicketActivity> ticketActivities = ticketActivityService.getTicketActivityByTicketCode(code, page, size);

        PageResponse<TicketActivityResponse> response = new TicketActivityResponseMapper().buildList(ticketActivities);

        return ResponseEntity.ok(ApiResponse.success(TicketActitivtyMessage.GET_BY_ID_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TicketActivityResponse>>> getTicketActivity(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Page<TicketActivity> ticketActivities = ticketActivityService.getTicketActivity(page, size);

        PageResponse<TicketActivityResponse> response = new TicketActivityResponseMapper().buildList(ticketActivities);

        return ResponseEntity.ok(ApiResponse.success(TicketActitivtyMessage.GET_ALL_SUCCESS, response));
    }
}
