package de.kingsware.md2thermal

import org.springframework.stereotype.Service
import java.io.File

@Service
class PrintService {

    private val printerName = System.getenv("PRINTER_NAME")
        ?: throw IllegalStateException("Umgebungvariable PRINTER_NAME ist nicht gesetzt.")

    fun printPdf(pdfFile: File) {
        try {
            val process = ProcessBuilder("lpr", "-P", printerName, pdfFile.absolutePath)
                .redirectErrorStream(true)
                .start()

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                val errorMsg = process.inputStream.bufferedReader().readText()
                throw RuntimeException("Fehler beim Drucken: $errorMsg")
            }
        } finally {
            // pdfFile.delete()
        }
    }
}
