package de.kingsware.bonmd

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream

@Service
class PdfService {

    fun convertMarkdownToPdf(mdContent: String): File {
        val htmlContent = markdownToHtml(mdContent)
        val styledHtml = generateStyledHtml(htmlContent)

        val tempPdfFile = File.createTempFile("md2thermal", ".pdf")

        try {
            val outputStream = FileOutputStream(tempPdfFile)
            val builder = PdfRendererBuilder()

            // Lade Schriftarten aus src/main/resources
            val fontRegular = ClassPathResource("MesloLGS NF Regular.ttf").inputStream
            val fontBold = ClassPathResource("MesloLGS NF Bold.ttf").inputStream
            val fontItalic = ClassPathResource("MesloLGS NF Italic.ttf").inputStream
            val fontBoldItalic = ClassPathResource("MesloLGS NF Bold Italic.ttf").inputStream

            // Schriftarten korrekt registrieren
            builder.useFont({ fontRegular }, "MesloLGS", 400, BaseRendererBuilder.FontStyle.NORMAL, true)
            builder.useFont({ fontBold }, "MesloLGS", 700, BaseRendererBuilder.FontStyle.NORMAL, true)
            builder.useFont({ fontItalic }, "MesloLGS", 400, BaseRendererBuilder.FontStyle.ITALIC, true)
            builder.useFont({ fontBoldItalic }, "MesloLGS", 700, BaseRendererBuilder.FontStyle.ITALIC, true)

            builder.useFastMode()
            builder.withHtmlContent(styledHtml, null)
            builder.toStream(outputStream)
            builder.run()
            outputStream.close()

            PDDocument.load(tempPdfFile).use { pdfDocument ->
                pdfDocument.pages.forEach { page ->
                    page.mediaBox = PDRectangle(226.77f, page.mediaBox.height)
                }

                val finalPdfFile = File.createTempFile("md2thermal_final", ".pdf")
                pdfDocument.save(finalPdfFile)
                return finalPdfFile
            }
        } finally {
            tempPdfFile.delete()
        }
    }

    private fun markdownToHtml(mdContent: String): String {
        val options = com.vladsch.flexmark.util.data.MutableDataSet().toImmutable()
        val parser = com.vladsch.flexmark.parser.Parser.builder().build()
        val renderer = com.vladsch.flexmark.html.HtmlRenderer.builder().build()
        val document = parser.parse(mdContent)
        return renderer.render(document)
    }

    private fun generateStyledHtml(htmlContent: String): String {
        return """
        <!DOCTYPE html>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <meta charset="UTF-8"/>
            <style>
                @page {
                    size: 80mm auto;
                    margin: 0;
                }
                body {
                    width: 72mm;
                    font-size: 11px;
                    font-family: "MesloLGS", monospace;
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
                    padding-left: 1.5em;
                }
                ul li::before {
                    content: ">";
                    position: absolute;
                    left: 0;
                }
                strong {
                    font-weight: bold;
                }
                em {
                    font-style: italic;
                }
                blockquote {
                    border-left: 1.5mm solid black; /* Schmale, schwarze vertikale Linie */
                    padding-left: 3mm;
                    margin: 0 0 5mm 0;
                    color: black; /* Schwarze Schriftfarbe für Zitate */
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
                a:link, a:visited, a:hover, a:active {
                    color: black; /* Schwarze Schriftfarbe für Links */
                    text-decoration: none; /* Entfernt die Unterstreichung */
                }
            </style>
        </head>
        <body>
            $htmlContent
        </body>
        </html>
        """.trimIndent()
    }

}

