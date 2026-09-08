package org.sleepless_artery.transactionapi.domain.model

import org.sleepless_artery.transaction_api.domain.model.Currency
import org.sleepless_artery.transaction_api.domain.model.MerchantId
import org.sleepless_artery.transaction_api.domain.model.Money
import org.sleepless_artery.transaction_api.domain.model.Transaction
import org.sleepless_artery.transaction_api.domain.model.TransactionStatus
import org.sleepless_artery.transaction_api.domain.model.UserId
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TransactionTest {

    @Test
    fun `should create transaction`() {
        val createdAt = Instant.parse("2026-01-01T12:00:00Z")

        val transaction = Transaction.create(
            userId = UserId.from("11111111-1111-1111-1111-111111111111"),
            money = Money(
                amount = BigDecimal("100.00"),
                currency = Currency.EUR
            ),
            merchantId = MerchantId.from("22222222-2222-2222-2222-222222222222"),
            country = "fi",
            createdAt = createdAt
        )

        assertEquals("FI", transaction.country)
        assertEquals(TransactionStatus.RECEIVED, transaction.status)
        assertEquals(createdAt, transaction.createdAt)
    }

    @Test
    fun `should reject invalid country`() {
        assertFailsWith<IllegalArgumentException> {
            Transaction.create(
                userId = UserId.from("11111111-1111-1111-1111-111111111111"),
                money = Money(
                    amount = BigDecimal("100.00"),
                    currency = Currency.EUR
                ),
                merchantId = MerchantId.from("22222222-2222-2222-2222-222222222222"),
                country = "FIN"
            )
        }
    }

    @Test
    fun `should reject non latin country`() {
        assertFailsWith<IllegalArgumentException> {
            Transaction.create(
                userId = UserId.from("11111111-1111-1111-1111-111111111111"),
                money = Money(
                    amount = BigDecimal("100.00"),
                    currency = Currency.EUR
                ),
                merchantId = MerchantId.from("22222222-2222-2222-2222-222222222222"),
                country = "12"
            )
        }
    }
}