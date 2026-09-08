package org.sleepless_artery.transaction_api.infrastructure.persistence

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.sleepless_artery.transaction_api.domain.model.Currency
import org.sleepless_artery.transaction_api.domain.model.MerchantId
import org.sleepless_artery.transaction_api.domain.model.Money
import org.sleepless_artery.transaction_api.domain.model.Transaction
import org.sleepless_artery.transaction_api.domain.model.TransactionId
import org.sleepless_artery.transaction_api.domain.model.UserId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.postgresql.PostgreSQLContainer
import java.math.BigDecimal
import java.time.temporal.ChronoUnit

@Testcontainers
@SpringBootTest
class JooqTransactionRepositoryIntegrationTest {

    @Autowired
    private lateinit var repository: JooqTransactionRepository

    @Test
    fun `save and findById should persist and restore transaction`() {
        // given
        val userId = UserId.from("11111111-1111-1111-1111-111111111111")
        val merchantId = MerchantId.from("22222222-2222-2222-2222-222222222222")

        val transaction = Transaction.create(
            userId = userId,
            money = Money(
                amount = BigDecimal("1500.50"),
                currency = Currency.EUR
            ),
            merchantId = merchantId,
            country = "fi"
        )

        // when
        repository.save(transaction)

        val restored = repository.findById(transaction.id)

        // then
        assertNotNull(restored)
        assertEquals(transaction.id, restored!!.id)
        assertEquals(transaction.userId, restored.userId)
        assertEquals(0, transaction.money.amount.compareTo(restored.money.amount))
        assertEquals(transaction.money.currency, restored.money.currency)
        assertEquals(transaction.merchantId, restored.merchantId)
        assertEquals(transaction.country, restored.country)
        assertEquals(transaction.status, restored.status)
        assertEquals(transaction.createdAt.truncatedTo(ChronoUnit.MICROS), restored.createdAt)
    }


    @Test
    fun `findById should return null when transaction does not exist`() {
        val id = TransactionId.generate()
        val result = repository.findById(id)
        assertEquals(null, result)
    }


    companion object {

        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:17")

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(
            registry: DynamicPropertyRegistry
        ) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }
}