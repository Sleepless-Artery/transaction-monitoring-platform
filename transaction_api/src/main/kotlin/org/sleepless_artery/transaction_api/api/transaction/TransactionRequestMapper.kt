package org.sleepless_artery.transaction_api.api.transaction

import org.sleepless_artery.transaction_api.application.transaction.CreateTransactionCommand
import org.sleepless_artery.transaction_api.domain.model.MerchantId
import org.sleepless_artery.transaction_api.domain.model.UserId

object TransactionRequestMapper {
    fun toCommand(
        request: CreateTransactionRequest
    ): CreateTransactionCommand =
        CreateTransactionCommand(
            userId = UserId(request.userId!!),
            amount = request.amount!!,
            currency = request.currency!!,
            merchantId = MerchantId(request.merchantId!!),
            country = request.country!!
        )
}