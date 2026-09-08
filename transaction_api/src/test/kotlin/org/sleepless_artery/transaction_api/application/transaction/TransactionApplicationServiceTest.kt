package org.sleepless_artery.transaction_api.application.transaction

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.sleepless_artery.transaction_api.domain.model.Currency
import org.sleepless_artery.transaction_api.domain.model.MerchantId
import org.sleepless_artery.transaction_api.domain.model.Money
import org.sleepless_artery.transaction_api.domain.model.Transaction
import org.sleepless_artery.transaction_api.domain.model.TransactionId
import org.sleepless_artery.transaction_api.domain.model.TransactionStatus
import org.sleepless_artery.transaction_api.domain.model.UserId
import org.sleepless_artery.transaction_api.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExtendWith(MockitoExtension::class)
class TransactionApplicationServiceTest {

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    private lateinit var transactionApplicationService: TransactionApplicationService

    @BeforeEach
    fun setUp() {
        transactionApplicationService =
            TransactionApplicationService(transactionRepository)
    }

    @Test
    fun `create should save transaction and return it`() {
        val command = CreateTransactionCommand(
            userId = UserId(UUID.randomUUID()),
            amount = BigDecimal("100.00"),
            currency = Currency.EUR,
            merchantId = MerchantId(UUID.randomUUID()),
            country = "fi"
        )

        val result = transactionApplicationService.create(command)

        assertEquals(command.userId, result.userId)
        assertEquals(command.amount, result.money.amount)
        assertEquals(command.currency, result.money.currency)
        assertEquals(command.merchantId, result.merchantId)
        assertEquals("FI", result.country)
        assertEquals(TransactionStatus.RECEIVED, result.status)

        verify(transactionRepository).save(result)
        verifyNoMoreInteractions(transactionRepository)
    }

    @Test
    fun `findById should return transaction when repository finds it`() {
        val id = TransactionId(UUID.randomUUID())

        val transaction = Transaction.rehydrate(
            id = id,
            userId = UserId(UUID.randomUUID()),
            money = Money(
                amount = BigDecimal("100.00"),
                currency = Currency.EUR
            ),
            merchantId = MerchantId(UUID.randomUUID()),
            country = "FI",
            status = TransactionStatus.RECEIVED,
            createdAt = Instant.parse("2026-01-01T12:00:00Z")
        )

        whenever(transactionRepository.findById(id))
            .thenReturn(transaction)

        val result = transactionApplicationService.findById(id)

        assertEquals(transaction, result)

        verify(transactionRepository).findById(id)
        verifyNoMoreInteractions(transactionRepository)
    }

    @Test
    fun `findById should return null when repository does not find transaction`() {
        val id = TransactionId(UUID.randomUUID())

        whenever(transactionRepository.findById(id))
            .thenReturn(null)

        val result = transactionApplicationService.findById(id)

        assertNull(result)

        verify(transactionRepository).findById(id)
        verifyNoMoreInteractions(transactionRepository)
    }
}