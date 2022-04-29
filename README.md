# Web-Project-Danawa
다나와 사이트를 참고하여 게시판과 노트북 상세 검색을 구현한 웹 프로젝트입니다.
</br></br>
  
## 개발 환경 및 사용 기술
OS: Window  
IDE: Eclipse  
Front: JavaScript, jQuery, Thymeleaf  
Back: Java 11, SpringBoot, Gradle, Spring Data JPA, H2 Database  
크롤링: Selenium
</br></br>

## 주요 기능
노트북 상세 검색 (옵션 체크, 키워드 검색, 가격 범위 설정)  
노트북 가격 비교  
노트북 관심 상품 등록  
회원가입, 로그인  
게시판 글 작성, 수정, 삭제, 이미지 첨부  
게시물 추천 기능, 댓글 기능  
페이징, 정렬, 검색 기능
</br></br>

## 애플리케이션 구조
controller: 웹 계층  
service: 비즈니스 로직, 트랜잭션 처리  
repository: JPA를 직접 사용하는 계층  
domain: 엔티티가 모여 있는 계층, 모든 계층에서 사용
</br></br>

## Class Diagram
![Class Diagram](https://user-images.githubusercontent.com/103295635/165959951-dfc5db07-1647-47b7-9b34-417c2d86300f.jpg)
회원이 탈퇴해도 게시물과 댓글을 남기기 위해 Post, Comment는 Member와 연관관계를 맺지 않았습니다.
<br/><br/>

## 프로젝트 기간
2022-03-14 ~ 2022-04-28
</br></br>
   
## 데이터 출처
http://www.danawa.com/
<br/><br/>

## 프로젝트 시연 영상
유튜브로 연결됩니다.
#### 노트북: 상세검색, 관심상품, 상세보기
[![Video Label](http://img.youtube.com/vi/bZnr_pLLitg/0.jpg)](https://youtu.be/bZnr_pLLitg)
<br/><br/>
#### 회원: 회원가입, 로그인, 회원정보수정, 회원탈퇴
[![Video Label](http://img.youtube.com/vi/mlndTOWwJME/0.jpg)](https://youtu.be/mlndTOWwJME)
<br/><br/>
#### 자유게시판: 글 작성, 수정, 삭제, 검색, 내가 쓴 글
[![Video Label](http://img.youtube.com/vi/Hxb9mSJKvJk/0.jpg)](https://youtu.be/Hxb9mSJKvJk)
