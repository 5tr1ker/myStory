# 📎myStory ( https://mystorynews.com )
> 웹 서버의 기본 소양이 되는 게시판 프로젝트입니다.

![img1](https://user-images.githubusercontent.com/49367338/196038573-aee974a6-edba-4118-9c29-e71a518ce945.png)

<h2>Project Introduce</h2>
백엔드의 기초를 다지기 위해 시작한 개인 프로젝트입니다.<br/>
관련 기술들을 학습하고 활용한 결과물이며, 각 기술이 어떻게 동작하고 어디에 활용되는지 공부할 수 있었던 프로젝트입니다.<br/>
프로젝트 진행 도중 많은 시행착오가 있었지만, 문제를 해결하는 능력과 구현 능력을 키울 수 있었습니다.<br/>

<h3># 주요 기능</h3>
프로젝트 주요 기능은 다음과 같습니다.<br/><br/>

- 게시판 : CRUD 기능, 조회수 , 추천 , 페이징 및 검색 처리
- 사용자 : Security 회원가입 및 로그인 ( JWT ) , OAuth 2.0 (구글, 네이버) , 회원정보 수정, 회원가입시 중복 검사 , 알림
- 댓글 : CRUD 기능
- 보안 : SSL 인증서를 이용해 https 적용 , RefreshToken는 클라이언트의 접근 제한 , AccessToken 과 RefreshToken 를 발급해 API 요청에 대한 권한 확인
- 최적화 : 수정할 수 없는 데이터는 읽기 전용 모드를 이용해 메모리 최적화 , 페이징을 활용해 데이터 조회 속도 개선

<h2>1 . 로그인 / 회원가입</h2>

> 구글 , 네이버 계정으로 회원가입을 하거나 , 별도로 가입할 수 있습니다.

![image](https://user-images.githubusercontent.com/49367338/197676731-9463999d-921d-4f14-8926-702996662002.png)

- Oauth 2.0 를 통해 불필요한 회원가입을 단축시켰습니다.
- 로그인 후 발급되는 Access Token 과 Refresh Token은 클라이언트에서 다음과 같이 보관합니다. 
  - AccessToken은 로컬 변수에 저장해서 외부 접근을 제한합니다.
  - RefreshToken은 쿠키에 보관하나 Security , HTTPOnly 옵션을 추가해서 서버와 클라이언트 간 https 통신 및 자바스크립트로 쿠키 접근을 제한합니다. 
  - 로그아웃시 Refresh Token 쿠키 삭제 및 세션 무효화를 합니다.
  - OAuth 2.0 로그인 시 사용자 이름을 이용해 회원가입 또는 로그인을 진행합니다.
  
<h2>2 . 메인 화면</h2>

> 전체 게시물을 페이지로 나눠서 출력했습니다.

![image](https://user-images.githubusercontent.com/49367338/197678129-d36ca987-ebf2-4803-ab50-3af49db5d28b.png)

- Spring Data JPA 를 이용해 반복적인 CRUD 코드를 최소화 하고 , 페이징을 이용해 성능을 높혔습니다.
- QueryDSL 을 이용해 복잡한 JPQL 작성시 발생할 수 있는 문법오류를 컴파일 시점에 빠르게 찾아 내고 , 단순한 정적 쿼리는 JPQL를 이용해 빠르게 구현했습니다.

<h2>3 . 게시물 조회</h2>

> 사용자가 작성한 게시글을 조회합니다. 하단에 textbox에 댓글을 추가할 수 있고, 본인이 작성한 댓글만 삭제할 수 있습니다. 또한 작성자가 등록한 태그 및 첨부파일을 이용할 수 있습니다.

![image](https://user-images.githubusercontent.com/49367338/197679483-236fd600-419f-468a-a0af-1af995aa6ae9.png)

- 조회는 데이터가 수정되지 않기 때문에 읽기 전용 모드인 ReadOnly를 이용해 영속성 컨텍스트에 있는 변경감지 스냅샷 관리를 제외해 메모리 최적화를 했습니다.

<h2>4 . 게시물 수정 / 작성</h2>

> 태그와 첨부파일을 첨부할 수 있고, 에디터를 이용해 글을 작성 및 수정할 수 있습니다.

![image](https://user-images.githubusercontent.com/49367338/197680769-2354229c-01e5-42f4-935e-f27f3c20df7f.png)

- 수정 시 DB에 있는 Entity를 그대로 가져오기 때문에 작성되어 있는 데이터에 값을 덮어쓰기 할 수 있습니다.
- 쿼리 스트링을 변조하는 불법적인 침입을 금지했습니다.

<h2>5 . 프로필 조회</h2>

> 통계 함수를 이용해 데이터를 조회하고 , 계정 정보 수정 및 탈퇴를 할 수 있습니다.

![image](https://user-images.githubusercontent.com/49367338/197685138-6754e6d7-bdc7-4a5e-857b-40652d4ba761.png)

- 계정이 수정되거나 삭제됐을 때 사용자와 연관된 값을 벌크 연산으로 DB에 값을 직접 변경해줬습니다.
- 변경된 DB는 영속성 컨텍스트와 데이터 불일치 오류가 발생할 수 있어,  @Modifying(clearAutomatically = true)를 이용해 영속성 컨텍스트를 clear 해줬습니다.

<h2>6 . 알림창</h2>

> 내가 작성된 게시물에 등록된 댓글을 알림으로 표현하고, 버튼을 누르면 해당 게시물로 이동합니다.

![image](https://user-images.githubusercontent.com/49367338/197687034-459491f7-7616-463b-af4c-695a7992875b.png)

<h2>Project Structure</h2>

> React ( SPA ) + Spring ( API ) 구조로 개발했습니다.

- React ( SPA )
- Spring ( API Server )
- Spring Security ( Security )
- MySQL ( RDBMS )
- JPA Hibernate & QueryDSL ( ORM )
- OAuth 2.0 & JWT ( Login )
- Docker ( Container )
- AWS EC2 ( Infra )

<h2>React</h2>

> React ( SPA ) 구조로 하나의 페이지에 담아 동적으로 화면을 바꿔 사용자에게 출력했습니다.

- Route path를 설정해서 동적으로 페이지를 출력합니다.

- /login : 로그인페이지
- /register : 회원가입페이지
- /findid : 아이디 찾는 페이지
- /* : 이외 링크는 `<NoticeFrame />` 컴포넌트에 연결합니다.
  - /noticelist : 게시판의 목록을 보여줍니다.
  - /newpost : 새 글을 작성합니다.
  - /modified : 작성된 글을 수정합니다
  - /viewpost : 작성된 글을 조회합니다.
  - /profile : 프로필 화면으로 이동합니다.

<h2>Spring API Server</h2>

> React와 Spring간의 통신은 JSON 형태로 Response 합니다.

프로젝트 구조는 다음과 같습니다.<br/>
- controller : API를 관리합니다.<br/>
- entity<br/>
  - DTO : DTO 파일을 관리합니다.<br/>
  - freeboard : 게시판의 entity를 관리 합니다.<br/>
  - userdata : 사용자 정보 entity를 관리합니다.<br/>
- preferences : Spring Config 와 Security Config를 관리합니다. <br/>
- repository<br/>
  - custom : JPA , QueryDSL 를 관리합니다.<br/>
- security<br/>
  - oauth  : OAuth2 에 관한 기능을 관리합니다.<br/>
  - repository : Security , JWT , OAuth 의 JPA , QueryDSL 를 관리합니다.<br/>
  - service : Security , JWT , OAuth 로직 호출을 관리합니다.<br/>
  - table : JWT 토큰을 저장하는 Entity 입니다.<br/>
- service : 비즈니스 로직 호출을 관리합니다.<br/>

<h2>Spring Security</h2>

> Security 설정을 추가해 인가된 사용자만 특정 API에 접근할 수 있도록 제한합니다. 

프로젝트 구조는 다음과 같습니다.</br>
- CSRF : disable
- Session Creation Policy : STATELESS
- JwtAuthenticationFilter : UsernamePasswordAuthenticationFilter.class
</br>
- 인가가 필요한 API는 .hasRole("USER") 으로 접근 지정하고 그 외 모든 USER 가 접근할 수 있는 API는 .permitAll() 로 설정했습니다.

<h2>JPA & QueryDSL</h2>

> 객체 중심 설계와 반복적인 CRUD를 Spring Data JPA 로 최소화해 비즈니스 로직에 집중합니다.

- JPA : 반복적인 CRUD 작업을 Spring Data JPA 를 이용해서 DB에서 간단하게 데이터를 조회합니다.
- QueryDSL : 복잡한 JPQL 작성시 발생할 수 있는 문법오류를 컴파일 시점에 빠르게 찾아냅니다.
- JPQL : 단순한 정적 쿼리는 JPQL를 이용해 빠르게 구현했습니다. 
</br>
JPA & QueryDSL 패키지 구조는 다음과 같습니다.</br>

- entity ( Entity 테이블 )</br>
- repository ( Spring Data JPA 인터페이스 )</br>
- repository.custom ( JPQL , QueryDSL )</br>

<h2>OAuth2.0 & JWT</h2>

> 구글 & 네이버 OAuth provider를 이용해 불필요한 회원가입을 줄이고 , JWT Token을 이용해 Authorization 인증 시스템을 구현했습니다.

- OAuth & JWT 구조는 다음과 같습니다.
![image](https://user-images.githubusercontent.com/49367338/197803435-6ecd2c22-6be9-41e4-a663-1c368f911726.png)

- Refresh Token은 클라이언트에 httpOnly , Secure 옵션으로 보안처리 했습니다
- Access Token은 클라이언트 로컬 변수에 저장하여, 외부 접근을 차단했습니다.

<h2>Docker</h2>

> 여러 컨테이너를 단일 객체로 안정적으로 확보했습니다.

![도커 다이어그램](https://user-images.githubusercontent.com/49367338/196452012-d1ac40b4-987f-4bb3-8717-33255ce338e9.png)

- 각 컨테이너의 환경을 개발 환경과 동일화합니다.
- volumes 을 이용해 로컬 파일에 미리 생성해둔 환경 설정 파일을 컨테이너와 공유합니다.
- environment 를 이용해 서버에 환경 설정 변수를 지정했습니다.
- links 설정을 이용해 각 컨테이너 끼리 통신할 수 있게 지정했습니다.

<h2>AWS EC2</h2>

> 전체 프로젝트 인프라 구성

![aws 다이어그램](https://user-images.githubusercontent.com/49367338/196463615-0289247c-9a73-4550-b894-e32d97ed8fec.png)

- AWS에 SSL 인증서를 이용해 https 보안설정을 했습니다.
