package org.example.rinhabackend2024kotlin

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class AccountPostgresRepository(
    private val template: R2dbcEntityTemplate,
) : AccountRepository {
    override fun updateBalance(account: Long, customerId: Int): Mono<Account> {
        return template.databaseClient.sql("update account set balance = balance + :amount where customer_id = :customerId and balance + :amount + account_limit > 0")
            .bindValues(mapOf("amount" to account, "customerId" to customerId))
            .filter { f -> f.returnGeneratedValues("balance", "account_limit") }
            .map { row, _ -> Account(limit = row["account_limit"] as Int, balance = row["balance"] as Int) }
            .first()
    }
}
