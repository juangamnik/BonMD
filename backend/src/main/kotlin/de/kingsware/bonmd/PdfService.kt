package de.kingsware.bonmd

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.util.data.MutableDataSet

@Service
class PdfService {

    /**
     * Converts Markdown content to a styled PDF file.
     * @param mdContent The Markdown content to be converted.
     * @return A temporary PDF file.
     */
    fun convertMarkdownToPdf(mdContent: String, tempPdfFile: File): File {
        val htmlContent = markdownToHtml(mdContent) // Convert Markdown to HTML
        val styledHtml = generateStyledHtml(htmlContent) // Apply styling to the HTML

        FileOutputStream(tempPdfFile).use { outputStream ->
            val builder = PdfRendererBuilder()

            // Load font files from the classpath (resources folder)
            val fontRegular = ClassPathResource("MesloLGS NF Regular.ttf").inputStream
            val fontBold = ClassPathResource("MesloLGS NF Bold.ttf").inputStream
            val fontItalic = ClassPathResource("MesloLGS NF Italic.ttf").inputStream
            val fontBoldItalic = ClassPathResource("MesloLGS NF Bold Italic.ttf").inputStream

            // Register fonts for PDF rendering
            builder.useFont({ fontRegular }, "MesloLGS", 400, BaseRendererBuilder.FontStyle.NORMAL, true)
            builder.useFont({ fontBold }, "MesloLGS", 700, BaseRendererBuilder.FontStyle.NORMAL, true)
            builder.useFont({ fontItalic }, "MesloLGS", 400, BaseRendererBuilder.FontStyle.ITALIC, true)
            builder.useFont({ fontBoldItalic }, "MesloLGS", 700, BaseRendererBuilder.FontStyle.ITALIC, true)

            // Configure the PDF renderer
            builder.useFastMode() // Enable faster rendering
            builder.withHtmlContent(styledHtml, null) // Set the HTML content
            builder.toStream(outputStream) // Output to the file stream
            builder.run() // Execute the PDF generation
        }

        // Skip additional processing (e.g., page size adjustments) and return the file directly
        return tempPdfFile
    }

    /**
     * Converts Markdown content to HTML with table support.
     *
     * @param mdContent The raw Markdown input.
     * @return HTML representation of the Markdown.
     */
    private fun markdownToHtml(mdContent: String): String {
        val options = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
        }
        val parser = Parser.builder(options).build()
        val renderer = HtmlRenderer.builder(options).build()
        val document = parser.parse(mdContent)
        return renderer.render(document)
    }

    /**
     * Wraps the generated HTML in a styled template.
     * @param htmlContent The raw HTML content.
     * @return A complete HTML document with styles.
     */
    private fun generateStyledHtml(htmlContent: String): String {
        return """
        <!DOCTYPE html>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <meta charset="UTF-8"/>
            <style>
                @page {
                    /* Set fixed page size: 80mm width and up to 3276mm height (e.g., for long receipts) */
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

