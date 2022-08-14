# lecture-reservation-service
`코틀린, 스프링부트, JPA`를 사용한 사내 강연 예약서비스


## 기술스택
![Generic badge](https://img.shields.io/badge/1.6.21-kotlin-7F52FF.svg)
![Generic badge](https://img.shields.io/badge/17-OpenJDK-537E99.svg)
![Generic badge](https://img.shields.io/badge/2.7.2-SpringBoot-6DB33F.svg)
![Generic badge](https://img.shields.io/badge/8.0-MySQL-01578B.svg)
![Generic badge](https://img.shields.io/badge/5.0-JUnit-DD524A.svg)

## 기능소개
작성예정

## ERD 설계
<img width="980" alt="ERD 설계" src="https://user-images.githubusercontent.com/53418946/184529238-712b27ab-5449-4533-aabd-2828475b2b49.png">

## 디렉토리 구조
```
├── docs                            (API Reference, 에러코드, DB 스키마 이미지 등)
├── scripts                         (DB 스키마 및 테스트 데이터 스크립트)
/src/main
├── resources
│   ├── application.yml             (프로젝트 관련 설정 파일)
│   └── application-dev.yml         (테스트코드 관련 설정 파일)
└── kotlin/me/jincrates/reservation
    ├── config                      (인증관련 설정, Swagger 설정)
    ├── domain                      (Entity, Repository 관리 - DB 테이블과 entity를 1:1 매칭)
    │   ├── eunums                  (Entity 상태처리에 대한 enum 파일)
    │   └── validator               (@Valid 어노테이션 외에 Request 객체에 대한 유효성 검증)
    ├── controller                  (API 엔드포인트를 명시하는 컨트롤러)
    ├── exception                   (글로벌 에러 처리 핸들러 및 Exception 처리 포함)
    ├── model                       (Request, Response 객체 관리)
    ├── service                     (controller와 repository를 연결, 비즈니스 로직 처리)
    ├── web                         (API 엔드포인트를 명시하는 컨트롤러)
    └── App.kt                      (애플리케이션 실행)
``` 

## API Reference
[API 문서](https://github.com/jincrates/lecture-reservation-service/blob/main/docs/apis.md) 를 참조 
