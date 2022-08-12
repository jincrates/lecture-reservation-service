package me.jincrates.reservation.domain

import me.jincrates.reservation.domain.enums.CommonStatus
import javax.persistence.*

@Entity
@Table(name = "room")
class Room (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,  // 강연장 id

    @Column(nullable = false)
    var title: String,  //강연장 명칭

    @Column(nullable = false)
    var limitOfPersons: Int,  // 수용인원

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: CommonStatus,  // 상태

    @Column(nullable = false)
    var createdBy: String,   //생성자 사번

    @OneToMany(fetch = FetchType.EAGER)
    val lectures: MutableList<Lecture> = mutableListOf(),

) : BaseEntity()