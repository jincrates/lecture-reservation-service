# API Reference
* 강연장, 강연, 예약에 대한 API 구현
* 필수 구현 API 목록에는 ![필수](https://img.shields.io/badge/필수-FF0000.svg) 표기


## 강연장 API
| Method | URI | Description | Request Body | Response Body | etc |
| --- | --- | --- | --- | --- | --- |
| GET | /api/v1/admin/rooms ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 강연장 전체 조회 | - | 강연장 목록 | status를 경로에 입력하면 사용하지 않는 강연장을 확인할 수 있습니다.(기본값은 ACTIVE) |
| GET | /api/v1/admin/rooms/{roomId} | 강연장 상세 조회 | - | RoomResponse | - |
| POST | /api/v1/admin/rooms ![필수](https://img.shields.io/badge/필수-FF0000.svg)  | 강연장 등록 | RoomRequest | RoomResponse | - |
| PUT | /api/v1/admin/rooms/{roomId} | 강연장 수정 | RoomRequest | RoomResponse | 권한 관련 하단 참조 |
| PUT | /api/v1/admin/rooms/{roomId}/{status} | 강연장 상태 수정 | RoomRequest | RoomResponse | 권한 관련 하단 참조 |
| DELETE | /api/v1/admin/rooms/{roomId} | 강연장 삭제 | - | - | 204 NO_CONTENT |

## 강연 API
| Method | URI | Description | Request Body | Response Body | etc |
| --- | --- | --- | --- | --- | --- |
| GET | /api/v1/rooms/{roomId}/lectures ![필수](https://img.shields.io/badge/필수-FF0000.svg)| 강연 전체 조회 | - | 강연 목록 | 강연 전체 목록을 조회합니다. 조회기간 조건에 따라 조회 범위를 지정할 수 있습니다. ex) 강의 시작일자 기준: 7일 전 ~ 1일 후까지의 강연 리스트 조회 |
| GET | /api/v1/rooms/{roomId}/lectures/{lectureId} | 강연 상세 조회 | - | 강연 정보 |  |
| GET | /api/v1/rooms/{roomId}/lectures/{lectureId}/users | 강연 신청자 사번 리스트 조회 | - | 강연 목록 |  |
| GET | /api/v1/rooms/{roomId}/lectures/popular ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 인기 강연 조회 | - | 강연 목록 |  |
| POST | /api/v1/rooms/{roomId}/lectures ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 강연 등록 | - | 강연 정보 | - |
| PUT | /api/v1/rooms/{roomId}/lectures/{lectureId} | 강연장 수정 | - | 수정된 강연장 정보 |  |
| DELETE | /api/v1/rooms/{roomId}/lectures/{lectureId} | 강연장 삭제 | - | - | 204 NO_CONTENT |

## 예약 API
| Method | URI | Description | Request Body | Response Body | etc |
| --- | --- | --- | --- | --- | --- |
| GET | /api/v1/lectures/{lectureId}/reservations/{reservationId} | 예약 상세 조회 | - | 강연장 정보 |  |
| GET | /api/v1/lectures/{lectureId}/reservations/{reservationId}/users/{userId} ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 신청자 사번으로 예약 조회 | - | 강연장 정보 |  |
| POST | /api/v1/lectures/{lectureId}/reservations | 예약 등록 | -  | 강연장 정보 | - |
| PUT | /api/v1/lectures/{lectureId}/reservations/{reservationId} | 예약 수정 | RoomRequest | 수정된 강연장 정보 |  |
| PUT | /api/v1/lectures/{lectureId}/reservations/{reservationId}/approval | 예약 승인 | RoomRequest | RoomRequest |  |
| PUT | /api/v1/lectures/{lectureId}/reservations/{reservationId}/waiting | 예약 대기 | RoomRequest | RoomRequest |  |
| PUT | /api/v1/lectures/{lectureId}/reservations/{reservationId}/cancel ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 예약 취소 | RoomRequest | RoomRequest |  |
| DELETE | /api/v1/lectures/{lectureId}/reservations/{reservationId} | 예약 삭제 | - | - | 204 NO_CONTENT |
