package org.example.rinhabackend2024kotlin

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Statement(
    @JsonProperty("saldo") val balance: StatementBalance,
    @JsonProperty("ultimas_transacoes") val lastTransactions: List<StatementResumeTransaction>
)

data class StatementBalance(
    @JsonProperty("limite") val limit: Int,
    @JsonProperty("total") val total: Int,
    @JsonProperty("data_extrato") val consultAt: LocalDateTime
)

data class StatementResumeTransaction(
    @JsonProperty("valor") val value: Long,
    @JsonProperty("tipo") val type: String,
    @JsonProperty("descricao") val description: String,
    @JsonProperty("realizada_em") val transactionCreatedAt: LocalDateTime
)
