package com.nyansapoai.teaching.presentation.survey.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.nyansapoai.teaching.data.remote.assessment.AssessmentRepository
import com.nyansapoai.teaching.data.remote.students.StudentsRepository
import com.nyansapoai.teaching.utils.ResultStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateLearnerDetailWorker(
    appContext: Context,
    workerParams: WorkerParameters
): KoinComponent, CoroutineWorker(appContext = appContext, params = workerParams) {

    private val studentsRepository: StudentsRepository by inject()
    private val assessmentRepository: AssessmentRepository by inject()

    override suspend fun doWork(): Result {
        val studentId = inputData.getString(STUDENT_ID) ?: return Result.failure()
        val organizationId = inputData.getString(ORGANIZATION_ID) ?: return Result.failure()
        val projectId = inputData.getString(PROJECT_ID) ?: return Result.failure()
        val schoolId = inputData.getString(SCHOOL_ID) ?: return Result.failure()
        val firstName = inputData.getString(STUDENT_FIRSTNAME) ?: ""
        val lastName = inputData.getString(STUDENT_LASTNAME) ?: ""
        val isLinked = inputData.getBoolean(STUDENT_IS_LINKED, true)

        if (studentId.isEmpty() || organizationId.isEmpty() || projectId.isEmpty() || schoolId.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            Log.e("UpdateLearnerDetailWorker", "Invalid input data")
            Log.e("UpdateLearnerDetailWorker", "Student ID: $studentId, Organization ID: $organizationId, Project ID: $projectId, School ID: $schoolId, First Name: $firstName, Last Name: $lastName")
            return Result.failure()
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
                Log.w("UpdateLearnerDetailWorker", "Transient error updating student or assessments")
                return Result.retry()
            }


            if (studentData.status != ResultStatus.SUCCESS || assessmentsData.status != ResultStatus.SUCCESS) {
                return Result.retry()
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("UpdateLearnerDetailWorker", "Failed to update learner details", e)
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "UpdateLearnerDetailWorker"
        const val STUDENT_ID = "studentId"
        const val ORGANIZATION_ID = "organizationId"
        const val SCHOOL_ID = "schoolId"
        const val PROJECT_ID = "projectId"
        const val STUDENT_FIRSTNAME = "studentFirstName"
        const val STUDENT_LASTNAME = "studentLastName"
        const val STUDENT_IS_LINKED = "studentIsLinked"

        fun createInputData(
            studentId: String,
            organizationId: String,
            projectId: String,
            schoolId: String,
            firstName: String,
            lastName: String,
            isLinked: Boolean
        ): Data {
            return workDataOf(
                STUDENT_ID to studentId,
                ORGANIZATION_ID to organizationId,
                PROJECT_ID to projectId,
                SCHOOL_ID to schoolId,
                STUDENT_FIRSTNAME to firstName,
                STUDENT_LASTNAME to lastName,
                STUDENT_IS_LINKED to isLinked
            )
        }
    }
}