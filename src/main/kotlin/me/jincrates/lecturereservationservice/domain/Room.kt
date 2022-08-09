package me.jincrates.lecturereservationservice.domain

import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
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
    var status: CommonStatus = CommonStatus.ACTIVE  // 상태: 기본값(ACTIVE)

) : BaseEntity()