package org.axonframework.sample.axonbank.coreapi

class MoneyTransferCancelledEvent(
        val transferId: String)

class CancelMoneyTransferCommand (
        val transferId: String)

class MoneyTransferCompletedEvent(
        val transferId: String)

class CompleteMoneyTransferCommand(
        val transferId: String)

class MoneyTransferRequestedEvent(
        val transferId: String,
        val sourceAccount: String,
        val targetAccount: String,
        val amount: Int)

class MoneyTransferCommand (
        val transferId: String,
        val sourceAccount: String,
        val targetAccount: String,
        val amount: Int)