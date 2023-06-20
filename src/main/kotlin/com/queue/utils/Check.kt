package com.queue.utils

import com.lowagie.text.*
import com.lowagie.text.alignment.HorizontalAlignment
import com.lowagie.text.pdf.PdfWriter
import com.queue.service.QueueService
import org.springframework.core.io.ClassPathResource
import java.awt.Color
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class Lang(val lang: String) {
    RU("ru"),
    KZ("kz")
}

class Check(
    val lang : Lang,
    queueService: QueueService?
) {
    private val fileName = "check.pdf"

    init {
        val document = Document(PageSize.B6)

        PdfWriter.getInstance(document, FileOutputStream(System.getProperty("java.io.tmpdir") + fileName))

        val image: Image = Image.getInstance(ClassPathResource("static/images/pdf/logo.gif").url);

        image.scaleAbsolute(200F, 75F);

        document.open()

        val table = Table(1)

        table.border = 0

        if (this.lang == Lang.RU) {
            table.addCell(getCell(image, HorizontalAlignment.CENTER))
            table.addCell(getCell("ВАШ НОМЕР",30.0f, HorizontalAlignment.CENTER))
            table.addCell(getCell(queueService?.number.toString(), 30.0f, HorizontalAlignment.CENTER))
            table.addCell(getCell(queueService?.service.toString(),15.0f, HorizontalAlignment.CENTER))
            table.addCell(getCell(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now()).toString(),15.0f, HorizontalAlignment.CENTER))
            table.addCell(getCell("ЖДИТЕ ВЫЗОВА НА ТАБЛО", 30.0f, HorizontalAlignment.CENTER))
        } else if (this.lang == Lang.KZ) {
            table.addCell(getCell(image, HorizontalAlignment.CENTER))
            table.addCell(getCell("Бөлмеңіздің нөмірі",30.0f, HorizontalAlignment.CENTER))
            table.addCell(getCell(queueService?.number.toString(), 30.0f, HorizontalAlignment.CENTER))
            table.addCell(getCell(queueService?.service.toString(),15.0f, HorizontalAlignment.CENTER))
            table.addCell(getCell(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now()).toString(),15.0f, HorizontalAlignment.CENTER))
            table.addCell(getCell("Таблодағы қоңырауды күтіңіз", 30.0f, HorizontalAlignment.CENTER))
        }

        document.add(table)

        document.close()
   }

    private fun getCell(text: String, size: Float, alignment: HorizontalAlignment): Cell {
        return Cell().also {
            it.add(Paragraph(text, Font(Font.BOLD, 24.0f, Font.BOLD, Color.BLACK)))
            it.setHorizontalAlignment(alignment)
            it.border = 0
        }
    }

    private fun getCell(image: Image, alignment: HorizontalAlignment): Cell {
        return Cell().also {
            it.add(image)
            it.setHorizontalAlignment(alignment)
            it.border = 0
        }
    }

    fun data(): ByteArray {
        return Files.readAllBytes(Paths.get(System.getProperty("java.io.tmpdir") + fileName))
    }

}