# **News Insight API 명세서 v1.0**

## **API Endpoint 요약**

### **인증 (Auth)**

* POST /api/auth/signup (회원가입)
* POST /api/auth/login (로그인)
* POST /api/auth/logout (로그아웃)
* POST /api/auth/refresh (JWT 토큰 갱신)

### **사용자 (Users)**

* GET /api/users/me (내 정보 조회)
* PUT /api/users/me (내 정보 수정)
* DELETE /api/users/me (회원 탈퇴)

### **AI 기능 (요약/인사이트)**

* POST /api/summary (텍스트 요약 요청)
* POST /api/insights (인사이트 생성 요청)
* GET /api/insights (인사이트 목록 조회)

### **구독자 전용 (Subscription-only)**

* GET /api/briefings (데일리 브리핑 조회)
* GET /api/subscriptions/me (내 구독 정보 조회)
* POST /api/subscriptions (구독 신청)
* DELETE /api/subscriptions/me (구독 해지)

### **관리자 (Admin)**

* GET /api/admin/users (사용자 목록 조회)
* PUT /api/admin/users/{userId} (사용자 정보 수정)
* DELETE /api/admin/users/{userId} (사용자 삭제)

## **1. 공통 사항**

### **1.1. Base URL**

모든 API의 기본 URL은 다음과 같습니다.  
/api

### **1.2. 인증 (Authentication)**

회원가입, 로그인을 제외한 모든 API 호출 시 HTTP Header에 Access Token을 포함해야 합니다.  
Authorization: Bearer <ACCESS\_TOKEN>

### **1.3. 공통 에러 응답 (Common Error Response)**

API 호출 실패 시 아래와 같은 형식으로 응답합니다.

{  
"timestamp": "2025-09-23T12:00:00Z",  
"status": 404,  
"error": "Not Found",  
"message": "요청한 리소스를 찾을 수 없습니다.",  
"path": "/api/users/999"  
}

## **2. 인증 (Auth)**

### **POST /api/auth/signup (회원가입)**

* **설명**: 새로운 사용자를 등록합니다.
* **Request Body**:  
  {  
  "userid": "newUser",  
  "password": "password123!",  
  "email": "newUser@example.com",  
  "nickname": "새로운유저"  
  }
* **Success Response** (201 Created):  
  {  
  "message": "회원가입이 성공적으로 완료되었습니다.",  
  "userId": 123  
  }

### **POST /api/auth/login (로그인)**

* **설명**: 사용자 로그인 후 Access/Refresh 토큰을 발급합니다.
* **Request Body**:  
  {  
  "userid": "testuser",  
  "password": "password123"  
  }
* **Success Response** (200 OK):  
  {  
  "grantType": "Bearer",  
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",  
  "refreshToken": "df9a8b7c-6e5d-4f3c-9a8b-7c6e5d4f3c9a..."  
  }

### **POST /api/auth/logout (로그아웃)**

* **설명**: 서버에서 Refresh Token을 무효화하여 로그아웃 처리합니다.
* **Request Body**:  
  {  
  "refreshToken": "df9a8b7c-6e5d-4f3c-9a8b-7c6e5d4f3c9a..."  
  }
* **Success Response** (200 OK):  
  {  
  "message": "로그아웃 되었습니다."  
  }

### **POST /api/auth/refresh (JWT 토큰 갱신)**

* **설명**: Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.
* **Request Body**:  
  {  
  "refreshToken": "df9a8b7c-6e5d-4f3c-9a8b-7c6e5d4f3c9a..."  
  }
* **Success Response** (200 OK):  
  {  
  "grantType": "Bearer",  
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9... (새로운 토큰)"  
  }

## **3. 사용자 (Users)**

### **GET /api/users/me (내 정보 조회)**

* **설명**: 현재 로그인된 사용자의 정보를 조회합니다.
* **Success Response** (200 OK):  
  {  
  "id": 1,  
  "userid": "testuser",  
  "email": "testuser@example.com",  
  "nickname": "테스트유저",  
  "provider": "local",  
  "role": "USER",  
  "createdAt": "2025-09-23T10:20:18Z"  
  }

### **PUT /api/users/me (내 정보 수정)**

* **설명**: 현재 로그인된 사용자의 정보를 수정합니다.
* **Request Body**:  
  {  
  "nickname": "새로운닉네임",  
  "currentPassword": "password123",  
  "newPassword": "newPassword456!"  
  }
* **Success Response** (200 OK):  
  {  
  "id": 1,  
  "userid": "testuser",  
  "email": "testuser@example.com",  
  "nickname": "새로운닉네임",  
  "provider": "local",  
  "role": "USER",  
  "createdAt": "2025-09-23T10:20:18Z"  
  }

### **DELETE /api/users/me (회원 탈퇴)**

* **설명**: 현재 로그인된 사용자를 탈퇴 처리합니다.
* **Success Response**: 204 No Content (Body 없음)

## **4. AI 기능 (요약/인사이트)**

### **POST /api/summary (텍스트 요약 요청)**

* **설명**: 입력된 텍스트를 AI를 통해 요약합니다.
* **Request Body**:  
  {  
  "text": "여기에 긴 뉴스 기사 원문이 들어갑니다..."  
  }
* **Success Response** (200 OK):  
  {  
  "summary": "AI가 생성한 핵심 요약문입니다."  
  }

### **POST /api/insights (인사이트 생성 요청)**

* **설명**: 특정 도메인과 태그를 기반으로 AI 인사이트를 생성합니다.
* **Request Body**:  
  {  
  "domain": "it",  
  "tags": \["반도체", "수출"]  
  }
* **Success Response** (201 Created):  
  {  
  "id": 55,  
  "title": "반도체 수출 동향 기반 IT 시장 전망",  
  "content": "최근 7일간의 반도체 수출 관련 뉴스를 종합 분석한 결과...",  
  "domain": "it",  
  "confidence": 0.925,  
  "references": \[  
  { "newsId": 101, "title": "정부, 반도체 산업 지원 확대 발표" },  
  { "newsId": 105, "title": "글로벌 IT 수요 증가, 반도체 수출 청신호" }  
  ],  
  "createdAt": "2025-09-23T12:30:00Z"  
  }

### **GET /api/insights (인사이트 목록 조회)**

* **설명**: 생성된 인사이트 목록을 조회합니다. (페이징 지원)
* **Query Parameters**: ?domain=it\&page=0\&size=10
* **Success Response** (200 OK):  
  {  
  "page": 0,  
  "size": 10,  
  "totalPages": 5,  
  "totalElements": 48,  
  "insights": \[  
  {  
  "id": 55,  
  "title": "반도체 수출 동향 기반 IT 시장 전망",  
  "domain": "it",  
  "createdAt": "2025-09-23T12:30:00Z"  
  }  
  ]  
  }

## **5. 구독자 전용 (Subscription)**

### **GET /api/briefings (데일리 브리핑 조회)**

* **설명**: 특정 날짜와 섹션의 데일리 브리핑을 조회합니다.
* **Query Parameters**: ?date=2025-09-23\&section=it
* **Success Response** (200 OK):  
  {  
  "id": 1,  
  "briefDate": "2025-09-23",  
  "section": "it",  
  "items": \[  
  {  
  "newsId": 234,  
  "title": "새로운 AI 언어 모델 공개",  
  "summary": "A사에서 새로운 거대 언어 모델을 공개하며 업계에 파장을 일으켰습니다..."  
  }  
  ]  
  }

### **GET /api/subscriptions/me (내 구독 정보 조회)**

* **설명**: 현재 사용자의 구독 정보를 조회합니다.
* **Success Response** (200 OK):  
  {  
  "id": 12,  
  "userId": 1,  
  "subscriptionType": "premium",  
  "startDate": "2025-09-01T00:00:00Z",  
  "endDate": "2026-09-01T00:00:00Z",  
  "isActive": true  
  }

### **POST /api/subscriptions (구독 신청)**

* **설명**: 새로운 구독을 신청하고 결제를 진행합니다.
* **Request Body**:  
  {  
  "subscriptionType": "premium",  
  "paymentToken": "iamport\_payment\_token\_abcdefg"  
  }
* **Success Response** (201 Created):  
  {  
  "id": 15,  
  "userId": 1,  
  "subscriptionType": "premium",  
  "startDate": "2025-09-23T10:20:18Z",  
  "endDate": "2026-09-23T10:20:18Z",  
  "isActive": true  
  }

### **DELETE /api/subscriptions/me (구독 해지)**

* **설명**: 현재 사용자의 구독을 해지(다음 결제 취소)합니다.
* **Success Response**: 204 No Content (Body 없음)

## **6. 관리자 (Admin)**

### **GET /api/admin/users (사용자 목록 조회)**

* **설명**: 모든 사용자 목록을 조회합니다. (페이징 지원)
* **Query Parameters**: ?page=0\&size=10
* **Success Response** (200 OK):  
  {  
  "page": 0,  
  "size": 10,  
  "totalPages": 25,  
  "totalElements": 245,  
  "users": \[  
  {  
  "id": 1,  
  "userid": "testuser",  
  "email": "testuser@example.com",  
  "nickname": "테스트유저",  
  "role": "USER",  
  "provider": "local",  
  "createdAt": "2025-09-23T10:20:18Z"  
  }  
  ]  
  }

### **PUT /api/admin/users/{userId} (사용자 정보 수정)**

* **설명**: 특정 사용자의 정보를 관리자가 수정합니다.
* **Request Body**:  
  {  
  "nickname": "변경닉네임",  
  "email": "force.change@example.com",  
  "role": "ADMIN"  
  }
* **Success Response** (200 OK):  
  {  
  "id": 123,  
  "userid": "someuser",  
  "email": "force.change@example.com",  
  "nickname": "변경닉네임",  
  "role": "ADMIN",  
  "provider": "local",  
  "createdAt": "2025-09-21T15:00:00Z"  
  }

### **DELETE /api/admin/users/{userId} (사용자 삭제)**

* **설명**: 특정 사용자를 관리자가 삭제합니다.
* **Success Response**: 204 No Content (Body 없음)
