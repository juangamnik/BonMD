package de.kingsware.md2thermal

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream

@Service
class PdfService {

    fun convertMarkdownToPdf(mdContent: String): File {
        // Markdown zu HTML konvertieren
        val parser = Parser.builder().build()
        val renderer = HtmlRenderer.builder().build()
        val document = parser.parse(mdContent)
        val htmlContent = renderer.render(document)

        // **📌 XHTML-konformes HTML für OpenHTMLtoPDF mit Monospace-Font**
        val styledHtml = """
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <meta charset="UTF-8"/>
                <style>
                    @page {
                        size: 80mm auto; /* Breite fixiert, Höhe automatisch */
                        margin: 0;
                    }
                    body {
                        width: 72mm;
                        font-size: 11px;
                        font-family: monospace;
                        word-wrap: break-word;
                        margin: 0;
                        padding: 4mm;
                    }
                    h1 {
                        font-size: 14px;
                        font-weight: bold;
                    }
                    h2 {
                        font-size: 13px;
                        font-weight: bold;
                    }
                    h3 {
                        font-size: 12px;
                        font-weight: bold;
                    }
                    p {
                        margin: 0 0 5mm 0;
                    }
                    ul {
                    list-style: none;
                    margin: 0;
                    padding: 0;
                    }

                    ul li {
                    position: relative;
                    padding-left: 1.5em; /* Einrückung für alle Zeilen */
                    }

                    ul li::before {
                    content: ">";
                    position: absolute;
                    left: 0;         /* Zeigt das Symbol an der linken Kante */
                    }
                    strong {
                        font-weight: bold;
                    }
                    em {
                        font-style: italic;
                    }
                    blockquote {
                        border-left: 1.5mm solid #ddd; /* Dünnerer, hellerer Balken */
                        padding-left: 3mm;
                        margin: 0 0 5mm 0;
                        color: #666;
                    }
                    table {
                        width: 100%;
                        border-collapse: collapse;
                    }
                    th, td {
                        border: 1px solid black;
                        padding: 2mm;
                        text-align: left;
                    }
                </style>
            </head>
            <body>
                $htmlContent
            </body>
            </html>
        """.trimIndent()

        // **PDF als 80mm-Dokument rendern**
        val tempPdfFile = File.createTempFile("md2thermal", ".pdf")

        try {
            val outputStream = FileOutputStream(tempPdfFile)
            val builder = PdfRendererBuilder()
            builder.useFastMode()
            builder.withHtmlContent(styledHtml, null)
            builder.toStream(outputStream)
            builder.run()
            outputStream.close()

            // **PDF laden und sicherstellen, dass das Format stimmt**
            PDDocument.load(tempPdfFile).use { pdfDocument ->
                pdfDocument.pages.forEach { page ->
                    page.mediaBox = PDRectangle(226.77f, page.mediaBox.height) // 80mm Breite, Höhe bleibt flexibel
                }

                val finalPdfFile = File.createTempFile("md2thermal_final", ".pdf")
                pdfDocument.save(finalPdfFile)
                return finalPdfFile
            }
        } finally {
            // tempPdfFile.delete()
        }
    }
}