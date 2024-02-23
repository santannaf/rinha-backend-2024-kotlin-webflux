package org.example.rinhabackend2024kotlin

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TransactionRepository {
    fun createTransaction(event: Pair<Transaction, Account>, customerId: Int): Mono<Account>
    fun fetchLastTenTransactions(customerId: Int): Flux<Transaction>
}
