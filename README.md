# Edugether
An application for academic members of a school. Implemented are Vertx, Hibernate, bcrpyt.

## 프로젝트 개요 
 강사의 강좌 개설 및 관리 기능 제공, 학생의 수강 신청 기능과 수업 리뷰  작성 기능 구현. 서비스 이용자 간 소셜 기능 제공 및 사용자 친화적인 반응형 인터페이스 구현 


## 사용자 및 기능  

- 공통기능

  - 회원 가입, 로그인, 로그아웃 
  - 강의 조회 
  - 강의 상세 및 리뷰 조회
  - 강의 리뷰 `like` 요청
  - 타 사용자에 `follow` 요청
  - 타 사용자의 `follow` 요청 수락, 거절 

- 강사

  - 강의 등록 
  - 강의 등록 상태 변경 
  - `Qualification` 등록 

- 학생 

  - 강의 신청 
  - 강의 신청 상태 변경
  - 강의 위시리스트 등록 
  - 강의 리뷰 작성 
  - `Interest` 등록 

## 성취 내용 

- 다양한 JPA 연관 관계를 활용한 데이터 모델 설계 
- 비동기 프로그래밍을 통한 반응형 사용자 인터페이스 구현
- Vert.x 툴킷을 이용한 MVC 패턴의 애플리케이션 구현 및 의존성 주입  
- JPA / Hibernate 기반 요구사항 구현, 도메인 주도 개발에 대한 이해 확장  
- JUnit5 유닛 테스트, JMH 벤치마크 성능 테스트 