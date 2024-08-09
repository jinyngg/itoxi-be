<div align="center">

  ![image](https://github.com/FINAL-TEAM4/itoxi-be/assets/96164211/bf9e5f39-fe21-42c2-a6f7-35903e1f03ab)

  <!-- ![image](https://github.com/FINAL-TEAM4/itoxi-be/assets/96164211/eb511776-2115-48cd-ac39-efb035d46fee) -->

</div>

</br>

# 📸 프로젝트 

주제 : 챌린지 기반 펫 커뮤니티 </br>
기간 : 2023.09.04 ~ 2023.10.06 </br>
배포 링크: [펫누리](https://petnuri.netlify.app/)

</br>

## 📕 기대 효과

1. 반려동물과의 일상이나 특정 행위를 사용자들이 자발적으로 공유하도록 유도하며 사용자의 참여를 자연스럽게 유도하여 커뮤니티를 활성화 및 콘텐츠 생산 </br>
2. 반려동물과의 일상 공유를 통해 다양한 UGC가 지속적으로 생성, 새로운 사용자 유입을 촉진 

</br>

<!--
## 👨‍👨‍👧‍👦 팀원

## 📑 백엔드

|![](https://avatars.githubusercontent.com/u/104764933?v=4)|![](https://avatars.githubusercontent.com/u/65496092?v=4)|![](https://avatars.githubusercontent.com/u/104916288?v=4)|![](https://avatars.githubusercontent.com/u/96164211?v=4)|![](https://avatars.githubusercontent.com/u/86757234?v=4)|
|:---:|:---:|:---:|:---:|:---:|
|[강경민](https://github.com/redbean00)|[엄채원](https://github.com/chaewon12)|[이지상](https://github.com/matrixpower1004)|[장진영](https://github.com/jinyngg)|[황인영](https://github.com/inyoung0215)|
|카카오 로그인/로그아웃 </br>온보딩, 홈탭(펫 프로필 추가/수정) </br>배송지 목록 조회, 삭제|펫톡 댓글 CRUD </br>펫톡 감정표현 CRUD </br>리워드 챌린지 CRUD|데일리 챌린지 CRUD </br>회원 포인트 CRUD|AWS, Docker 설정 </br> 펫톡 CRUD </br> 리워드 챌린지 CRUD|Oauth 카카오 회원 관리 </br>Security jwt </br>마이페이지 CRUD </br>홈 화면 CRUD|

-->

## 👨‍👨‍👧‍👦 BE Developer

|![](https://avatars.githubusercontent.com/u/104764933?v=4)|![](https://avatars.githubusercontent.com/u/65496092?v=4)|![](https://avatars.githubusercontent.com/u/104916288?v=4)|![](https://avatars.githubusercontent.com/u/96164211?v=4)|![](https://avatars.githubusercontent.com/u/86757234?v=4)|
|:---:|:---:|:---:|:---:|:---:|
|[강경민](https://github.com/redbean00)|[엄채원](https://github.com/chaewon12)|[이지상](https://github.com/matrixpower1004)|[장진영](https://github.com/jinyngg)|[황인영](https://github.com/inyoung0215)|

</br>

## 🏛️ Architecture
![image](https://github.com/user-attachments/assets/5fa6036b-81fa-4cd9-b141-cfe152cdf7e9)

## 🛠️ 구현

#### 펫톡 커뮤니티 기능 구현 (기여도 80%)
1. 펫톡 커뮤니티 엔티티 설계 및 개발
2. 해커 뉴스 랭킹 알고리즘의 계산 결과를 칼럼으로 생성 및 정렬하여 조회 속도를 개선 (데이터 약 300개, 450ms → 60ms)
#### 이벤트 챌린지 기능 구현 (기여도 100%)
1. 이벤트 챌린지 엔티티 설계 및 개발
2. 이벤트 챌린지 리뷰를 작성했을 때, 자동으로 펫톡에 인증글이 등록되도록 구현 (UGC 생산&공급)
3. 출석체크 포인트 지급 구현
#### 민감 정보 관리 (기여도 100%)
1. 서브 모듈을 사용한 민감 정보 관리


</br>

## 💫 Trouble Shooting

- #### [Ranking Algorithm를 통한 인기순 조회 구현(with. View Table)](https://jinyngg.tistory.com/23)
