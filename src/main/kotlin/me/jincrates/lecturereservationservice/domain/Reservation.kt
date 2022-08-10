package me.jincrates.lecturereservationservice.domain

import me.jincrates.lecturereservationservice.domain.enums.ReservationStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "reservation")
class Reservation(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    var lecture: Lecture,  //강연정보

    @Column(nullable = false)
    var userId: String,   //신청자 사번

    @Column(nullable = false)
    var reservedAt: LocalDateTime,   //예약일자

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ReservationStatus,   //예약상태

) : BaseEntity()
