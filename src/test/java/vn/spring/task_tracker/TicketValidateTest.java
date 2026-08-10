package vn.spring.task_tracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.spring.task_tracker.mappers.TicketCreateMapper;
import vn.spring.task_tracker.repositories.TicketRepository;
import vn.spring.task_tracker.services.impl.TicketServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class TicketValidateTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketCreateMapper ticketMapper;

    @Mock
    private TicketServiceImpl ticketService;

    @Test
    void giveCreateTicketSuccessfullyWithRequiredFieldsOnly() {


    }
}
