package com.example.stickeralbum.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonPrimitive

object StickerIdSerializer : KSerializer<Int> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "StickerId",
            PrimitiveKind.INT
        )

    override fun deserialize(decoder: Decoder): Int {

        val jsonDecoder = decoder as JsonDecoder

        val idSaApi =
            jsonDecoder
                .decodeJsonElement()
                .jsonPrimitive
                .content

        return when (idSaApi) {
            "grb-ARG" -> 110
            "grb-AUT" -> 111
            "grb-BEL" -> 112
            "grb-BIH" -> 113
            "grb-BRA" -> 114
            "grb-EGY" -> 115
            "grb-ENG" -> 116
            "grb-FRA" -> 117
            "grb-CRO" -> 118
            "grb-MAR" -> 119
            "grb-NED" -> 120
            "grb-GER" -> 121
            "grb-NOR" -> 122
            "grb-POR" -> 123
            "grb-TUR" -> 124

            else -> idSaApi.toInt()
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: Int
    ) {
        encoder.encodeInt(value)
    }
}


@Serializable
@Entity(tableName = "stickers")
data class Sticker(

    @PrimaryKey
    @Serializable(with = StickerIdSerializer::class)
    val id: Int,

    val ime: String = "",
    val prezime: String = "",
    val broj_dresa: Int = 0,

    val reprezentacija: String,

    val pozicija: String = "",
    val slicica_lokacija: String,

    val tip_slicice: String = "obicna",
    val zlatna: Boolean = false,

    val favorite: Boolean = false,
    val collected: Boolean = false
)