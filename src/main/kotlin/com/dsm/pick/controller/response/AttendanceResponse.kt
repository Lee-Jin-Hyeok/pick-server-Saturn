package com.dsm.pick.controller.response

import com.dsm.pick.domain.Teacher
import com.fasterxml.jackson.annotation.JsonProperty

data class AttendanceResponse(
    val attendances: List<StudentState>,
    @get:JsonProperty("head")
    val clubHead: String?,
    val name: String,
    val managerTeacher: String?,
) {

    class StudentState(
        @get:JsonProperty("gradeClassNumber")
        val studentNumber: String,
        @get:JsonProperty("name")
        val studentName: String,
        val state: State,
        val memo: Memo,
        val reason: String?,
    ) {

        class State(
            val seven: String?,
            val eight: String?,
            val nine: String?,
            val ten: String?,
        )

        class Memo(
            @get:JsonProperty("sevenMemo")
            val seven: String?,
            @get:JsonProperty("eightMemo")
            val eight: String?,
            @get:JsonProperty("nineMemo")
            val nine: String?,
            @get:JsonProperty("tenMemo")
            val ten: String?,
        )
    }
}