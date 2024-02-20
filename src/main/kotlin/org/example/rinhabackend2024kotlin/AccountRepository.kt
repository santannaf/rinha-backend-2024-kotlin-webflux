package org.example.rinhabackend2024kotlin

import reactor.core.publisher.Mono

interface AccountRepository {
    fun updateBalance(account: Long, customerId: Int): Mono<Account>
}
