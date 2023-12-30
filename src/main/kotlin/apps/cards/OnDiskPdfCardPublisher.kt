package apps.cards

import util.TDir

sealed interface CardPublisher {

    fun publish(name: String, cards: CardList)

}

class OnDiskPdfCardPublisher(private val dest: TDir, private val printer: CardsPdfPrinter): CardPublisher {

    override fun publish(name: String, cards: CardList) {
        printer.print(dest.path.resolve(name), cards)
    }

}
