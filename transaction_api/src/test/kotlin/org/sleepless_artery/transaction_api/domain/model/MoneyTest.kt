package org.sleepless_artery.transaction_api.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import java.math.BigDecimal

class MoneyTest {

    @Test
    fun `should create money with positive amount`() {
        val money = Money(
            amount = BigDecimal("100.50"),
            currency = Currency.EUR
        )

        assertEquals(BigDecimal("100.50"), money.amount)
        assertEquals(Currency.EUR, money.currency)
    }

    @Test
    fun `should reject zero amount`() {
        assertFailsWith<IllegalArgumentException> {
            Money(
                amount = BigDecimal.ZERO,
                currency = Currency.EUR
            )
        }
    }

    @Test
    fun `should reject negative amount`() {
        assertFailsWith<IllegalArgumentException> {
            Money(
                amount = BigDecimal("-1.00"),
                currency = Currency.EUR
            )
        }
    }

    @Test
    fun `should reject amount with more than four decimal places`() {
        assertFailsWith<IllegalArgumentException> {
            Money(
                amount = BigDecimal("10.12345"),
                currency = Currency.EUR
            )
        }
    }
}