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
    private fun factoryStatementDTO(statement: List<Statement>): Mono<Map<String, Any>> {
        return Mono.fromCallable {
            val resume = object {
                val limite = statement[0].limit.toInt()
                val total = statement[0].balance.toInt()
                val data_extrato = LocalDateTime.now()
            }

            val response = mapOf("saldo" to resume, "ultimas_transacoes" to statement.map {
                object {
                    val valor = it.amount
                    val tipo = it.type?.lowercase()
                    val descricao = it.description
                    val realizada_em = it.transactionCreatedAt
                }
            })
            response
        }
    }

    @Transactional(readOnly = true)
    fun statement(customerId: Int): Mono<Map<String, Any>> {
        if (customerId > 5 || customerId < 1) return Mono.error(CustomerNotFoundException("Customer not found"))

        return transactionRepository.fetchStatementCustomer(customerId)
            .switchIfEmpty(Mono.error(CustomerNotFoundException("Customer not found")))
            .buffer(10)
            .flatMap(this::factoryStatementDTO)
            .single()
            .onErrorResume { Mono.empty() }
    }
}
