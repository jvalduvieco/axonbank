package org.axonframework.sample.axonbank.MoneyTransfer

import org.axonframework.sample.axonbank.coreapi.MoneyTransferRequestedEvent
import org.axonframework.sample.axonbank.coreapi.WithdrawMoneyCommand
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
                .expectDispatchedCommandsEqualTo(WithdrawMoneyCommand("acc1", 100))
    }

}