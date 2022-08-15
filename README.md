# 사내 강연 예약서비스
- `코틀린, 스프링부트, JPA`를 사용한 사내 강연 예약서비스입니다.
- 강연장을 등록하고, 해당 강연장 규모에 맞는 강연을 신청할 수 있습니다.
- 신청된 강연에는 해당 예약마감 인원 수에 맞게 예약 신청을 할 수 있습니다.
<br/>

## 기술스택
![Generic badge](https://img.shields.io/badge/1.6.21-kotlin-7F52FF.svg)
![Generic badge](https://img.shields.io/badge/17-OpenJDK-537E99.svg)
![Generic badge](https://img.shields.io/badge/2.7.2-SpringBoot-6DB33F.svg)
![Generic badge](https://img.shields.io/badge/8.0-MySQL-01578B.svg)
![Generic badge](https://img.shields.io/badge/5.0-JUnit-DD524A.svg)

<br/><br/>

## ERD 설계
<img width="980" alt="ERD 설계" src="https://user-images.githubusercontent.com/53418946/184529238-712b27ab-5449-4533-aabd-2828475b2b49.png">
<br/>

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
<br/>

## API Reference
[API 문서](https://github.com/jincrates/lecture-reservation-service/blob/main/docs/apis.md)를 참조하시길 바랍니다. 

<br/>

## 과제를 하면서 🤔 

### 구현하면서 어려웠던 점
#### 1. 동시성 이슈
[추가사항]으로 있는 "`동시성 이슈를 고려하여 구현, 데이터 일관성 고려`" 이 부분이 어려웠습니다. 솔직하게 말해서 평소 개발할 때 동시성 이슈에 대한 고려하지 않고 개발을 했었습니다.(그룹웨어 특성상 개별 사용자가 프로세스 처리하는 환경이지, 동시에 결재상신 한다던지 연차를 상신한다고해서 데이터 일관성이 깨지는 경우가 없었습니다.)   
일반적으로 update 하는 과정에서 트랜잭션을 유지하며 데이터 간에 정합성을 유지하려고 했지, update에 처리 이전에 읽어온 데이터에 대해서는 신경을 쓰지 못했습니다.   
과제를 진행하는 과정에서 동시성 문제가 무엇인지부터 찾아보았고, 이를 해결하기 위해서 어떤 방법들이 있는지를 확인하여 실습을 해보았습니다.([예약시스템으로 통해 알아보는 동시성 문제](https://github.com/jincrates/kotlin-workspace/tree/main/concurrency-problems)) 시간을 많이 잡아먹긴 했지만, 이번 기회에 새로운 사실을 배울 수 있었습니다. 

#### 2. N+1의 문제
JPA를 사용하여 기능구현을 하였고, 이후 쿼리 튜닝을 위해 하나하나 살펴보다가 N+1 문제가 발생하였습니다. 데이터를 가져온 데이터 자체는 조건에 맞았으나, 하위 데이터를 가져오면서 불필요한 반복이 발생하였습니다. 이를 해결하기 위해 fetch join 등의 방법들을 적용해보았으나, 잘 되지 않았습니다. 아무래도 JPA에 대한 지식이 부족하여 좀 더 학습이 필요하다고 생각되었습니다. 

### 보완이 필요한 점
사용자 정보를 따로 만들어두지 않았기 때문에 인증처리, 권한에 대해서 구현을 하지 않았습니다. 현재는 URI상으로 구분이 되어있지만, BackOffice와 Front 모두 호출이 가능합니다. 인증처리와 권한 체크부분에 대해서 보완이 필요할 것으로 보입니다.   
