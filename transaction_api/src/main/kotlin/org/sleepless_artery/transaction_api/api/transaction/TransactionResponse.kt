package org.sleepless_artery.transaction_api.api.transaction

import org.sleepless_artery.transaction_api.domain.model.Transaction
import org.sleepless_artery.transaction_api.domain.model.TransactionStatus
import java.time.Instant
import java.util.UUID

data class TransactionResponse(
    val transactionId: UUID,
    val status: TransactionStatus,
    val createdAt: Instant
) {
    companion object {
        fun from(transaction: Transaction): TransactionResponse =
            TransactionResponse(
                transactionId = transaction.id.value,
                status = transaction.status,
                createdAt = transaction.createdAt
            )
    }
}