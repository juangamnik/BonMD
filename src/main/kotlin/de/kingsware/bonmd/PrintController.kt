package de.kingsware.md2thermal

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.io.File

@RestController
@RequestMapping("/api")
class PrintController(
    private val pdfService: PdfService,
    private val printService: PrintService
) {

    @PostMapping("/printMarkdown")
    @ResponseStatus(HttpStatus.OK)
    fun printMarkdown(@RequestBody markdownContent: String) {
        val pdfFile: File = pdfService.convertMarkdownToPdf(markdownContent)
        printService.printPdf(pdfFile)
    }
}
