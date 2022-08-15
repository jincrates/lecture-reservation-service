package me.jincrates.reservation.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "lecture")
class Lecture (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    var room: Room,  //강연장

    @Column(nullable = false)
    var title: String,   //강연 제목

    @Column(nullable = false)
    @Lob @Basic(fetch = FetchType.EAGER)
    var description: String,  //상세 내용

    @Column(nullable = false)
    var lecturerName: String,  //강연자명

    @Column(nullable = false)
    var limitOfReservations: Int,  //예약(신청)마감인원

    @Column(nullable = false)
    var openedAt: LocalDateTime,   //강연 시작일시

    @Column(nullable = false)
    var closedAt: LocalDateTime,   //강연 종료일시

    @Column(nullable = false)
    var createdBy: String,       //생성자 사번

    //하나의 강의에 여러 예약이 있을 수 있기 떄문
    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    val reservations: MutableSet<Reservation> = mutableSetOf(),

    ) : BaseEntity()