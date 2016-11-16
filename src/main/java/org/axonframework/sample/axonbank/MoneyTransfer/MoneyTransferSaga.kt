package org.axonframework.sample.axonbank.MoneyTransfer

import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage
import org.axonframework.eventhandling.saga.EndSaga
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.axonframework.eventhandling.saga.SagaLifecycle
import org.axonframework.eventhandling.saga.StartSaga
import org.axonframework.sample.axonbank.coreapi.*
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

@Saga
class MoneyTransferSaga {
    @Transient private var commandBus: CommandBus? = null

    @Autowired
    fun setCommandBus(commandBus: CommandBus) {
        this.commandBus = commandBus
    }
    private var targetAccount: String? = null

    @StartSaga
    @SagaEventHandler(associationProperty = "transferId")
    fun on(event: MoneyTransferRequestedEvent){
        targetAccount = event.targetAccount
        commandBus!!.dispatch(asCommandMessage<WithdrawMoneyCommand>(
                WithdrawMoneyCommand(
                        event.sourceAccount,
                        event.transferId,
                        event.amount)))
    }
    @SagaEventHandler(associationProperty = "transactionId", keyName = "transferId")
    fun on(event: MoneyWithdrawnEvent){
        commandBus!!.dispatch(asCommandMessage<DepositMoneyCommand>(
                DepositMoneyCommand(
                        targetAccount!!,
                        event.transactionId,
                        event.amount)))
    }
    @SagaEventHandler(associationProperty = "transactionId", keyName = "transferId")
    fun on(event: MoneyDepositedEvent) {
        commandBus!!.dispatch(asCommandMessage<CompleteMoneyTransferCommand>(
                CompleteMoneyTransferCommand(event.transactionId)))
    }
    @EndSaga
    @SagaEventHandler(associationProperty = "transferId")
    fun on(event: MoneyTransferCompletedEvent){
        SagaLifecycle.end() //Not necessary as there is the annotation. This is used in conditional end
    }
    @EndSaga
    @SagaEventHandler(associationProperty = "transferId")
    fun on(event: MoneyTransferCancelledEvent){
        SagaLifecycle.end()
    }
}