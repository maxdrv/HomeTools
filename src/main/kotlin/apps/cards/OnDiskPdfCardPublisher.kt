package apps.cards

import util.TDir

sealed interface CardPublisher {

    fun publish(name: String, cards: CardList)

}

class OnDiskPdfCardPublisher(private val dest: TDir, private val printer: CardsPdfPrinter): CardPublisher {

    override fun publish(name: String, cards: CardList) {
        val outputFilename = "$name.pdf"
        val outputPath = dest.path.resolve(outputFilename)
        printer.print(outputPath, cards)
    }

}
