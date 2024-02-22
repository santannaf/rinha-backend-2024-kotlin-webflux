package org.example.rinhabackend2024kotlin

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
@Transactional(propagation = Propagation.NEVER)
class CreateTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    fun toEvent(request: TransactionRequest, account: Account): Mono<Pair<Transaction, Account>> {
        return Mono.fromCallable {
            Pair(
                Transaction(
                    amount = request.fetchAmount(),
                    type = TypeTransaction.valueOf(request.fetchType()).name,
                    description = request.fetchDescription()
                ), account
            )
        }
    }

    fun factoryResponse(account: Account): Map<String, Any> = mapOf("limite" to account.limit, "saldo" to account.balance)

    @Transactional
    fun movement(request: TransactionRequest, customerId: Int): Mono<*> {
        return Mono.fromCallable {
            if (request.fetchType() == "C") request.fetchAmount() else -request.fetchAmount()
        }
            .flatMap { amount -> accountRepository.updateBalance(amount, customerId) }
            .switchIfEmpty(Mono.error(BalanceInconsistencyException("Saldo menor que o limite estabelecido")))
            .flatMap { account -> toEvent(request, account) }
            .flatMap { event -> transactionRepository.createTransaction(event, customerId) }
            .map(this::factoryResponse)
            .subscribeOn(Schedulers.boundedElastic())
    }
}
