package org.sleepless_artery.transaction_api.domain.model

import java.util.UUID

@JvmInline
value class MerchantId(
    val value: UUID
) {
    companion object {
        fun from(value: String): MerchantId =
            MerchantId(UUID.fromString(value))
    }

    override fun toString(): String =
        value.toString()
}