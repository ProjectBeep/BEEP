# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

BEEP는 기프티콘 관리 Android 애플리케이션으로, 사용자가 다양한 기프티콘을 한 곳에서 관리하고, 위치 기반으로 추천받을 수 있는 서비스입니다.

## 핵심 아키텍처

### 모듈 구조
프로젝트는 멀티모듈 구조로 구성되어 있으며, 주요 모듈은 다음과 같습니다:

- **app**: 메인 애플리케이션 모듈
- **auth**: 인증 관련 (Google, Kakao, Naver 소셜 로그인)
- **core**: 공통 유틸리티 및 확장 함수
- **data**: 데이터 레이어 (database, datastore, remote, content)
- **domain**: 비즈니스 로직 (repository, usecase)
- **model**: 데이터 모델
- **theme**: 테마 및 스타일 리소스
- **ui**: UI 컴포넌트 (page, dialog, core)
- **library**: 독립적인 기능 모듈 (barcode, recognizer, network)
- **worker**: 백그라운드 작업 (알림, 위젯)

### 기술 스택
- **언어**: Kotlin
- **빌드 시스템**: Gradle (Version Catalog 사용)
- **DI**: Hilt
- **비동기 처리**: Coroutines, Flow
- **네비게이션**: Android Navigation Component
- **데이터베이스**: Room
- **네트워크**: Retrofit, OkHttp, Moshi
- **이미지 처리**: Coil
- **지도**: Naver Map SDK
- **보안**: AndroidX Security Crypto
- **머신러닝**: ML Kit (텍스트 인식)
- **인증**: Google 로그인 (Kakao, Naver 로그인은 비활성화됨)

## 빌드 및 실행 명령어

### 빌드
```bash
./gradlew assembleDebug  # 디버그 빌드
./gradlew assembleRelease  # 릴리스 빌드
./gradlew clean build  # 클린 빌드
```

### 테스트
```bash
./gradlew test  # 유닛 테스트 실행
./gradlew connectedAndroidTest  # 인스트루먼테이션 테스트
```

### 코드 품질
```bash
./gradlew lint  # Android Lint 실행
./gradlew ktlintCheck  # Kotlin 코드 스타일 검사 (설정 필요)
./gradlew ktlintFormat  # Kotlin 코드 스타일 자동 수정 (설정 필요)
```

## 개발 환경 설정

### 필수 설정 파일
`keystore.properties` 파일을 프로젝트 루트에 생성하고 다음 내용을 추가:
```properties
naver_map_api_id=YOUR_NAVER_MAP_API_ID
```

### JVM 버전
프로젝트는 JVM 17 이상이 필요합니다. JVM 24를 사용 중이라면 build-logic 모듈의 JVM target을 조정해야 할 수 있습니다.

## 데이터베이스 구조

프로젝트는 Room 데이터베이스를 사용하며, 다음과 같은 주요 엔티티를 포함합니다:

### 주요 테이블
1. **gifticon_table** (DBGifticonEntity)
   - 기프티콘 메인 정보 (id, userId, name, brand, barcode, expireAt 등)
   - 이미지 정보 (originUri, croppedUri, croppedRect)
   - 현금권 관련 정보 (isCashCard, totalCash, remainCash)
   - 상태 정보 (isUsed, memo, createdAt, updatedAt)

2. **usage_history_table** (DBUsageHistoryEntity)
   - 기프티콘 사용 기록 (gifticonId, date, amount)
   - 위치 정보 (x, y - Dms 타입)
   - 외래키: gifticon_table.id (CASCADE 삭제)

3. **brand_location_table** (DBBrandLocationEntity)
   - 브랜드 위치 정보 (placeUrl, placeName, addressName)
   - 카테고리 정보 (categoryName, brand, displayBrand)
   - 좌표 정보 (x, y - Dms 타입)

4. **brand_section_table** (DBBrandSectionEntity)
   - 브랜드 섹션 정보 (참조 테이블)

### 컨버터
- **DateConverter**: Date ↔ Long 변환
- **DmsConverter**: Dms(위도/경도) ↔ String 변환  
- **RectConverter**: Rect ↔ String 변환
- **UriConverter**: Uri ↔ String 변환

## 주요 기능 및 화면

1. **인트로/로그인**: Google 로그인 (Kakao, Naver는 현재 비활성화)
2. **홈**: 기프티콘 목록, 위치 기반 추천
3. **지도**: 사용 가능한 브랜드 위치 표시
4. **기프티콘 관리**: 추가, 수정, 삭제, 사용 처리
5. **보안**: 지문 인증으로 바코드 보호
6. **설정**: 알림, 위젯, 보안 설정

## 코딩 컨벤션

- Kotlin 공식 코딩 스타일 준수
- 컴포넌트 네이밍: Fragment는 `Fragment` 접미사, ViewModel은 `ViewModel` 접미사 사용
- 패키지 구조: feature 기반 패키징
- DI 모듈: 각 데이터 소스별로 `DataModule` 제공

## 한국어 진행 과정 설명

이 프로젝트에서 작업할 때는 진행 과정과 설명을 한국어로 제공합니다. 코드 주석은 영어로 작성하되, 사용자와의 커뮤니케이션은 한국어로 진행합니다.