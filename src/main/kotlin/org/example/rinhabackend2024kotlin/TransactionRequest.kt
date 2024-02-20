package org.example.rinhabackend2024kotlin

interface TransactionRequest {
    fun fetchAmount(): Long
    fun fetchType(): String
    fun fetchDescription(): String
}
