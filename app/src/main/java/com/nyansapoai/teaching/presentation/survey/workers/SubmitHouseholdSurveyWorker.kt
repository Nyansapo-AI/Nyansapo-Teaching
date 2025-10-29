package com.nyansapoai.teaching.presentation.survey.workers

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
import com.nyansapoai.teaching.data.remote.survey.SurveyRepository
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.utils.ResultStatus
import com.nyansapoai.teaching.utils.Results
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.supervisorScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.getValue

class SubmitHouseholdSurveyWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent
{
    private val localDataSource: LocalDataSource by inject()
    private val surveyRepository: SurveyRepository by inject()
    private val assessmentRepository: AssessmentRepository by inject()
    private val studentsRepository: StudentsRepository by inject()

    override suspend fun doWork(): Result {
        val organizationId = inputData.getString(ORGANIZATION_ID) ?: return Result.failure()
        val projectId = inputData.getString(PROJECT_ID) ?: return Result.failure()
        val schoolId = inputData.getString(SCHOOL_ID) ?: return Result.failure()
        val surveyId = inputData.getString(SURVEY_ID) ?: return Result.failure()


        if (organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty() || surveyId.isEmpty()) {
            Log.e(TAG, "Invalid input data")
            return Result.failure()
        }

        val schoolInfo = LocalSchoolInfo(
            organizationUid = organizationId,
            projectUId = projectId,
            schoolUId = schoolId
        )

        return try {
            val data = localDataSource.getPendingHouseholdDataById(householdId = surveyId).first()

            if (data == null){
                Log.e(TAG, "No pending household data found for survey ID: $surveyId")
                return Result.success()
            }

            Log.d("Survey Data Upload", "Uploading household data for household ID: ${data}")

            data.let { householdData ->
                try {
                    val response = surveyRepository.submitHouseholdSurvey(
                        createHouseHold = householdData,
                        localSchoolInfo = schoolInfo
                    )

                    when (response.status) {
                        ResultStatus.INITIAL,
                        ResultStatus.ERROR,
                        ResultStatus.LOADING -> {
                            Log.e(TAG, "Error uploading household ${householdData.id}")
                            return Result.retry()
                        }

                        ResultStatus.SUCCESS -> {
                            for (child in householdData.children) {
                                if (child.linkedLearnerId.isBlank()) {
                                    continue
                                }

                                val updateResponse = updateLearnerInfo(
                                    organizationId = organizationId,
                                    projectId = projectId,
                                    schoolId = schoolId,
                                    studentId = child.linkedLearnerId,
                                    firstName = child.firstName,
                                    lastName = child.lastName,
                                    isLinked = true
                                )

                                when (updateResponse.status) {
                                    ResultStatus.INITIAL,
                                    ResultStatus.LOADING -> {
                                        Log.e(
                                            TAG,
                                            "Error updating learner ${child.linkedLearnerId} for household ${householdData.id}"
                                        )
                                        return Result.retry()
                                    }

                                    ResultStatus.SUCCESS -> {
                                        Log.d(
                                            TAG,
                                            "Successfully updated learner ${child.linkedLearnerId} for household ${householdData.id}"
                                        )
                                        // No-op
                                    }

                                    ResultStatus.ERROR -> {
                                        Log.e(
                                            TAG,
                                            "Error updating learner ${child.linkedLearnerId} for household ${householdData.id}"
                                        )
                                        continue
                                    }
                                }
                            }
                        }
                    }

                }catch (e: Exception) {
                    Log.e(TAG, "Error processing household ${householdData.id}", e)
                    return Result.retry()
                }
            }

            Result.success()

        } catch (e: Exception) {
            Log.e(TAG, "Error uploading household data", e)
            Result.retry()
        }
    }

    private suspend fun updateLearnerInfo(
        organizationId: String,
        projectId: String,
        schoolId: String,
        studentId: String,
        firstName: String,
        lastName: String,
        isLinked: Boolean
    ): Results<Unit> {

        if (studentId.isBlank()) {
            return Results.success(data = Unit)
        }

        return try {
            val (studentData, assessmentsData) = supervisorScope {
                val studentDeferred = async {
                    studentsRepository.updateStudentLinkStatus(
                        organizationId = organizationId,
                        projectId = projectId,
                        schoolId = schoolId,
                        studentId = studentId,
                        firstName = firstName,
                        lastName = lastName,
                        isLinked = isLinked
                    )
                }
                val assessmentsDeferred = async {
                    assessmentRepository.updateAssignedStudent(
                        schoolId = schoolId,
                        studentId = studentId,
                        firstName = firstName,
                        lastName = lastName,
                        isLinked = isLinked
                    )
                }
                Pair(studentDeferred.await(), assessmentsDeferred.await())
            }

            if (studentData.status == ResultStatus.ERROR || assessmentsData.status == ResultStatus.ERROR) {
                Log.w(TAG, "Transient error updating student or assessments")
                return Results.error(msg = "Transient error updating student or assessments")
            }

            Results.success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating learner info", e)
            Results.error(msg = "${e.message}")
        }
    }

    companion object {
        const val WORK_NAME = "UploadHouseholdsWorker"
        const val TAG = "UploadHouseholdDataWorker"
        const val ORGANIZATION_ID = "organization_id"
        const val PROJECT_ID = "project_id"
        const val SCHOOL_ID = "school_id"
        const val SURVEY_ID = "survey_id"

        fun buildRequest(
            localSchoolInfo: LocalSchoolInfo,
            surveyId: String
        ): OneTimeWorkRequest {
            val inputData = workDataOf(
                ORGANIZATION_ID to localSchoolInfo.organizationUid,
                PROJECT_ID to localSchoolInfo.projectUId,
                SCHOOL_ID to localSchoolInfo.schoolUId,
                SURVEY_ID to surveyId
            )

            val constraints = Constraints.Builder()
                .build()

            return OneTimeWorkRequestBuilder<SubmitHouseholdSurveyWorker>()
                .setInputData(inputData)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()
        }
    }
}
