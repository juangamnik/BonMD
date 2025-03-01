package de.kingsware.bonmd

import org.springframework.stereotype.Service
import java.io.File

@Service
class PrintService {

    private val printerName = System.getenv("PRINTER_NAME")
        ?: throw IllegalStateException("Environment variable PRINTER_NAME not set.")

    fun printPdf(pdfFile: File) {
        val process = ProcessBuilder("lpr", "-P", printerName, pdfFile.absolutePath)
            .redirectErrorStream(true)
            .start()

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            val errorMsg = process.inputStream.bufferedReader().readText()
            throw RuntimeException("Error during printing: $errorMsg")
        }
    }
}
