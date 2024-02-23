package org.example.rinhabackend2024kotlin

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@RestController
@RequestMapping(path = ["/clientes"])
class AccountController(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val fetchStatementUseCase: FetchStatementUseCase
) {
    @PostMapping(path = ["/{id}/transacoes"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createTransaction(@PathVariable id: Int, @RequestBody payload: CreateTransactionRequest): Mono<*> {
        return createTransactionUseCase.movement(payload, id)
            .subscribeOn(Schedulers.boundedElastic())
    }

    @GetMapping(path = ["/{id}/extrato"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun statement(@PathVariable id: Int): Mono<Statement> {
        return Mono.defer { fetchStatementUseCase.statement(id) }.subscribeOn(Schedulers.boundedElastic())
    }
}
