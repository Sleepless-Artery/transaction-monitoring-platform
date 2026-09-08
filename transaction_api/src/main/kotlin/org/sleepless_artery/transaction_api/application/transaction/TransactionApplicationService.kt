package org.sleepless_artery.transaction_api.application.transaction

import org.sleepless_artery.transaction_api.domain.model.Money
import org.sleepless_artery.transaction_api.domain.model.Transaction
import org.sleepless_artery.transaction_api.domain.model.TransactionId
import org.sleepless_artery.transaction_api.domain.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionApplicationService(
    private val transactionRepository: TransactionRepository
) {

    @Transactional
    fun create(command: CreateTransactionCommand): Transaction {
        val transaction = Transaction.create(
            userId = command.userId,
            money = Money(
                amount = command.amount,
                currency = command.currency
            ),
            merchantId = command.merchantId,
            country = command.country
        )

        transactionRepository.save(transaction)

        return transaction
    }

    @Transactional(readOnly = true)
    fun findById(id: TransactionId): Transaction? =
        transactionRepository.findById(id)
}