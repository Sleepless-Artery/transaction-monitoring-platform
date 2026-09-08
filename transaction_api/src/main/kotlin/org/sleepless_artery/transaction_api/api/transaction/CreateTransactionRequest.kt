package org.sleepless_artery.transaction_api.api.transaction

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.sleepless_artery.transaction_api.domain.model.Currency
import java.math.BigDecimal
import java.util.UUID

data class CreateTransactionRequest(

    @field:NotNull
    val userId: UUID?,

    @field:NotNull
    @field:DecimalMin(value = "0.01")
    @field:Digits(integer = 15, fraction = 4)
    val amount: BigDecimal?,

    @field:NotNull
    val currency: Currency?,

    @field:NotNull
    val merchantId: UUID?,

    @field:NotBlank
    val country: String?
)