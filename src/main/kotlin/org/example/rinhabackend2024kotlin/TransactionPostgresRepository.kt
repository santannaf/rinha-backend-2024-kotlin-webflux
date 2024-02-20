package org.example.rinhabackend2024kotlin

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
class TransactionPostgresRepository(
    private val r2dbcTemplate: R2dbcEntityTemplate
) : TransactionRepository {
    override fun createTransaction(event: Pair<Transaction, Account>, customerId: Int): Mono<Account> {
        return r2dbcTemplate.databaseClient.sql("insert into transactions (id, customer_id, type, amount, description, created_at) values (:id, :customerId, :type, :amount, :description, :createdAt)")
            .bindValues(
                mapOf(
                    "id" to event.first.id,
                    "customerId" to customerId,
                    "type" to event.first.type,
                    "amount" to event.first.amount,
                    "description" to event.first.description,
                    "createdAt" to event.first.createdAt
                )
            )
            .fetch().rowsUpdated()
            .publishOn(Schedulers.boundedElastic())
            .thenReturn(event.second)
    }

    override fun fetchStatementCustomer(customerId: Int): Flux<Statement> {
        return r2dbcTemplate.databaseClient.sql(
            """
            select
                A.account_limit as "limit",
                A.balance,
                T.id,
                T.customer_id as customerId,
                T.type,
                T.amount,
                T.description,
                T.created_at as transactionCreatedAt
            from account A
            left join transactions T on T.customer_id = A.customer_id
            where A.customer_id = :customerId
            order by T.created_at desc limit 10
            """.trimIndent()
        )
            .bind("customerId", customerId)
            .mapProperties(Statement::class.java)
            .all()
            .publishOn(Schedulers.boundedElastic())
    }
}
