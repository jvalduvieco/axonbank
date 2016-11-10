package org.axonframework.sample.axonbank.account

import org.axonframework.sample.axonbank.coreapi.*
import org.axonframework.test.FixtureConfiguration
import org.axonframework.test.Fixtures
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AccountTest {
    private var fixture: FixtureConfiguration<Account> =  Fixtures.newGivenWhenThenFixture(Account::class.java)

    @Before
    fun setUp() {
    }
    @Test
    fun testCreateAccount() {
        fixture.givenNoPriorActivity()
                .`when`(CreateAccountCommand("1234", 1000))
                .expectEvents(AccountCreatedEvent("1234", 1000))
    }

    @Test
    fun testWithdrawReasonableAmount() {
        fixture.given(AccountCreatedEvent("1234", 1000))
                .`when`(WithdrawMoneyCommand("1234", 600))
                .expectEvents(MoneyWithdrawnEvent("1234", 600, -600))
    }

    @Test
    fun testWithdrawAbsurdAmount() {
        fixture.given(AccountCreatedEvent("1234", 1000))
                .`when`(WithdrawMoneyCommand("1234", 1001))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException::class.java)
    }

    @Test
    fun WithdrawTwice() {
        fixture.given(AccountCreatedEvent("1234", 1000),
                MoneyWithdrawnEvent("1234", 999, -999))
                .`when`(WithdrawMoneyCommand("1234", 2))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException::class.java)
    }

    @Test
    fun DepositMoney(){
        fixture.given(AccountCreatedEvent("1234", 1000))
                .`when`(DepositMoneyCommand("1234", 1000))
                .expectEvents(MoneyDepositedEvent("1234", 1000, 1000))
    }
    @Test
    fun DepositMoneyTwice(){
        fixture.given(AccountCreatedEvent("1234", 1000),
                MoneyDepositedEvent("1234", 1000, 1000))
                .`when`(DepositMoneyCommand("1234", 1000))
                .expectEvents(MoneyDepositedEvent("1234", 1000, 2000))
    }
}