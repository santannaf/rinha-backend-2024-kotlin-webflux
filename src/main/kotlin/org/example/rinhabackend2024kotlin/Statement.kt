package org.example.rinhabackend2024kotlin

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Statement(
    @JsonProperty("tipo") val type: String? = null,
    @JsonProperty("valor") val amount: Long? = null,
    @JsonProperty("descricao") val description: String? = null,
    @JsonProperty("realizada_em") val transactionCreatedAt: LocalDateTime? = null,
    @JsonProperty("limite") val limit: Long,
    @JsonProperty("saldo") val balance: Long
)
