package org.sleepless_artery.transaction_api.domain.model

import java.util.UUID

@JvmInline
value class UserId(
    val value: UUID
) {
    companion object {
        fun from(value: String): UserId =
            UserId(UUID.fromString(value))
    }

    override fun toString(): String =
        value.toString()
}