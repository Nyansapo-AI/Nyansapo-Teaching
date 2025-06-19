package com.nyansapoai.teaching.domain.models.ai

import kotlinx.serialization.Serializable

@Serializable
data class TextFromImageResult(
    val modelVersion: String,
    val metadata: Metadata,
    val readResult: ReadResult
)

@Serializable
data class Metadata(val width: Int, val height: Int)

@Serializable
data class BoundingPolygon(val x: Int, val y: Int)

@Serializable
data class Word(
    val text: String,
    val boundingPolygon: List<BoundingPolygon>,
    val confidence: Float
)

@Serializable
data class Line(
    val text: String,
    val boundingPolygon: List<BoundingPolygon>,
    val words: List<Word>
)

@Serializable
data class Block(val lines: List<Line>)

@Serializable
data class ReadResult(val blocks: List<Block>)