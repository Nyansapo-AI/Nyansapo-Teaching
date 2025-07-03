package com.nyansapoai.teaching.presentation.assessments.literacy.use_cases

import com.nyansapoai.teaching.data.remote.ai.ArtificialIntelligenceRepository
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.media.MediaRepository
import com.nyansapoai.teaching.domain.models.assessments.literacy.MultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentMetadata
import com.nyansapoai.teaching.domain.models.assessments.literacy.ReadingAssessmentResult
import com.nyansapoai.teaching.presentation.assessments.literacy.components.compareResponseStrings
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AndroidLiteracyAssessmentHandler(
    private val mediaRepository: MediaRepository,
    private val assessmentRepository: AssessmentRepository,
    private val artificialIntelligenceRepository: ArtificialIntelligenceRepository
): LiteracyAssessmentHandler {

    override suspend fun evaluateReadingAssessmentResponse(
        audioByteArray: ByteArray,
        content: String,
        type: String
    ): Results<ReadingAssessmentResult> {
        return withContext(Dispatchers.IO) {
            try {

                /**
                 * Save the audio file to the media repository.
                 * This will return a URL for the saved audio file.
                 */
                val audioUrlResult = mediaRepository.saveAudio(audioByteArray = audioByteArray)
                val audioUrl = audioUrlResult.data ?: return@withContext Results.error(
                    msg = "Failed to save audio file"
                )

                /**
                 * Transcribe the audio using the AI service.
                 */
                val transcriptionResult = artificialIntelligenceRepository.getTextFromAudio(audioByteArray = audioByteArray)
                    .catch { e ->
                        emit(Results.error(msg = e.message ?: "An error occurred while transcribing"))
                    }
                    .first()

                transcriptionResult.data?.let { transcribedText ->
                    val comparisonResult = compareResponseStrings(
                        expected = content,
                        actual = transcribedText.DisplayText,
                        similarity = 0.9
                    )

                    Results.success(
                        data = ReadingAssessmentResult(
                            type = type,
                            content = content,
                            metadata = ReadingAssessmentMetadata(
                                audio_url = audioUrl,
                                passed = comparisonResult.isMatch,
                                transcript = transcribedText.DisplayText
                            )
                        )
                    )

                } ?: Results.error(msg = "Transcription failed, no text returned")

            } catch (e: Exception) {
                Results.error(msg = e.message ?: "An error occurred while evaluating the reading assessment response")
            }
        }
    }

    override suspend fun addToMultipleChoiceResults(): MultipleChoicesResult? {
        TODO("Not yet implemented")
    }

    override suspend fun submitReadingAssessment(
        assessmentId: String,
        studentID: String,
        readingAssessmentResults: List<ReadingAssessmentResult>
    ): Results<String> {
        return withContext(Dispatchers.IO){
            assessmentRepository.assessReadingAssessment(assessmentId = assessmentId, studentID = studentID, readingAssessmentResults = readingAssessmentResults)
        }
    }

}