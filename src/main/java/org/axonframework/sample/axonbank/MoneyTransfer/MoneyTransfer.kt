package org.axonframework.sample.axonbank.MoneyTransfer

import lombok.NoArgsConstructor
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.sample.axonbank.coreapi.*
import org.axonframework.spring.stereotype.Aggregate


@NoArgsConstructor
@Aggregate
class MoneyTransfer @CommandHandler constructor(command: MoneyTransferCommand) {
    @AggregateIdentifier
    private var transferId: String? = null

    init {
        apply(MoneyTransferRequestedEvent(command.transferId, command.sourceAccount, command.targetAccount, command.amount))
    }
    @CommandHandler
    fun handle(command: CompleteMoneyTransferCommand) {
        apply(MoneyTransferCompletedEvent(command.transferId))
    }
    @CommandHandler
    fun handle(command: CancelMoneyTransferCommand) {
        apply(MoneyTransferCancelledEvent(command.transferId))
    }
    @EventSourcingHandler
    fun on(event: MoneyTransferRequestedEvent) {
        transferId = event.transferId
    }

    @EventSourcingHandler
    fun on(event: MoneyTransferCancelledEvent) {
        markDeleted()
    }
}