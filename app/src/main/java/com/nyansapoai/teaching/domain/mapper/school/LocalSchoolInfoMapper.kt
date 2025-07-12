package com.nyansapoai.teaching.domain.mapper.school

import com.nyansapoai.teaching.data.remote.school.LocalSchoolInfo
import database.SchoolInfoEntity

fun SchoolInfoEntity.toLocalSchoolInfo(): LocalSchoolInfo {
    return LocalSchoolInfo(
        organizationUid = organizationUID,
        projectUId = projectUID,
        schoolUId = schoolUID
    )
}