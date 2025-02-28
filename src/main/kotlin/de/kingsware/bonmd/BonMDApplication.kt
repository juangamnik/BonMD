package de.kingsware.md2thermal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MD2ThermalApplication

fun main(args: Array<String>) {
    runApplication<MD2ThermalApplication>(*args)
}
