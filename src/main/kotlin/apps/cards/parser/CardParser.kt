package apps.cards.parser

import apps.cards.CardContent

interface CardParser {

    fun parse(text: String): List<CardContent>

}