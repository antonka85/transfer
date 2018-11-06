package ru.mn.transfer;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.mn.transfer.db.Account;
import ru.mn.transfer.db.AccountTransfer;
import ru.mn.transfer.rest.AddAccountParam;
import ru.mn.transfer.rest.TransferParam;
import ru.mn.transfer.rest.UpdateAccountSumParam;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransferTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext
                .build("transfer")
                .run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void testTransferOperation() {
        HttpRequest<AddAccountParam> srcAccountReq = HttpRequest.POST("/accounts", new AddAccountParam("111"));
        HttpResponse<Account> srcResponse = client.toBlocking().exchange(srcAccountReq, Account.class);
        Account srcAccount = srcResponse.getBody().orElse(null);
        assertEquals(HttpStatus.CREATED, srcResponse.getStatus());
        assertNotNull(srcAccount);

        HttpRequest<UpdateAccountSumParam> setBalanceReq = HttpRequest.PUT("/accounts/" + srcAccount.getId(),
                new UpdateAccountSumParam(new BigDecimal(1000)));
        client.toBlocking().exchange(setBalanceReq);

        HttpRequest<AddAccountParam> destAccountReq = HttpRequest.POST("/accounts", new AddAccountParam("222"));
        HttpResponse<Account> destResponse = client.toBlocking().exchange(destAccountReq, Account.class);
        Account destAccount = destResponse.getBody().orElse(null);
        assertEquals(HttpStatus.CREATED, destResponse.getStatus());
        assertNotNull(destAccount);

        HttpRequest<TransferParam> transferReq = HttpRequest.POST(String.format("/accounts/%d/transfers", srcAccount.getId()),
                new TransferParam(destAccount.getId(), new BigDecimal(100)));
        HttpResponse<AccountTransfer> transferResp = client.toBlocking().exchange(transferReq, AccountTransfer.class);
        assertEquals(HttpStatus.CREATED, destResponse.getStatus());
        assertEquals(0, transferResp.body().getSum().compareTo(new BigDecimal(-100)));


        srcAccountReq = HttpRequest.GET("/accounts/" + srcAccount.getId());
        srcResponse = client.toBlocking().exchange(srcAccountReq, Account.class);
        srcAccount = srcResponse.getBody().orElse(null);
        assertNotNull(srcAccount);
        assertEquals(0, srcAccount.getBalance().compareTo(new BigDecimal(900)));

    }
}
