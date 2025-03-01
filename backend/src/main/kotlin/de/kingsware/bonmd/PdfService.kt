package de.kingsware.bonmd

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import org.apache.pdfbox.pdmodel.PDDocument
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
            FileOutputStream(tempPdfFile).use { outputStream ->
                val builder = PdfRendererBuilder()

                // Schriftarten aus src/main/resources laden
                val fontRegular = ClassPathResource("MesloLGS NF Regular.ttf").inputStream
                val fontBold = ClassPathResource("MesloLGS NF Bold.ttf").inputStream
                val fontItalic = ClassPathResource("MesloLGS NF Italic.ttf").inputStream
                val fontBoldItalic = ClassPathResource("MesloLGS NF Bold Italic.ttf").inputStream

                // Schriftarten korrekt registrieren
                builder.useFont({ fontRegular }, "MesloLGS", 400, BaseRendererBuilder.FontStyle.NORMAL, true)
                builder.useFont({ fontBold }, "MesloLGS", 700, BaseRendererBuilder.FontStyle.NORMAL, true)
                builder.useFont({ fontItalic }, "MesloLGS", 400, BaseRendererBuilder.FontStyle.ITALIC, true)
                builder.useFont({ fontBoldItalic }, "MesloLGS", 700, BaseRendererBuilder.FontStyle.ITALIC, true)

                // HTML rendern
                builder.useFastMode()
                builder.withHtmlContent(styledHtml, null)
                builder.toStream(outputStream)
                builder.run()
            }

            // Zweiter Schritt (Seitenanpassung) entfällt – wir rendern direkt mit 80mm x 3276mm
            // und geben die Datei direkt zurück
            return tempPdfFile

        } finally {
            // Falls nötig, temporäre Datei löschen (hier auskommentiert, um das Ergebnis zu behalten)
            // tempPdfFile.delete()
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
                    /* Direkt auf 80mm Breite und 3276mm Höhe (bspw. sehr lange Quittung) */
                    size: 80mm 3276mm;
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
                    border-left: 1.5mm solid black;
                    padding-left: 3mm;
                    margin: 0 0 5mm 0;
                    color: black;
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
                    color: black;
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

