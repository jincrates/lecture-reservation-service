package me.jincrates.lecturereservationservice.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "lecture")
class Lecture (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val room: Room,  //강연장

    @Column(nullable = false)
    val lecturerName: String,  //강연자명

    @Column(nullable = false)
    val numberOfReservations: Int,  //예약인원

    @Column(nullable = false)
    val openedAt: LocalDateTime,   //강연시작일시

    @Column(nullable = false)
    val closedAt: LocalDateTime,   //강연종료일시

    @Column(nullable = false)
    val title: String,   //강연 제목

    @Lob @Basic(fetch = FetchType.EAGER)
    val description: String,  //상세 내용

    @OneToMany(mappedBy = "lecture", fetch = FetchType.EAGER)
    val reservations: MutableList<Reservation> = mutableListOf()

) : BaseEntity()