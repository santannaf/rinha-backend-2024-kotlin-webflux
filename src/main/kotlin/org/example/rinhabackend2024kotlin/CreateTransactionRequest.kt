package org.example.rinhabackend2024kotlin

data class CreateTransactionRequest(
    val valor: String?,
    val tipo: String,
    val descricao: String?
) : TransactionRequest {
    override fun fetchAmount(): Long {
        if (valor.isNullOrEmpty() || valor.contains(".") || valor.toInt() < 0) throw InvalidInputException("Value invalid")
        return valor.toLong()
    }

    override fun fetchType(): String {
        if (tipo.uppercase() == "C" || tipo.uppercase() == "D") return TypeTransaction.valueOf(tipo.uppercase()).name
        throw InvalidInputException("Type transaction is invalid")
    }

    override fun fetchDescription(): String {
        if (descricao.isNullOrEmpty() || descricao.length > 10) throw InvalidInputException("Description very large or null")
        return descricao
    }
}
