package org.example.rinhabackend2024kotlin

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
class TransactionCustomConfig {
    @Bean
    fun transactionOperator(transactionManager: ReactiveTransactionManager): TransactionalOperator {
        return TransactionalOperator.create(transactionManager)
    }

    @Bean
    fun transactionManager(cf: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(cf)
    }
}
