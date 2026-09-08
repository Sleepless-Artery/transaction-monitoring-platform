package org.sleepless_artery.transaction_api.application.transaction

import org.sleepless_artery.transaction_api.domain.model.Currency
import org.sleepless_artery.transaction_api.domain.model.MerchantId
import org.sleepless_artery.transaction_api.domain.model.UserId
import java.math.BigDecimal

data class CreateTransactionCommand(
    val userId: UserId,
    val amount: BigDecimal,
    val currency: Currency,
    val merchantId: MerchantId,
    val country: String
)