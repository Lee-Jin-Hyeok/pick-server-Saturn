package com.dsm.pick.service

import com.dsm.pick.domain.*
import com.dsm.pick.domain.attribute.Floor
import com.dsm.pick.domain.attribute.Period
import com.dsm.pick.domain.attribute.Schedule
import com.dsm.pick.domain.attribute.State
import com.dsm.pick.exception.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

internal class AttendanceServiceTest {

    @Test
    fun `동아리 출석부 네비게이션 반환 OK`() {
        val attendanceNavigation = attendanceService.showAttendanceNavigation(
            schedule = Schedule.CLUB,
            floor = Floor.THREE,
            date = LocalDate.of(2021, 1, 1),
        )

        assertThat(attendanceNavigation.date).isEqualTo("0101")
        assertThat(attendanceNavigation.dayOfWeek).isEqualTo("금")
        assertThat(attendanceNavigation.schedule).isEqualTo("club")
        assertThat(attendanceNavigation.teacherName).isEqualTo("teacherName")
        assertThat(attendanceNavigation.locations).map<String> { it.name }.containsAll(listOf("테스트동아리"))
    }

    @Test
    fun `교실 출석부 네비게이션 반환 OK`() {
        val attendanceNavigation = attendanceService.showAttendanceNavigation(
            schedule = Schedule.SELF_STUDY,
            floor = Floor.THREE,
            date = LocalDate.of(2021, 1, 2),
        )

        assertThat(attendanceNavigation.date).isEqualTo("0102")
        assertThat(attendanceNavigation.dayOfWeek).isEqualTo("토")
        assertThat(attendanceNavigation.schedule).isEqualTo("self-study")
        assertThat(attendanceNavigation.teacherName).isEqualTo("teacherName")
        assertThat(attendanceNavigation.locations).map<String> { it.name }.containsAll(listOf("테스트교실"))
    }

    @Test
    fun `출석부 네비게이션 반환 중 일정이 없는 날짜 ActivityNotFoundException`() {
        assertThrows<ActivityNotFoundException> {
            attendanceService.showAttendanceNavigation(
                Schedule.CLUB,
                Floor.THREE,
                LocalDate.of(2003, 8, 16),
            )
        }
    }

    @Test
    fun `동아리 출석 현황 반환 OK`() {
        val attendance = attendanceService.showAttendance(
            schedule = Schedule.CLUB,
            floor = Floor.THREE,
            priority = 0,
            date = LocalDate.of(2021, 1, 1)
        )
        println(attendance.attendances.first())
        assertThat(attendance.clubHead).isEqualTo("3417 이진혁")
        assertThat(attendance.name).isEqualTo("테스트동아리")
        assertThat(attendance.attendances).map<String> { it.studentNumber }.containsAll(listOf("3417"))
    }

    @Test
    fun `교실 출석 현황 반환 OK`() {
        val attendance = attendanceService.showAttendance(
            schedule = Schedule.SELF_STUDY,
            floor = Floor.THREE,
            priority = 0,
            date = LocalDate.of(2021, 1, 2)
        )
        println(attendance.attendances.first())
        assertThat(attendance.clubHead).isNull()
        assertThat(attendance.name).isEqualTo("테스트교실")
        assertThat(attendance.attendances).map<String> { it.studentNumber }.containsAll(listOf("3417"))
    }

    @Test
    fun `존재하지 않는 동아리 출석 현황 반환 Club Not Found Exception`() {
        assertThrows<ClubNotFoundException> {
            attendanceService.showAttendance(
                schedule = Schedule.CLUB,
                floor = Floor.TWO,
                priority = 10,
                date = LocalDate.of(2021, 1, 1)
            )
        }
    }

    @Test
    fun `존재하지 않는 교실 출석 현황 반환 Classroom Not Found Exception`() {
        assertThrows<ClassroomNotFoundException> {
            attendanceService.showAttendance(
                schedule = Schedule.SELF_STUDY,
                floor = Floor.TWO,
                priority = 0,
                date = LocalDate.of(2021, 1, 2)
            )
        }
    }

    @Test
    fun `존재하지 않는 일정의 출석 현황 반환 Non Exist Schedule Exception`() {
        assertThrows<NonExistScheduleException> {
            attendanceService.showAttendance(
                schedule = Schedule.AFTER_SCHOOL,
                floor = Floor.THREE,
                priority = 0,
                date = LocalDate.of(2021, 1, 1)
            )
        }
    }

    @Test
    fun `출석 상태 변환 OK`() {
        attendanceService.updateAttendance(
            studentNumber = "3417",
            period = Period.EIGHT,
            attendanceState = State.MOVE,
            attendanceDate = LocalDate.of(2021, 1, 1)
        )
    }

    @Test
    fun `출석 상태 변환 중 해당하는 출석부를 찾을 수 없음 Attendance Not Found Exception`() {
        assertThrows<AttendanceNotFoundException> {
            attendanceService.updateAttendance(
                studentNumber = "잘못된 번호",
                period = Period.EIGHT,
                attendanceState = State.MOVE,
                attendanceDate = LocalDate.of(2021, 1, 1)
            )
        }
    }

    @Test
    fun `메모 변환 OK`() {
        attendanceService.updateMemo(
            studentNumber = "3417",
            period = Period.EIGHT,
            attendanceMemo = "안뇽",
            attendanceDate = LocalDate.of(2021, 1, 1)
        )
    }

    @Test
    fun `메모 변환 중 해당하는 출석부를 찾을 수 없음 Attendance Not Found Exception`() {
        assertThrows<AttendanceNotFoundException> {
            attendanceService.updateMemo(
                studentNumber = "잘못된 번호",
                period = Period.EIGHT,
                attendanceMemo = "안뇽",
                attendanceDate = LocalDate.of(2021, 1, 1)
            )
        }
    }

    private val teacher = Teacher(
        id = "teacherId",
        password = "teacherPassword",
        name = "teacherName",
        office = "teacherOffice",
    )

    private val club = Club(
        name = "테스트동아리",
        location = Location(
            location = "정보보안2실",
            floor = Floor.THREE,
            priority = 0,
        ),
        head = "3417 이진혁",
        teacher = "teacherId",
    )

    private val classroom = Classroom(
        name = "테스트교실",
        floor = Floor.THREE,
        priority = 0,
        manager = "teacherId",
    )

    private val student = Student(
        number = "3417",
        name = "이진혁",
        club = club,
        classroom = classroom,
        isMondaySelfStudy = false,
        isTuesdaySelfStudy = false,
    )

    private val clubActivity = Activity(
        date = LocalDate.of(2021, 1, 1),
        schedule = Schedule.CLUB,
        secondFloorTeacher = teacher,
        thirdFloorTeacher = teacher,
        forthFloorTeacher = teacher,
    )

    private val classroomActivity = Activity(
        date = LocalDate.of(2021, 1, 2),
        schedule = Schedule.SELF_STUDY,
        secondFloorTeacher = teacher,
        thirdFloorTeacher = teacher,
        forthFloorTeacher = teacher,
    )

    private val clubAttendance = Attendance(
        id = 1,
        activity = clubActivity,
        student = student,
        period = Period.EIGHT,
        teacher = teacher,
        state = State.ATTENDANCE,
        memo = null,
    )

    private val classroomAttendance = Attendance(
        id = 1,
        activity = classroomActivity,
        student = student,
        period = Period.EIGHT,
        teacher = teacher,
        state = State.MOVE,
        memo = "Undefined 놀러감",
    )

    private val attendanceService = AttendanceService(
        timeService = TimeService(),
        activityRepository = mock {
            on { findActivityByDate(LocalDate.of(2021, 1, 1)) } doReturn clubActivity
            on { findActivityByDate(LocalDate.of(2021, 1, 2)) } doReturn classroomActivity
        },
        clubRepository = mock {
            club.students = listOf(student)
            on { findByLocationFloor(Floor.THREE) } doReturn listOf(club)
            on { findByLocationFloorAndLocationPriority(Floor.THREE, 0) } doReturn club
        },
        classroomRepository = mock {
            classroom.students = listOf(student)
            on { findByFloor(any()) } doReturn listOf(classroom)
            on { findByFloorAndPriority(Floor.THREE, 0) } doReturn classroom
        },
        attendanceRepository = mock {
            on { findByStudentNumberAndPeriodAndActivityDate("3417", Period.EIGHT, LocalDate.of(2021, 1, 1)) } doReturn clubAttendance
            on { findByStudentClassroomFloorAndStudentClassroomPriorityAndActivityDate(Floor.THREE, 0, LocalDate.of(2021, 1, 2)) } doReturn listOf(classroomAttendance)
            on { findByStudentClubLocationFloorAndStudentClubLocationPriorityAndActivityDate(Floor.THREE, 0, LocalDate.of(2021, 1, 1)) } doReturn listOf(clubAttendance)
        },
    )
}