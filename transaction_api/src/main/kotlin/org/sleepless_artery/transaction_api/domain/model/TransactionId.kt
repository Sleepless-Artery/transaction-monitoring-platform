package org.sleepless_artery.transaction_api.domain.model

import java.util.UUID

@JvmInline
value class TransactionId(
    val value: UUID
) {
    companion object {
        fun generate(): TransactionId =
            TransactionId(UUID.randomUUID())

        fun from(value: String): TransactionId =
            TransactionId(UUID.fromString(value))
    }

    override fun toString(): String =
        value.toString()
}