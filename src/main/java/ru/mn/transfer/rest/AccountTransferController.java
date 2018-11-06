package ru.mn.transfer.rest;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import ru.mn.transfer.db.AccountService;
import ru.mn.transfer.db.AccountTransfer;
import ru.mn.transfer.db.AccountTransferService;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;

@Controller(AccountTransferController.LOCATION)
public class AccountTransferController {

    public static final String LOCATION = "/accounts/{accountId}/transfers";

    @Inject
    private AccountTransferService accountTransferService;

    @Get("/{transferId}")
    public AccountTransfer findById(Long accountId, Long transferId) {
        return accountTransferService.findById(accountId, transferId).orElse(null);
    }

    @Get
    public List<AccountTransfer> findAllTransfers(Long accountId) {
        return accountTransferService.findAllTransfer(accountId);
    }

    @Post(consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<AccountTransfer> execute(HttpRequest<?> httpRequest,
                                                 Long accountId, TransferParam transferAddCommand) {
        AccountTransfer accountTransfer = accountTransferService
                .execute(accountId, transferAddCommand.getDest(), transferAddCommand.getSum());
        return HttpResponse.created(accountTransfer)
                .headers(headers -> headers.location(URI.create(httpRequest.getPath() + "/" + accountTransfer.getId())));
    }
}
