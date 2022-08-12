package me.jincrates.reservation.domain.enums

enum class CommonStatus {

    ACTIVE, INACTIVE;

    // 생성자를 사용한 것처럼 사용할 수 있다.
    // CommonStatus("active")  => ACTIVE
    companion object {
        operator fun invoke(status: String) = valueOf(status.uppercase())
    }
}