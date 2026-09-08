package org.sleepless_artery.transaction_api.domain.model

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: Currency
) {

    init {
        require(amount > BigDecimal.ZERO) {
            "Money amount must be greater than zero"
        }

        require(amount.scale() <= MAX_SCALE) {
            "Money amount must have at most $MAX_SCALE decimal places"
        }

        require(amount.precision() <= MAX_PRECISION) {
            "Money amount must have at most $MAX_PRECISION digits"
        }
    }

    companion object {
        private const val MAX_PRECISION = 19
        private const val MAX_SCALE = 4
    }
}