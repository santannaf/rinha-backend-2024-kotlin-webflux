package org.example.rinhabackend2024kotlin

import com.fasterxml.jackson.annotation.JsonProperty

data class Account(
    @JsonProperty("limite") val limit: Int,
    @JsonProperty("saldo") val balance: Int
)
