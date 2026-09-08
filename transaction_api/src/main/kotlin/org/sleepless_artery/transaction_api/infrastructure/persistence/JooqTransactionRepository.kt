package org.sleepless_artery.transaction_api.infrastructure.persistence

import org.jooq.DSLContext
import org.sleepless_artery.transaction_api.domain.model.Currency
import org.sleepless_artery.transaction_api.domain.model.MerchantId
import org.sleepless_artery.transaction_api.domain.model.Money
import org.sleepless_artery.transaction_api.domain.model.Transaction
import org.sleepless_artery.transaction_api.domain.model.TransactionId
import org.sleepless_artery.transaction_api.domain.model.TransactionStatus
import org.sleepless_artery.transaction_api.domain.model.UserId
import org.sleepless_artery.transaction_api.domain.repository.TransactionRepository
import org.sleepless_artery.transaction_api.infrastructure.persistence.jooq.generated.tables.records.TransactionsRecord
import org.sleepless_artery.transaction_api.infrastructure.persistence.jooq.generated.tables.references.TRANSACTIONS
import org.springframework.stereotype.Repository
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Repository
class JooqTransactionRepository(
    private val dsl: DSLContext
) : TransactionRepository {

    override fun save(transaction: Transaction) {
        dsl.insertInto(TRANSACTIONS)
            .set(transaction.toRecord())
            .execute()
    }

    override fun findById(id: TransactionId): Transaction? {
        return dsl.selectFrom(TRANSACTIONS)
            .where(TRANSACTIONS.ID.eq(id.value))
            .fetchOne()
            ?.toDomain()
    }



    private fun Transaction.toRecord(): TransactionsRecord =
        TransactionsRecord(
            id = id.value,
            userId = userId.value,
            amount = money.amount,
            currency = money.currency.name,
            merchantId = merchantId.value,
            country = country,
            status = status.name,
            createdAt = createdAt
                .truncatedTo(ChronoUnit.MICROS)
                .atOffset(ZoneOffset.UTC)
        )


    private fun TransactionsRecord.toDomain(): Transaction =
        Transaction.rehydrate(
            id = TransactionId(id),
            userId = UserId(userId),
            money = Money(
                amount = amount,
                currency = Currency.valueOf(currency)
            ),
            merchantId = MerchantId(merchantId),
            country = country,
            status = TransactionStatus.valueOf(status),
            createdAt = createdAt.toInstant()
        )
}