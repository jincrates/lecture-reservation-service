package me.jincrates.reservation.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.LockModeType

@Transactional(readOnly = true)
interface LectureRepository : JpaRepository<Lecture, Long> {

    fun findByTitle(title: String): Lecture?

    fun findAllByRoomIdOrderByCreatedAtDesc(roomId: Long): List<Lecture>?

    fun existsByRoomIdAndOpenedAtBetween(roomId: Long, openedAt: LocalDateTime, closedAt: LocalDateTime): Boolean

    fun findByRoomIdAndOpenedAtBetween(roomId: Long, fromDate: LocalDateTime, toDate: LocalDateTime): List<Lecture>?

    @Query("select distinct l from Lecture l join fetch l.reservations")
    fun findByRoomIdAndOpenedAtBetweenUsingFetchJoin(roomId: Long, fromDate: LocalDateTime, toDate: LocalDateTime): List<Lecture>?

    @Query(" with temp_popular_lecture as ( " +
            " select lecture_id, count(lecture_id) as 'reservation_count' " +
            " from reservation " +
            " where created_at > date_sub(convert(curdate(), datetime), interval 3 day) " +
            " and status in ('APPROVAL', 'WAITING') " +
            " group by lecture_id " +
            " ) " +
            " select l.* from lecture l " +
            " left outer join temp_popular_lecture pl on l.id = pl.lecture_id " +
            " where l.room_id = :roomId" +
            " order by pl.reservation_count desc, l.opened_at asc ", nativeQuery = true)
    fun findByRoomIdWithMostPopularFor3days(roomId: Long): List<Lecture>?

    @Query("select distinct l from Lecture l left join fetch l.reservations r where l.room = :roomId")
    fun findAllByRoomIdWithReservationUsingFetchJoin(roomId: Long): List<Lecture>?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Lecture l where l.id = :id")
    fun findByIdWithPessimisticLock(id: Long): Lecture?

    @Query("select distinct l from Lecture l left join fetch l.reservations r where l.id = :id")
    fun findByLectureIdWithReservationUsingFetchJoin(id: Long): Lecture?
}