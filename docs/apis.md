# API Reference
- 강연장, 강연, 예약에 대한 API 구현
- 필수 구현 API 목록에는 ![필수](https://img.shields.io/badge/필수-FF0000.svg) 표기
- [Swagger 스크린샷 보기](#swagger-ui-screenshot)
<br/><br/>

## 강연장 API
| Method | URI | Description | Request Body | Response Body | etc |
| --- | --- | --- | --- | --- | --- |
| GET | /api/v1/admin/rooms ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 강연장 전체 조회 | - | RoomResponse | status를 경로에 입력하면 사용하지 않는 강연장을 확인할 수 있습니다.(기본값은 ACTIVE) |
| GET | /api/v1/admin/rooms/{roomId} | 강연장 상세 조회 | - | RoomResponse | - |
| POST | /api/v1/admin/rooms ![필수](https://img.shields.io/badge/필수-FF0000.svg)  | 강연장 등록 | RoomRequest | RoomResponse | - |
| PUT | /api/v1/admin/rooms/{roomId} | 강연장 수정 | RoomRequest | RoomResponse | 권한 관련 하단 참조 |
| PUT | /api/v1/admin/rooms/{roomId}/{status} | 강연장 상태 수정 | RoomRequest | RoomResponse | 권한 관련 하단 참조 |
| DELETE | /api/v1/admin/rooms/{roomId} | 강연장 삭제 | - | - | 204 NO_CONTENT |
<br/>

## 강연 API
| Method | URI | Description | Request Body | Response Body | etc |
| --- | --- | --- | --- | --- | --- |
| GET | /api/v1/rooms/{roomId}/lectures ![필수](https://img.shields.io/badge/필수-FF0000.svg)| 강연 전체 조회 | - | LectureResponse | 강연 전체 목록을 조회합니다. 조회기간 조건에 따라 조회 범위를 지정할 수 있습니다. ex) 강의 시작일자 기준: 7일 전 ~ 1일 후까지의 강연 리스트 조회 |
| GET | /api/v1/rooms/{roomId}/lectures/popular ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 인기 강연 조회 | - | LecturePopularResponse | 최근 3일간 가장 신청이 많은 강연순으로 리스트를 조회합니다. |
| GET | /api/v1/rooms/{roomId}/lectures/{lectureId} | 강연 상세 조회 | - | LectureResponse | - |
| GET | /api/v1/rooms/{roomId}/lectures/{lectureId}/users | 강연 신청자 사번 리스트 조회 | - | LectureUsersResponse | 강의를 신청한 사용자 사번리스트를 조회합니다. |
| POST | /api/v1/rooms/{roomId}/lectures ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 강연 등록 | LectureRequest | LectureResponse | 권한관련 하단 참조 |
| PUT | /api/v1/rooms/{roomId}/lectures/{lectureId} | 강연장 수정 | LectureRequest | LectureResponse | - |
| DELETE | /api/v1/rooms/{roomId}/lectures/{lectureId} | 강연장 삭제 | - | - | 204 NO_CONTENT |
<br/>

## 예약 API
| Method | URI | Description | Request Body | Response Body | etc |
| --- | --- | --- | --- | --- | --- |
| GET | /api/v1/lectures/{lectureId}/reservations/{reservationId} | 예약 상세 조회 | - | ReservationResponse | - |
| GET | /api/v1/lectures/{lectureId}/reservations/users/{userId} ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 신청자 사번으로 예약 조회 | - | ReservationDetailResponse | - |
| POST | /api/v1/lectures/{lectureId}/reservations | 예약 등록 | ReservationRequest | ReservationResponse | 권한관련 하단 참조 |
| PUT | /api/v1/lectures/{lectureId}/reservations/{reservationId} | 예약 수정 | ReservationRequest | 수ReservationResponse | - |
| PUT | /api/v1/lectures/{lectureId}/reservations/{reservationId}/approval | 예약 승인 | ReservationRequest | ReservationResponse | - |
| PUT | /api/v1/lectures/{lectureId}/reservations/{reservationId}/waiting | 예약 대기 | ReservationRequest | ReservationResponse | - |
| PUT | /api/v1/lectures/{lectureId}/reservations/{reservationId}/cancel ![필수](https://img.shields.io/badge/필수-FF0000.svg) | 예약 취소 | ReservationRequest | ReservationResponse | - |
| DELETE | /api/v1/lectures/{lectureId}/reservations/{reservationId} | 예약 삭제 | - | - | 204 NO_CONTENT |

<br/>

## Request

### RoomRequest 예시
```
{
    "title": "강연장명",  
    "limitOfPersons": "99",
    "status": "active"
}
```
- 강연장명, 수용인원, 상태(ACTIVE/INACTIVE)를 입력받습니다.(status 생략 가능)
- `title`: 공백일 수 없고, 50자 제한입니다.
- `limitOfPersons`: 1 이상이고, 수정시 신청된 강연의 예약 마감인원보다 크거나 같아야 합니다.  
<br/>

### LectureRequest 예시
```
{
    "title": "강연 제목",
    "description": "강연 상세내용",
    "lecturerName": "강연자명",
    "limitOfReservations": "99",
    "openedAt": "2022-12-31 09:00:00",
    "closedAt": "2022-12-31 12:00:00"
}
```
- 강연제목, 상세내용, 강연자 이름, 예약 마감인원, 강의 시작일시, 강의 종료일시를 입력받습니다.
- `title`: 공백일 수 없고, 50자 제한입니다.
- `description`: 공백일 수 없습니다.
- `lectureName`: 공백일 수 없습니다.
- `limitOfReservations`: 1 이상이고, 수정시 승인된 신청자 수보다 크거나 같아야 합니다.
- `openedAt`: yyy-MM-dd HH:mm:ss 포맷입니다.
- `closedAt`: yyy-MM-dd HH:mm:ss 포맷이고, 시작일시 이전으로 지정할 수 없습니다.
<br/>

### ReservationRequest 예시
```
{
    "userId": "12345",
    "status": "approval"
}
```
- 신청자 사번, 예약 상태(approval/waiting/canel)를 입력받습니다.(status 생략 가능)
- `userId`: 공백일 수 없고, 5자여야합니다.
<br/><br/><br/>


## Swagger UI Screenshot
![localhost_8080_swagger-ui_index html](https://user-images.githubusercontent.com/53418946/184538195-82c5583b-cbb4-42ed-8e20-b15a65d1057c.png)
