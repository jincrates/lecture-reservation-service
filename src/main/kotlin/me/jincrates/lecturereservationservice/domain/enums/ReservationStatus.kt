package me.jincrates.lecturereservationservice.domain.enums

enum class ReservationStatus {

    APPROVAL, WAITING, CANCEL;

    companion object {
        operator fun invoke(status: String) = valueOf(status.uppercase())
    }
}
