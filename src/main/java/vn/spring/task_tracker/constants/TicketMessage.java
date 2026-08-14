package vn.spring.task_tracker.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TicketMessage {
    public static final String CREATE_SUCCESS = "Create ticket successfully";
    public static final String GET_BY_ID_SUCCESS = "Get ticket";
    public static final String GET_BY_CODE_SUCCESS = "Get ticket by code";
    public static final String GET_ALL_ACTIVE_SUCCESS = "Get active tickets successfully";
    public static final String UPDATE_SUCCESS = "Update ticket successfully";
    public static final String CHANGE_SUCCESS = "Change ticket successfully";

    public static final String NOT_FOUND = "Ticket not found.";
}
