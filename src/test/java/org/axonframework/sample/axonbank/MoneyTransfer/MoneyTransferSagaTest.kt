package org.axonframework.sample.axonbank.MoneyTransfer

import org.axonframework.sample.axonbank.coreapi.*
import org.axonframework.test.saga.AnnotatedSagaTestFixture
import org.junit.Before
import org.junit.Test

class MoneyTransferSagaTest {
    private val  fixture: AnnotatedSagaTestFixture<MoneyTransferSaga> =
            AnnotatedSagaTestFixture<MoneyTransferSaga>(MoneyTransferSaga::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun testMoneyTransferRequest() {
        fixture.givenNoPriorActivity()
                .whenPublishingA(MoneyTransferRequestedEvent("tf1", "acc1", "acc2", 100))
                .expectActiveSagas(1)
                .expectDispatchedCommandsEqualTo(WithdrawMoneyCommand("acc1", "tf1", 100))
    }
    @Test
    fun testTooMuchMoneyTransferRequest() {
        fixture.givenNoPriorActivity()
                .whenPublishingA(MoneyTransferRequestedEvent("tf1", "acc1", "acc2", 10000))
    }
    @Test
    fun testDepositMoneyAfterWithdrawal() {
        fixture.givenAPublished(MoneyTransferRequestedEvent("tf1", "acct1", "acct2", 100))
                .whenPublishingA(MoneyWithdrawnEvent("acct1", "tf1", 100, 500))
                .expectDispatchedCommandsEqualTo(DepositMoneyCommand("acct2", "tf1", 100))
    }
    @Test
    fun testTransferCompletedAfterDeposit(){
        fixture.givenAPublished(MoneyTransferRequestedEvent("tf1", "acct1", "acct2", 100))
                .andThenAPublished(MoneyWithdrawnEvent("acct1", "tf1", 100, 500))
                .whenPublishingA(MoneyDepositedEvent("acct2", "tf1", 100,400))
                .expectDispatchedCommandsEqualTo(CompleteMoneyTransferCommand("tf1"))
    }
    @Test
    fun testSagaEndsAfterTransactionCompleted(){
        fixture.givenAPublished(MoneyTransferRequestedEvent("tf1", "acct1", "acct2", 100))
                .andThenAPublished(MoneyWithdrawnEvent("acct1", "tf1", 100, 500))
                .andThenAPublished(MoneyDepositedEvent("acct2", "tf1", 100, 400))
                .whenPublishingA(MoneyTransferCompletedEvent("tf1"))
                .expectActiveSagas(0)
                .expectNoDispatchedCommands()
    }
}