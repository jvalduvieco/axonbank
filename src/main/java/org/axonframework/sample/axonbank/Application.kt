package org.axonframework.sample.axonbank

import org.axonframework.commandhandling.AsynchronousCommandBus
import org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage
import org.axonframework.config.Configuration
import org.axonframework.config.DefaultConfigurer
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.sample.axonbank.account.Account
import org.axonframework.sample.axonbank.coreapi.CreateAccountCommand
import org.axonframework.sample.axonbank.coreapi.WithdrawMoneyCommand

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>): Unit {
            val config: Configuration = DefaultConfigurer.defaultConfiguration()
                    .configureAggregate(Account::class.java)
                    .configureEmbeddedEventStore { c -> InMemoryEventStorageEngine() }
                    .buildConfiguration()
            config.start()
            config.commandBus().dispatch<CreateAccountCommand>(asCommandMessage(CreateAccountCommand("4321", 500)))
            config.commandBus().dispatch<WithdrawMoneyCommand>(asCommandMessage(WithdrawMoneyCommand("4321", 250)))
        }
    }
}