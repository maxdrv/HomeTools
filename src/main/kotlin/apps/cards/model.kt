package apps.cards

import kotlinx.serialization.Serializable

@Serializable
data class CardList(val content: List<Card>)

@Serializable
data class Card(val topic: String, val question: String, val answer: String)

internal data class CardContent(val question: String, val answer: String)