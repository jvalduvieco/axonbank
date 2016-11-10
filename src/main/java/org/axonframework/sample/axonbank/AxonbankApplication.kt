package org.axonframework.sample.axonbank

import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.spring.config.EnableAxon
import org.axonframework.spring.config.EnableAxonAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@EnableAxon
@SpringBootApplication
open class AxonbankApplication {
    @Bean
    open fun eventStoreEngine(): EventStorageEngine {
        return InMemoryEventStorageEngine()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(AxonbankApplication::class.java, *args)
        }
    }

}
