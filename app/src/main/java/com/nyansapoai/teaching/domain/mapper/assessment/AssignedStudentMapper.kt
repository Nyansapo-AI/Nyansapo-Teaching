package com.nyansapoai.teaching.domain.mapper.assessment

import com.nyansapoai.teaching.domain.models.assessments.AssignedStudentDto
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent

fun AssignedStudentDto.toNyansapoStudent(): NyansapoStudent {
    return NyansapoStudent(
        id = id,
        baseline = baseline,
        grade = grade,
        group = group,
        name = name,
        sex = sex,
        first_name = first_name,
        last_name = last_name
    )
}
