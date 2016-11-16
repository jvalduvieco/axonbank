package org.axonframework.sample.axonbank.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier
import javax.validation.constraints.Min

class CreateAccountCommand(
        @TargetAggregateIdentifier
        val accountId: String,
        @Min(value=0, message = "overdraft limit must be greater than 0")
        val overdraftLimit: Int)
class WithdrawMoneyCommand(
        @TargetAggregateIdentifier
        val accountId: String,
        val transactionId: String,
        @Min(value=0, message = "amount must be greater than 0")
        val amount:Int)
class DepositMoneyCommand(
        @TargetAggregateIdentifier
        val accountId: String,
        val transactionId: String,
        @Min(value=0, message = "amount must be greater than 0")
        val amount:Int)

class AccountCreatedEvent(val accountId: String, val overdraftLimit: Int)
class MoneyWithdrawnEvent(val accountId: String, val transactionId: String, val amount: Int, val balance:Int)
class MoneyDepositedEvent(accountId: String, val transactionId: String, amount: Int, val balance:Int)

class OverdraftLimitExceededException : Exception()