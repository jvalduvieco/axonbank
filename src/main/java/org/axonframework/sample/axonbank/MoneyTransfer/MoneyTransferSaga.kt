package org.axonframework.sample.axonbank.MoneyTransfer

import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.axonframework.eventhandling.saga.StartSaga
import org.axonframework.sample.axonbank.coreapi.MoneyTransferRequestedEvent
import org.axonframework.sample.axonbank.coreapi.WithdrawMoneyCommand
import org.springframework.beans.factory.annotation.Autowired


class MoneyTransferSaga {
    @Transient private var commandBus: CommandBus? = null

    @Autowired
    fun setCommandBus(commandBus: CommandBus) {
        this.commandBus = commandBus
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "transferId")
    fun on(event: MoneyTransferRequestedEvent){
        commandBus!!.dispatch(asCommandMessage<WithdrawMoneyCommand>(
                WithdrawMoneyCommand(
                        event.sourceAccount,
                        event.amount)))
    }

}