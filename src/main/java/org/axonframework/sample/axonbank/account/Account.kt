package org.axonframework.sample.axonbank.account

import lombok.NoArgsConstructor
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.sample.axonbank.coreapi.*
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
@NoArgsConstructor
class Account {
    @AggregateIdentifier
    private var  accountId: String? = null
    private var overdraftLimit: Int = 0
    private var  balance: Int = 0
    constructor() {

    }

    @CommandHandler
    constructor(command: CreateAccountCommand) {
        apply(AccountCreatedEvent(command.accountId, command.overdraftLimit))
    }

    @CommandHandler
    fun handle(command: WithdrawMoneyCommand){
        if (balance + overdraftLimit >=command.amount){
            AggregateLifecycle.apply(MoneyWithdrawnEvent(accountId!!, command.transactionId, command.amount, balance - command.amount))
        } else
            throw OverdraftLimitExceededException()
    }
    @CommandHandler
    fun handle(command: DepositMoneyCommand){
        AggregateLifecycle.apply(MoneyDepositedEvent(accountId!!, command.transactionId, command.amount, balance + command.amount))
    }
    @EventSourcingHandler
    fun on(event: AccountCreatedEvent) {
        accountId = event.accountId
        overdraftLimit = event.overdraftLimit
    }
    @EventSourcingHandler
    fun on(event: MoneyWithdrawnEvent){
        balance = event.balance
    }
    @EventSourcingHandler
    fun on(event: MoneyDepositedEvent) {
        balance = event.balance
    }
}
