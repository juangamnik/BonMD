package de.kingsware.bonmd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BonMDApplication

fun main(args: Array<String>) {
    runApplication<BonMDApplication>(*args)
}
