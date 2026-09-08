package org.sleepless_artery.transaction_api.api.transaction

import jakarta.validation.Valid
import org.sleepless_artery.transaction_api.application.transaction.TransactionApplicationService
import org.sleepless_artery.transaction_api.domain.model.TransactionId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionApplicationService: TransactionApplicationService
) {

    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateTransactionRequest
    ): ResponseEntity<TransactionResponse> {

        val transaction = transactionApplicationService.create(
            TransactionRequestMapper.toCommand(request)
        )

        return ResponseEntity
            .status(201)
            .body(TransactionResponse.from(transaction))
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: UUID
    ): ResponseEntity<TransactionResponse> {

        val transaction = transactionApplicationService.findById(
            TransactionId(id)
        ) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(
            TransactionResponse.from(transaction)
        )
    }
}