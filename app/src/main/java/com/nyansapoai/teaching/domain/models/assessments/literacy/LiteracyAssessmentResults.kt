package com.nyansapoai.teaching.domain.models.assessments.literacy

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class LiteracyAssessmentResults(
    val assessmentId: String = "",
    val completed_assessment: Boolean = false,
    val student_id: String = "",
    val literacy_results: LiteracyResultsData? = null,
    val student_grade: Int? =  null
){
    data class LiteracyResultsData(
        val reading_results: List<ReadingResult>? = null,
        val multiple_choice_questions: List<MultipleChoicesResult>? = null,
    ){
        data class ReadingResult(
            val content: String = "",
            val metadata: ReadingAssessmentMetadata? = null,
        ){
            data class ReadingAssessmentMetadata(
                val audio_url: String = "",
                val mistakes: Int = 0,
                val passed: Boolean = false,
                val transcript: String = "",
                val transcription_failed: Boolean = false,
                val type: String = "",
            )
        }
    }

}


