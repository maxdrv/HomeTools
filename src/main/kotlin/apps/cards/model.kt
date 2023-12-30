package apps.cards

import kotlinx.serialization.Serializable

@Serializable
data class CardList(val content: List<Card>)

@Serializable
data class Card(val group: String, val topic: String, val question: String, val answer: String)
