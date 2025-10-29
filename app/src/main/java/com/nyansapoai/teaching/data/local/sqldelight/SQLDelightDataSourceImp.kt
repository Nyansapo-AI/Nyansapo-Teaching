package com.nyansapoai.teaching.data.local.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.nyansapoai.teaching.Database
import com.nyansapoai.teaching.data.local.LocalDataSource
import com.nyansapoai.teaching.domain.models.school.LocalSchoolInfo
import com.nyansapoai.teaching.domain.mapper.assessment.toCompletedAssessment
import com.nyansapoai.teaching.domain.mapper.assessment.toNumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.mapper.assessment.toNumeracyWordProblem
import com.nyansapoai.teaching.domain.mapper.assessment.toPendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.mapper.assessment.toPendingReadingAssessmentResult
import com.nyansapoai.teaching.domain.mapper.school.toLocalSchoolInfo
import com.nyansapoai.teaching.domain.models.assessments.CompletedAssessment
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingMultipleChoicesResult
import com.nyansapoai.teaching.domain.models.assessments.literacy.PendingReadingAssessmentResult
import com.nyansapoai.teaching.domain.models.assessments.numeracy.CountMatch
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyArithmeticOperation
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyWordProblem
import com.nyansapoai.teaching.domain.models.survey.Child
import com.nyansapoai.teaching.domain.models.survey.ChildLearningEnvironment
import com.nyansapoai.teaching.domain.models.survey.CreateHouseHoldInfo
import com.nyansapoai.teaching.domain.models.survey.Parent
import com.nyansapoai.teaching.domain.models.survey.ParentalEngagement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.text.toInt

class SQLDelightDataSourceImp(
    private val database: Database,
): LocalDataSource {

    private val assessmentQueries = database.assessmentDatabaseQueries
    private val schoolDatabaseQueries = database.schoolDatabaseQueries

    private val surveyQueries = database.surveyQueries



    override suspend fun clearAllData() {
        assessmentQueries.transaction {
            schoolDatabaseQueries.clearSchoolInfo()
        }
    }

    override suspend fun insertPendingReadingResult(
        assessmentId: String,
        studentId: String,
        type: String,
        content: String,
        audioUrl: String,
        transcript: String,
        passed: Boolean,
        timestamp: Int,
        isPending: Boolean
    ) {
        assessmentQueries.insertPendingResult(
            assessmentId = assessmentId,
            studentId = studentId,
            type = type,
            content = content,
            audioUrl = audioUrl,
            transcript = transcript,
            passed = if (passed) 1L else 0L,
            timestamp = timestamp.toLong(),
            isPending = if (isPending) 1L else 0L
        )
    }

    override suspend fun getPendingReadingResults(assessmentId: String, studentId: String): Flow<List<PendingReadingAssessmentResult>> {
        return assessmentQueries.getPendingResultsByAssessmentandStudent(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pendingResults -> pendingResults.map { it.toPendingReadingAssessmentResult() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun markResultsAsSubmitted(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.markResultsAsSubmitted(
            assessmentId = assessmentId,
            studentId = studentId
        )
    }

    override suspend fun deleteSubmittedResults(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.deleteSubmittedResults(
            assessmentId = assessmentId,
            studentId = studentId
        )
    }

    override suspend fun insertPendingMultipleChoicesResult(
        assessmentId: String,
        studentId: String,
        question: String,
        options: List<String>,
        studentAnswer: String,
        passed: Boolean,
        timestamp: Int,
        isPending: Boolean
    ) {
        assessmentQueries.insertPendingMultipleChoicesResult(
            assessmentId = assessmentId,
            studentId = studentId,
            question = question,
            options = options.joinToString("#"),
            studentAnswer = studentAnswer,
            passed = if (passed) 1L else 0L,
            timestamp = timestamp.toLong(),
            isPending = if (isPending) 1L else 0L,
        )
    }

    override suspend fun getPendingMultipleChoicesResults(
        assessmentId: String,
        studentId: String
    ): Flow<List<PendingMultipleChoicesResult>> {
        return assessmentQueries.getPendingMultipleChoicesResultsByAssessmentandStudent(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pending -> pending.map { it.toPendingMultipleChoicesResult() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun markMultipleChoicesResultsAsSubmitted(
        studentId: String,
        assessmentId: String
    ) {
        assessmentQueries.markMultipleChoicesResultsAsSubmitted(
            assessmentId = assessmentId,
            studentId = studentId,
        )
    }

    override suspend fun clearSubmittedMultipleChoicesResults(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.deleteSubmittedResults(assessmentId = assessmentId, studentId = studentId)
    }

    override suspend fun saveCurrentSchoolInfo(
        organizationUid: String,
        projectUid: String,
        schoolUid: String
    ) {
        schoolDatabaseQueries.transaction{

            schoolDatabaseQueries.clearSchoolInfo()

            schoolDatabaseQueries.insertSchoolInfo(
                organizationUID = organizationUid,
                projectUID = projectUid,
                schoolUID = schoolUid
            )
        }
    }

    override fun getSavedCurrentSchoolInfo(): Flow<LocalSchoolInfo> {
        return schoolDatabaseQueries.getSchoolInfo()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map {  it -> it.toLocalSchoolInfo() }
            .flowOn(Dispatchers.Main)
    }

    override suspend fun insertCompletedAssessment(
        studentId: String,
        assessmentId: String
    ) {
        assessmentQueries.insertCompleteAssessment(
            assessmentId = assessmentId,
            studentId = studentId,
            isCompleted = 1
        )
    }

    override fun completeAssessment(
        studentId: String,
        assessmentId: String,
        isCompleted: Boolean
    ) {
        assessmentQueries.updateAssessmentCompletion(
            studentId = studentId,
            assessmentId = assessmentId,
            isCompleted = if (isCompleted) 1 else 0
        )
    }

    override suspend fun fetchCompletedAssessments(assessmentId: String): Flow<List<CompletedAssessment>> {
        return assessmentQueries.getCompletedAssessments(assessmentId = assessmentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map {  it.map { assessment -> assessment.toCompletedAssessment() } }
            .flowOn(Dispatchers.IO )
    }

    override suspend fun insertLiteracyAssessmentWorkerRequest(
        assessmentId: String,
        studentId: String,
        requestId: String,
        type: String
    ) {
        assessmentQueries.insertLIteracyAssessmentWorkerRequest(
            requestId = requestId,
            assessmentId = assessmentId,
            studentId = studentId,
            assessmentType = type
        )
    }

    override suspend fun getLiteracyAssessmentWorkerRequests(
        assessmentId: String,
        studentId: String,
        type: String
    ): Flow<List<String>> {
        return assessmentQueries.getLIteracyAssessmentWorkerRequestByAssessmentIDandStudentIDandAssessmentType(
            assessmentId = assessmentId,
            studentId = studentId,
            assessmentType = type
        )
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map { request -> request.requestId } }
            .flowOn(Dispatchers.Main)
    }

    override suspend fun clearLiteracyAssessmentRequests(
        assessmentId: String,
        studentId: String,
        type: String
    ) {
        assessmentQueries.clearLIteracyAssessmentWorkerRequestByAssessmentIDandStudentIDandAssessmentType(
            assessmentId = assessmentId,
            studentId = studentId,
            assessmentType = type
        )
    }

    override suspend fun insertPendingNumeracyOperation(
        assessmentId: String,
        studentId: String,
        numeracyArithmeticOperation: NumeracyArithmeticOperation
    ) {
        assessmentQueries.insertPendingNumeracyArithmeticResult(
            assessmentId = assessmentId,
            studentId = studentId,
            operationType = numeracyArithmeticOperation.type,
            expectedAnswer = numeracyArithmeticOperation.expected_answer.toLong(),
            answer = numeracyArithmeticOperation.student_answer?.toLong(),
            operand1 = numeracyArithmeticOperation.operationNumber1.toLong(),
            operand2 = numeracyArithmeticOperation.operationNumber2.toLong(),
            workAreaImageUrl = numeracyArithmeticOperation.metadata?.workAreaMediaUrl,
            answerImageUrl = numeracyArithmeticOperation.metadata?.answerMediaUrl,
            passed = if (numeracyArithmeticOperation.metadata?.passed == true) 1L else 0L
        )
    }

    override suspend fun getPendingNumeracyArithmeticOperations(
        assessmentId: String,
        studentId: String
    ): Flow<List<NumeracyArithmeticOperation>> {
        return assessmentQueries.getPendingNumeracyArithmeticResults(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pendingResults -> pendingResults.map { it.toNumeracyArithmeticOperation() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun clearPendingNumeracyArithmeticOperations(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.clearPendingNumeracyArithmeticResults(assessmentId, studentId)
    }

    override fun insertPendingNumeracyWordProblemResult(
        assessmentId: String,
        studentId: String,
        numeracyWordProblem: NumeracyWordProblem
    ) {
        assessmentQueries.insertPendingNumeracyWordProblemResult(
            assessmentId = assessmentId,
            studentId = studentId,
            question = numeracyWordProblem.question,
            studentAnswer = numeracyWordProblem.studentAnswer?.toLong(),
            expectedAnswer = numeracyWordProblem.expectedAnswer.toLong(),
            passed = if (numeracyWordProblem.metadata?.passed == true) 1L else 0L,
            workAreaImageUrl = numeracyWordProblem.metadata?.workAreaMediaUrl,
            answerImageUrl = numeracyWordProblem.metadata?.answerMediaUrl,
        )
    }

    override fun getPendingNumeracyWordProblems(
        assessmentId: String,
        studentId: String
    ): Flow<List<NumeracyWordProblem>> {
        return assessmentQueries.getPendingNumeracyWordProblemResults(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pendingResults -> pendingResults.map { it.toNumeracyWordProblem() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun clearPendingNumeracyWordProblems(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.clearPendingNumeracyWordProblemResults(assessmentId, studentId)
    }

    override suspend fun insertPendingCountMatch(
        assessmentId: String,
        studentId: String,
        countList: List<CountMatch>
    ) {
        assessmentQueries.transaction {
            countList.forEach { countMatch ->
                assessmentQueries.insertPendingCountAndMatchResult(
                    assessmentId = assessmentId,
                    studentId = studentId,
                    expectedNumber = countMatch.expected_number?.toLong() ?: 0,
                    studentAnswer = countMatch.student_count?.toLong() ?: 0,
                    passed = if (countMatch.passed == true) 1L else 0L,
                )
            }
        }
    }

    override suspend fun getPendingCountMatches(
        assessmentId: String,
        studentId: String
    ): Flow<List<CountMatch>> {
        return assessmentQueries.getPendingCountAndMatchResults(assessmentId = assessmentId, studentId = studentId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { pendingResults -> pendingResults.map {
                CountMatch(
                    expected_number = it.expectedNumber.toInt(),
                    student_count = it.studentAnswer?.toInt(),
                    passed = it.passed == 1L,
                )
            } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun clearPendingCountMatches(
        assessmentId: String,
        studentId: String
    ) {
        assessmentQueries.clearPendingCountAndMatchResults(assessmentId, studentId)
    }

    override suspend fun insertAssignedStudent(
        studentId: String,
        firstName: String,
        lastName: String,
        isLinked: Boolean
    ) {
        schoolDatabaseQueries.insertAssignedStudent(
            studentId = studentId,
            studentFirstName = firstName,
            studentLastName = lastName,
            isLinked = if(isLinked) 1L else 0L
        )
    }

    override fun getAssignedStudents(): Flow<List<Child>> {
        return schoolDatabaseQueries.getAssignedStudents()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    Child(
                        linkedLearnerId = entity.studentId,
                        firstName = entity.studentFirstName,
                        lastName = entity.studentLastName,
//                        isLinked = entity.isLinked == 1L
                    )
                }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun insertHouseholdData(createHouseHoldData: CreateHouseHoldInfo) {
        surveyQueries.transaction {
            // insert household (positional args to match .sq file parameter order)
            surveyQueries.insertHousehold(
                createHouseHoldData.id,
                createHouseHoldData.interviewerName,
                createHouseHoldData.interviewDate,
                createHouseHoldData.village,
                createHouseHoldData.county,
                createHouseHoldData.subCounty,
                createHouseHoldData.ward,
                if (createHouseHoldData.consentGiven) 1L else 0L,
                createHouseHoldData.respondentName,
                if (createHouseHoldData.isHouseholdHead) 1L else 0L,
                createHouseHoldData.householdHeadName,
                createHouseHoldData.relationshipToHead,
                createHouseHoldData.householdHeadPhone,
                createHouseHoldData.respondentAge?.toLong(),
                createHouseHoldData.mainLanguage,
                createHouseHoldData.maritalStatus,
                createHouseHoldData.householdMembersCount?.toLong(),
                createHouseHoldData.incomeSource,
                createHouseHoldData.hasElectricity?.let { if (it) 1L else 0L }
            )

            // children
            createHouseHoldData.children.forEach { child ->
                surveyQueries.insertChild(
                    createHouseHoldData.id,
                    child.firstName,
                    child.lastName,
                    child.gender,
                    child.age,
                    child.livesWith,
                    child.linkedLearnerId
                )
            }

            // parents
            createHouseHoldData.parents.forEach { parent ->
                surveyQueries.insertParent(
                    createHouseHoldData.id,
                    parent.name,
                    parent.age,
                    parent.type,
                    if (parent.hasAttendedSchool) 1L else 0L,
                    parent.highestEducationLevel
                )
            }

            // assets
            createHouseHoldData.householdAssets.forEach { asset ->
                surveyQueries.insertHouseholdAsset(
                    createHouseHoldData.id,
                    asset
                )
            }

            // parental engagement
            createHouseHoldData.parentalEngagement?.let { engagement ->
                surveyQueries.insertParentalEngagement(
                    createHouseHoldData.id,
                    if (engagement.hasSchoolAgeChild) 1L else 0L,
                    engagement.homeworkHelper,
                    engagement.teacherDiscussionFrequency,
                    engagement.attendsSchoolMeetings?.let { if (it) 1L else 0L },
                    engagement.monitorsAttendance?.let { if (it) 1L else 0L }
                )
            }

            // child learning environment
            createHouseHoldData.childLearningEnvironment?.let { learningEnvironment ->
                surveyQueries.insertChildLearningEnvironment(
                    createHouseHoldData.id,
                    if (learningEnvironment.hasQuietPlaceToStudy) 1L else 0L,
                    if (learningEnvironment.hasBooksOrMaterials) 1L else 0L,
                    if (learningEnvironment.missedSchoolLastMonth) 1L else 0L,
                    learningEnvironment.reasonForMissingSchool
                )
            }
        }
    }

    // kotlin
    override fun getPendingHouseholdData(): Flow<List<CreateHouseHoldInfo>> {
        return surveyQueries.getAllFullCreateHouseHoldInfo()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows ->
                rows.groupBy { it.id }.map { (_, group) ->
                    val householdRow = group.first()

                    // build children list (one child per row where childFirstName or any child column is not null)
                    val children = group.mapNotNull { row ->
                        if (row.childFirstName == null &&
                            row.childLastName == null &&
                            row.childGender == null &&
                            row.childAge == null &&
                            row.childLivesWith == null &&
                            row.childLinkedLearnerId == null
                        ) null
                        else Child(
                            firstName = row.childFirstName ?: "",
                            lastName = row.childLastName ?: "",
                            gender = row.childGender ?: "",
                            age = row.childAge ?: "",
                            livesWith = row.childLivesWith ?: "",
                            linkedLearnerId = row.childLinkedLearnerId ?: ""
                        )
                    }.distinctBy { listOf(it.firstName, it.lastName, it.age, it.linkedLearnerId) }

                    // build parents list
                    val parents = group.mapNotNull { row ->
                        if (row.parentName == null &&
                            row.parentAge == null &&
                            row.parentType == null &&
                            row.parentHasAttendedSchool == null &&
                            row.parentHighestEducationLevel == null
                        ) null
                        else Parent(
                            name = row.parentName ?: "",
                            age = row.parentAge ?: "",
                            type = row.parentType ?: "",
                            hasAttendedSchool = (row.parentHasAttendedSchool == 1L),
                            highestEducationLevel = row.parentHighestEducationLevel ?: ""
                        )
                    }.distinctBy { listOf(it.name, it.age, it.type) }

                    // build assets list
                    val assets = group.mapNotNull { it.householdAsset }.distinct()

                    // parental engagement (unique per household, may be null)
                    val parentalEngagement = if (householdRow.parentalEngagementHasSchoolAgeChild == null &&
                        householdRow.parentalEngagementHomeworkHelper == null &&
                        householdRow.parentalEngagementTeacherDiscussionFrequency == null &&
                        householdRow.parentalEngagementAttendsSchoolMeetings == null &&
                        householdRow.parentalEngagementMonitorsAttendance == null
                    ) {
                        null
                    } else {
                        ParentalEngagement(
                            hasSchoolAgeChild = (householdRow.parentalEngagementHasSchoolAgeChild == 1L),
                            homeworkHelper = householdRow.parentalEngagementHomeworkHelper,
                            teacherDiscussionFrequency = householdRow.parentalEngagementTeacherDiscussionFrequency,
                            attendsSchoolMeetings = householdRow.parentalEngagementAttendsSchoolMeetings?.let { it == 1L },
                            monitorsAttendance = householdRow.parentalEngagementMonitorsAttendance?.let { it == 1L }
                        )
                    }

                    // child learning environment (unique per household, may be null)
                    val childLearningEnvironment = if (householdRow.childLearningEnvironmentHasQuietPlaceToStudy == null &&
                        householdRow.childLearningEnvironmentHasBooksOrMaterials == null &&
                        householdRow.childLearningEnvironmentMissedSchoolLastMonth == null &&
                        householdRow.childLearningEnvironmentReasonForMissingSchool == null
                    ) {
                        null
                    } else {
                        ChildLearningEnvironment(
                            hasQuietPlaceToStudy = (householdRow.childLearningEnvironmentHasQuietPlaceToStudy == 1L),
                            hasBooksOrMaterials = (householdRow.childLearningEnvironmentHasBooksOrMaterials == 1L),
                            missedSchoolLastMonth = (householdRow.childLearningEnvironmentMissedSchoolLastMonth == 1L),
                            reasonForMissingSchool = householdRow.childLearningEnvironmentReasonForMissingSchool
                        )
                    }

                    CreateHouseHoldInfo(
                        id = householdRow.id,
                        interviewerName = householdRow.interviewerName,
                        interviewDate = householdRow.interviewDate,
                        village = householdRow.village,
                        county = householdRow.county ?: "",
                        subCounty = householdRow.subCounty ?: "",
                        ward = householdRow.ward ?: "",
                        consentGiven = (householdRow.consentGiven == 1L),
                        respondentName = householdRow.respondentName,
                        isHouseholdHead = (householdRow.isHouseholdHead == 1L),
                        householdHeadName = householdRow.householdHeadName,
                        relationshipToHead = householdRow.relationshipToHead,
                        householdHeadPhone = householdRow.householdHeadPhone,
                        respondentAge = householdRow.respondentAge?.toInt(),
                        mainLanguage = householdRow.mainLanguage,
                        children = children,
                        parents = parents,
                        maritalStatus = householdRow.maritalStatus,
                        householdMembersCount = householdRow.householdMembersCount?.toInt(),
                        incomeSource = householdRow.incomeSource,
                        hasElectricity = householdRow.hasElectricity?.let { it == 1L },
                        householdAssets = assets,
                        parentalEngagement = parentalEngagement,
                        childLearningEnvironment = childLearningEnvironment
                    )
                }
            }
            .flowOn(Dispatchers.IO)
    }


    override fun getPendingHouseholdDataById(householdId: String): Flow<CreateHouseHoldInfo?> {
        return surveyQueries.getHouseholdById(id = householdId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows ->
                val householdRow = rows.firstOrNull() ?: return@map null

                // children, parents, assets from dedicated queries
                val children = surveyQueries.getChildrenByHouseholdId(id = householdId)
                    .executeAsList()
                    .map { row ->
                        Child(
                            firstName = row.first_name ?: "",
                            lastName = row.last_name ?: "",
                            gender = row.gender ?: "",
                            age = row.age ?: "",
                            livesWith = row.lives_with ?: "",
                            linkedLearnerId = row.linked_learner_id ?: ""
                        )
                    }

                val parents = surveyQueries.getParentsByHouseholdId(id = householdId)
                    .executeAsList()
                    .map { row ->
                        Parent(
                            name = row.name ?: "",
                            age = row.age ?: "",
                            type = row.type ?: "",
                            hasAttendedSchool = (row.has_attended_school == 1L),
                            highestEducationLevel = row.highest_education_level ?: ""
                        )
                    }

                val assets = surveyQueries.getHouseholdAssetsByHouseholdId(id = householdId)
                    .executeAsList()
                    .mapNotNull { it.asset }
                    .distinct()

                // use the full-household joined query to fetch parental engagement and child learning environment fields
                val fullRow = surveyQueries.getFullHouseholdById(id = householdId).executeAsList().firstOrNull()

                val parentalEngagement = if (fullRow == null ||
                    (fullRow.parentalEngagementHasSchoolAgeChild == null &&
                            fullRow.parentalEngagementHomeworkHelper == null &&
                            fullRow.parentalEngagementTeacherDiscussionFrequency == null &&
                            fullRow.parentalEngagementAttendsSchoolMeetings == null &&
                            fullRow.parentalEngagementMonitorsAttendance == null)
                ) {
                    null
                } else {
                    ParentalEngagement(
                        hasSchoolAgeChild = (fullRow.parentalEngagementHasSchoolAgeChild == 1L),
                        homeworkHelper = fullRow.parentalEngagementHomeworkHelper,
                        teacherDiscussionFrequency = fullRow.parentalEngagementTeacherDiscussionFrequency,
                        attendsSchoolMeetings = fullRow.parentalEngagementAttendsSchoolMeetings?.let { it == 1L },
                        monitorsAttendance = fullRow.parentalEngagementMonitorsAttendance?.let { it == 1L }
                    )
                }

                val childLearningEnvironment = if (fullRow == null ||
                    (fullRow.childLearningEnvironmentHasQuietPlaceToStudy == null &&
                            fullRow.childLearningEnvironmentHasBooksOrMaterials == null &&
                            fullRow.childLearningEnvironmentMissedSchoolLastMonth == null &&
                            fullRow.childLearningEnvironmentReasonForMissingSchool == null)
                ) {
                    null
                } else {
                    ChildLearningEnvironment(
                        hasQuietPlaceToStudy = (fullRow.childLearningEnvironmentHasQuietPlaceToStudy == 1L),
                        hasBooksOrMaterials = (fullRow.childLearningEnvironmentHasBooksOrMaterials == 1L),
                        missedSchoolLastMonth = (fullRow.childLearningEnvironmentMissedSchoolLastMonth == 1L),
                        reasonForMissingSchool = fullRow.childLearningEnvironmentReasonForMissingSchool
                    )
                }

                CreateHouseHoldInfo(
                    id = householdRow.id,
                    interviewerName = householdRow.interviewer_name,
                    interviewDate = householdRow.interview_date,
                    village = householdRow.village,
                    county = householdRow.county ?: "",
                    subCounty = householdRow.sub_county ?: "",
                    ward = householdRow.ward ?: "",
                    consentGiven = (householdRow.consent_given == 1L),
                    respondentName = householdRow.respondent_name,
                    isHouseholdHead = (householdRow.is_household_head == 1L),
                    householdHeadName = householdRow.household_head_name,
                    relationshipToHead = householdRow.relationship_to_head,
                    householdHeadPhone = householdRow.household_head_phone,
                    respondentAge = householdRow.respondent_age?.toInt(),
                    mainLanguage = householdRow.main_language,
                    children = children,
                    parents = parents,
                    maritalStatus = householdRow.marital_status,
                    householdMembersCount = householdRow.household_members_count?.toInt(),
                    incomeSource = householdRow.income_source,
                    hasElectricity = householdRow.has_electricity?.let { it == 1L },
                    householdAssets = assets,
                    parentalEngagement = parentalEngagement,
                    childLearningEnvironment = childLearningEnvironment
                )
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getChildrenInPendingHouseholds(): Flow<List<Child>> {
        return surveyQueries.getChildren()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entries ->
                entries.map { entry ->
                    Child(
                        firstName = entry.first_name ?: "",
                        lastName = entry.last_name ?: "",
                        gender = entry.gender ?: "",
                        age = entry.age ?: "",
                        livesWith = entry.lives_with ?: "",
                        linkedLearnerId = entry.linked_learner_id ?: ""
                    )
                }
            }
            .flowOn(Dispatchers.Main)
    }

    override fun clearSubmittedHouseholdData(householdId: String) {
        surveyQueries.clearHouseholdData(householdId)
    }

}