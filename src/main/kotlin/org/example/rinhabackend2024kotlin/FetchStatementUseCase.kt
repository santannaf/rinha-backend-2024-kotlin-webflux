package org.example.rinhabackend2024kotlin

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
@Transactional(propagation = Propagation.NEVER)
class FetchStatementUseCase(
    private val transactionRepository: TransactionRepository
) {
    val mapCustomersLimit = mapOf(1 to 1000 * 100, 2 to 800 * 100, 3 to 10000 * 100, 4 to 100000 * 100, 5 to 5000 * 100)

    private fun factoryStatementDTO(transactions: List<Transaction>, customerId: Int): Mono<Statement> {
        return Mono.fromCallable {
            if (transactions.isEmpty()) {
                val limit = mapCustomersLimit[customerId] ?: 0
                Statement(
                    balance = StatementBalance(
                        limit = limit,
                        total = 0,
                        consultAt = LocalDateTime.now()
                    ),
                    lastTransactions = emptyList()
                )
            } else {
                Statement(
                    balance = StatementBalance(
                        limit = transactions[0].limit,
                        total = transactions[0].balance,
                        consultAt = LocalDateTime.now()
                    ),
                    lastTransactions = transactions
                        .map {
                            StatementResumeTransaction(
                                value = it.amount,
                                type = it.type.lowercase(),
                                description = it.description,
                                transactionCreatedAt = it.createdAt
                            )
                        }
                )
            }
        }
    }

    fun statement(customerId: Int): Mono<Statement> {
        if (customerId > 5 || customerId < 1) return Mono.error(CustomerNotFoundException("Customer not found"))
        return transactionRepository.fetchLastTenTransactions(customerId)
            .collectList()
            .flatMapMany { transactions -> this.factoryStatementDTO(transactions, customerId) }
            .single()
            .onErrorResume { Mono.empty() }
    }
}
