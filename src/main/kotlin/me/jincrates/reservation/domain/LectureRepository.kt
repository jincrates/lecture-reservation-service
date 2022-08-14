package me.jincrates.reservation.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
interface LectureRepository : JpaRepository<Lecture, Long> {

    fun findByTitle(title: String): Lecture?

    fun findAllByRoomIdOrderByCreatedAtDesc(roomId: Long): List<Lecture>?

    fun existsByRoomIdAndOpenedAtBetween(roomId: Long, openedAt: LocalDateTime, closedAt: LocalDateTime): Boolean

    fun findByRoomIdAndOpenedAtBetween(roomId: Long, fromDate: LocalDateTime, toDate: LocalDateTime): List<Lecture>?

    @Query(" with temp_popular_lecture as ( " +
            " select lecture_id, count(lecture_id) as 'reservation_count' " +
            " from reservation " +
            " where created_at > date_sub(convert(curdate(), datetime), interval 3 day) " +
            " and status in ('APPROVAL', 'WAITING') " +
            " group by lecture_id " +
            " order by reservation_count desc " +
            " ) " +
            " select pl.reservation_count, l.* " +
            " from temp_popular_lecture pl " +
            " left outer join lecture l on pl.lecture_id = l.id ", nativeQuery = true)
    fun findByRoomIdWithMostPopularFor3days(roomId: Long): List<Lecture>?

}