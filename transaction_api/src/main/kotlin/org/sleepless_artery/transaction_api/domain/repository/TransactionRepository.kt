package org.sleepless_artery.transaction_api.domain.repository

import org.sleepless_artery.transaction_api.domain.model.Transaction
import org.sleepless_artery.transaction_api.domain.model.TransactionId

interface TransactionRepository {

    fun save(transaction: Transaction)

    fun findById(id: TransactionId): Transaction?
}