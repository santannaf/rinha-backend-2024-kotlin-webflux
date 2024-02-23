package org.example.rinhabackend2024kotlin

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class TransactionPostgresRepository(
    private val r2dbcTemplate: R2dbcEntityTemplate
) : TransactionRepository {
    override fun createTransaction(event: Pair<Transaction, Account>, customerId: Int): Mono<Account> {
        return r2dbcTemplate.databaseClient.sql("insert into transactions (id, customer_id, type, amount, description, created_at, account_limit, balance) values (:id, :customerId, :type, :amount, :description, :createdAt, :limit, :balance)")
            .bindValues(
                mapOf(
                    "id" to event.first.id,
                    "customerId" to customerId,
                    "type" to event.first.type,
                    "amount" to event.first.amount,
                    "description" to event.first.description,
                    "createdAt" to event.first.createdAt,
                    "limit" to event.first.limit,
                    "balance" to event.first.balance,
                )
            )
            .fetch().rowsUpdated()
            .thenReturn(event.second)
    }

    override fun fetchLastTenTransactions(customerId: Int): Flux<Transaction> {
        return r2dbcTemplate.databaseClient.sql(
            """
            select 
                id, 
                type, 
                amount, 
                description, 
                created_At as "createdAt", 
                account_limit as "limit", 
                balance 
            from transactions T 
            where T.customer_id = :customerId 
            order by created_at desc limit 10
        """.trimIndent()
        )
            .bind("customerId", customerId)
            .mapProperties(Transaction::class.java)
            .all()
    }
}
