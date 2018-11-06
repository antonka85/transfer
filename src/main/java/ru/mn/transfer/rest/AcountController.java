package ru.mn.transfer.rest;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import ru.mn.transfer.db.Account;
import ru.mn.transfer.db.AccountService;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;

@Controller("/accounts")
public class AcountController {

    @Inject
    private AccountService accountService;

    @Get
    public List<Account> findAll() {
        return accountService.findAll();
    }

    @Get("/{id}")
    public Account findById(Long id) {
        return accountService.findById(id).orElse(null);
    }

    @Put("/{account_id}")
    public HttpResponse update(HttpRequest httpRequest, Long account_id, @Body UpdateAccountSumParam param) {
        accountService.update(account_id, param.getBalance());
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, httpRequest.getPath());
    }

    @Post(value = "/", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<Account> add(@Body AddAccountParam addAccountParam) {
        Account account = accountService.add(addAccountParam.getNumber());
        return HttpResponse
                .created(account)
                .headers(headers -> headers.location(URI.create("/accounts/" + account.getId())));
    }


}
