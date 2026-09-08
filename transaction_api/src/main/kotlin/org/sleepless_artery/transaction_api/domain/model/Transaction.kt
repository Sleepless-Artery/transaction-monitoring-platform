package org.sleepless_artery.transaction_api.domain.model

import java.time.Instant

class Transaction private constructor(
    val id: TransactionId,
    val userId: UserId,
    val money: Money,
    val merchantId: MerchantId,
    val country: String,
    val status: TransactionStatus,
    val createdAt: Instant
) {

    companion object {

        fun create(
            userId: UserId,
            money: Money,
            merchantId: MerchantId,
            country: String,
            createdAt: Instant = Instant.now()
        ): Transaction {
            val normalizedCountry = country.uppercase()

            require(normalizedCountry.length == 2) {
                "Country must contain exactly 2 characters"
            }

            require(normalizedCountry.all { it in 'A'..'Z' }) {
                "Country must contain only uppercase Latin letters"
            }

            return Transaction(
                id = TransactionId.generate(),
                userId = userId,
                money = money,
                merchantId = merchantId,
                country = normalizedCountry,
                status = TransactionStatus.RECEIVED,
                createdAt = createdAt
            )
        }

        internal fun rehydrate(
            id: TransactionId,
            userId: UserId,
            money: Money,
            merchantId: MerchantId,
            country: String,
            status: TransactionStatus,
            createdAt: Instant
        ): Transaction =
            Transaction(
                id = id,
                userId = userId,
                money = money,
                merchantId = merchantId,
                country = country,
                status = status,
                createdAt = createdAt
            )
    }
}