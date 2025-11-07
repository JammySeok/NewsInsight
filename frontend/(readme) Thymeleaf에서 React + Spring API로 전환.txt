React 환경 구성하는 방법
1. 구성할 폴더로 이동
2. 터미널 켜서 아래 명령어 입력 (현제 페이지에 React 환경 구성)
npm create vite@latest .
3. 옵션 선택
- Select a framework: React
- Select a variant: TypeScript + SWC
- Use rolldown-vite (Experimental)?: No
- Install with npm and start now?: Yes
4. 실행 방법 : npm run dev

ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


Thymeleaf에서 React + Spring API로 전환 작업
작업 요약: Thymeleaf에서 React + Spring API로 전환 기존 방식은 Spring(백엔드)이 HTML을 다 만들어서 브라우저에 보내는 방식(Thymeleaf)이었습니다.

새 방식은 React(프론트엔드)가 뼈대만 있는 HTML을 띄우고, 필요한 데이터를 백엔드의 API에 요청해서 받아온 뒤 화면을 동적으로 그리는 방식(SPA)입니다.


1. React 프론트엔드 (frontend 폴더)
"화면 그리기"와 "페이지 이동"을 담당합니다.

package.json (패키지 추가)
- WHAT: react-router-dom, styled-components 라이브러리 외에, typescript, @types/react, @types/styled-components, typescript-eslint 등 TypeScript 관련 라이브러리를 새로 설치했습니다. 또한, 더 빠른 컴파일을 위해 Babel 기반의 @vitejs/plugin-react 대신 **SWC 기반의 @vitejs/plugin-react-swc**로 변경했습니다.
- WHY1: React는 기본적으로 한 페이지만 보여줍니다. (SPA) react-router-dom은 브라우저의 URL 주소(예: /, /login)에 따라 IndexPage나 LoginPage 같은 다른 컴포넌트를 보여주게 하는 "페이지 이동(라우팅)" 필수 라이브러리입니다.
- WHY2: styled-components는 화면을 꾸며주는 여러 기능이 있는 라이브러리입니다.

vite.config.ts (수정)
- WHAT: server.proxy 설정을 추가했습니다.
- WHY: (매우 중요) React(localhost:5173)가 백엔드(localhost:8080)에 API를 요청하면 CORS(보안) 오류가 발생합니다. 이 proxy 설정은 React의 모든 API 요청(예: /api/me, /auth/login)을 Vite 개발 서버가 대신 가로채서 백엔드로 전달해주는 우회로입니다. (CORS 오류 해결!)

tsconfig.json / tsconfig.app.json
- WHAT: TypeScript가 .tsx 코드를 어떻게 이해하고 검사할지 (예: "strict": true 등) [cite: 현재/tsconfig.app.json]가 정의된 TypeScript 설정 파일 입니다.

eslint.config.js
- WHAT: "이 프로젝트는 TypeScript와 React를 사용하니, 관련 규칙으로 코드를 검사할게"라고 설정해 주는 "ESLint(코드 검사기) 설정 파일"입니다.

src/App.tsx (수정)
- WHAT: react-router-dom의 Routes, Route를 사용하도록 수정했습니다. 
- WHY: 기존 Spring IndexController가 하던 URL 관리 역할을 React가 대신하도록 합니다. "/ 주소로 오면 IndexPage를, /login 주소로 오면 LoginPage를 보여줘"라고 지시하는 교통 관제사입니다.

src/page/IndexPage.tsx (신규)
- WHAT: 기존 index.html을 대체하는 React 컴포넌트입니다. useEffect, useState를 사용했습니다. useState에 <User | null>처럼 상태(state)의 타입을 명시하고, fetch로 받아오는 API 응답(예: User DTO)을 interface로 정의하여 코드의 안정성을 높였습니다.
- WHY: (가장 핵심) 페이지가 뜬 직후에 useEffect가 fetch('/api/me') API를 호출합니다.
  - 비로그인 시 (401 응답): user 상태(state)가 null이 되어 "로그인 하세요" 화면을 보여줍니다. (Thymeleaf의 sec:authorize="isAnonymous()" 대체)
  - 로그인 시 (200 OK 응답): 응답으로 온 UserDTO(JSON)를 user 상태에 저장합니다. 화면이 다시 그려지며 "${user.nickname}님 환영합니다"를 보여줍니다. (Thymeleaf의 sec:authentication 대체)

src/page/LoginPage.tsx (신규)
- WHAT: login.html을 대체합니다. fetch로 /auth/login을 호출합니다. TypeScript를 사용하여, handleSubmit 같은 이벤트 핸들러의 매개변수 타입(예: React.FormEvent<HTMLFormElement>)을 명시했습니다.
- WHY: Spring Security는 JSON이 아닌 Form 데이터를 기대합니다. fetch 요청 시 URLSearchParams를 사용해 폼 데이터(application/x-www-form-urlencoded) 형식으로 전송하고, credentials: 'include' 옵션으로 세션 쿠키를 주고받도록 설정했습니다.


2. Spring Boot 백엔드 (API 서버)
"데이터 제공"과 "인증"을 담당합니다. 

AuthApiController.java / IndexApiController.java (신규)
- WHAT: @RestController가 붙은 새 API 컨트롤러입니다.
- WHY: React가 호출할 수 있는 JSON 전용 창구입니다.
  - IndexApiController의 /api/me는 IndexPage.tsx가 "로그인했어?"라고 물어볼 때 사용자 정보를 JSON(UserDTO)으로 답해줍니다.
  - AuthApiController의 /api/auth/signup은 React의 회원가입 폼에서 보낸 JSON을 받아 처리합니다.
- (이제 IndexController와 AuthController의 return "html이름" 같은 메서드들은 React가 대체하므로 필요 없어졌습니다.)

WebConfig.java (수정)
- WHAT: addCorsMappings 메서드를 추가했습니다.
- WHY: (Proxy와 짝꿍) React 서버(localhost:5173)가 우리 백엔드 서버(localhost:8080)에 접근하는 것을 공식적으로 **허용(CORS)**해주는 설정입니다. 특히 allowCredentials(true)는 React가 세션 쿠키(JSESSIONID)를 주고받을 수 있게 허용해 줍니다.

SecurityConfig.java (수정)
- WHAT: filterChain 설정을 React(SPA) 연동에 맞게 수정했습니다.
- WHY:
  - CSRF 해제: http.csrf().ignoringRequestMatchers(...)에 /api/**, /auth/login, /logout을 추가했습니다. React의 fetch 요청이 CSRF 토큰 없이도 API를 호출할 수 있게 허용했습니다.
  - 로그인/로그아웃 핸들러: .formLogin()과 .logout()이 성공/실패 시 HTML 페이지로 **리디렉션(Redirect)**하던 것을 멈췄습니다. 대신 React가 알아들을 수 있도록 200 OK (성공) 또는 401 Unauthorized (실패) 같은 HTTP 상태 코드만 반환하도록 변경했습니다.
  - OAuth2 리디렉션: oauth2Login().defaultSuccessUrl("http://localhost:5173", ...)을 추가했습니다. 구글/네이버 로그인 성공 시 백엔드(/)가 아닌 React 앱(localhost:5173)으로 사용자를 돌려보내도록 수정했습니다.



앞으로 할 일 (패턴 적용)
myPage(user/myPage/myPage.html)를 React로 바꾸려면 이 패턴을 그대로 쓰시면 됩니다.

1. React: src/page/MyPage.tsx 컴포넌트를 새로 만듭니다.
2. React: App.tsx에 <Route path="/users/me" element={<MyPage />} /> 라우팅을 추가합니다.
3. Backend: UserController에 @GetMapping("/api/users/me")처럼 UserDTO를 JSON으로 반환하는 API를 만듭니다. (기존 /api/me를 쓰셔도 됩니다.)
4. React: MyPage.tsx에서 useEffect를 사용해 /api/users/me API를 fetch로 호출하고, 응답받은 데이터로 화면을 그립니다.
5. (닉네임 수정 등): updateNickname.html 대신 MyPage.tsx 안에 폼을 만들고, UserController에 @PostMapping("/api/users/me/nickname") 같은 API를 만들어 fetch로 호출하게 만듭니다.
6. React의 전역 상태 관리(Context API, Recoil 등)나 컴포넌트 최적화


ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


1. 백엔드(Spring)와 프론트엔드(React)의 역할 분리
- 백엔드 (API 서버): 기존 @Controller가 HTML 뷰를 반환하던 방식에서, @RestController (예: IndexApiController, AuthApiController)가 JSON 데이터(예: UserDTO)를 반환하도록 변경하셨습니다. 이는 API 서버의 정석적인 역할입니다.
- 프론트엔드 (UI): React가 UI 렌더링과 페이지 이동(라우팅)을 담당하도록 react-router-dom을 사용하고, TypeScript로 타입 안정성을 확보하며 fetch를 통해 API를 호출하는 구조를 잡으셨습니다.

2. (가장 중요) API 연동을 위한 Spring Security 설정 SPA(React)와 연동할 때 Spring Security 설정이 가장 까다로운데, 이 부분을 교과서적으로 잘 처리하셨습니다.
- 로그인/로그아웃 핸들러: 기존의 '페이지 리디렉션' 방식 대신, React가 응답을 받고 스스로 처리할 수 있도록 HTTP 상태 코드(200 OK, 401 Unauthorized)와 JSON을 반환하도록 successHandler와 failureHandler를 커스텀하신 것이 핵심입니다.
- OAuth2 리디렉션: 소셜 로그인 성공 시 백엔드 /가 아닌 React 앱 주소(http://localhost:5173)로 사용자를 돌려보내도록 defaultSuccessUrl을 수정한 것도 완벽합니다.
- CSRF 설정: React의 fetch 요청은 세션 쿠키만 주고받을 뿐, CSRF 토큰을 자동으로 처리하지 않습니다. 따라서 API 경로( /api/**, /auth/login 등)에 대해 ignoringRequestMatchers를 설정하여 API 호출이 가능하게 하신 것이 정확합니다.

3. 개발 환경 CORS 문제 해결 이 부분에서 많은 분이 어려움을 겪는데, 2중으로 완벽하게 방어하셨습니다.
- Vite 프록시 설정: React 개발 서버(localhost:5173)에서 백엔드(localhost:8080)로 보내는 요청을 우회하기 위해 vite.config.ts에 proxy를 설정하셨습니다.
- Spring CORS 설정: 백엔드 WebConfig에서 addCorsMappings를 통해 React 개발 서버 주소(localhost:5173)를 공식적으로 허용하고, allowCredentials(true)로 쿠키(세션)를 주고받을 수 있게 하셨습니다.

MyPage나 다른 Thymeleaf 페이지들도 지금 만드신 API 호출 패턴을 그대로 적용해서 React TypeScript 컴포넌트로 하나씩 전환해 나가시면 됩니다. 현재 진행 중인 작업은 실무 표준에 매우 가까우며, 훌륭한 리팩토링의 예시입니다.

