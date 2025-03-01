package de.kingsware.bonmd

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
        val tempPdfFile = File.createTempFile("bonmd", ".pdf") // Create a temporary PDF file
        try {
            pdfService.convertMarkdownToPdf(markdownContent, tempPdfFile)
            printService.printPdf(tempPdfFile)
        }
        finally {
            tempPdfFile.delete()
        }
    }
}
