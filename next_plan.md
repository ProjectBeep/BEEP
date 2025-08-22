# BEEP 그룹 기반 공유 기능 기획서

## 확정된 요구사항

### 핵심 기능
1. **그룹 기반 공유**: 가족/지인 그룹 생성 및 관리
2. **초대 기능**: QR코드나 링크로 그룹 초대
3. **전체 권한**: 모든 멤버가 기프티콘 추가/사용/삭제 가능
4. **WiFi QR 공유**: WiFi 정보를 QR코드로 저장/공유
5. **멤버십 공유**: 투썸 등 멤버십 번호 공유
6. **작성자 표시**: 누가 공유했는지 표시
7. **오프라인 지원**: 오프라인에서도 사용 가능, 온라인 시 동기화
8. **실시간 동기화**: 실시간으로 변경사항 반영

## 기존 DB 구조 분석

### 공유 대상 테이블
1. **gifticon_table** ✅ - 기프티콘 메인 정보
2. **usage_history_table** ✅ - 사용 기록

### 개별 캐시 테이블 (공유 불필요)
3. **brand_location_table** ❌ - 매장 위치 캐시
4. **brand_section_table** ❌ - 검색 캐시

## Firebase 데이터 구조 설계

### Collection: `users`
```firestore
users/{userId} {
  email: string,
  displayName: string,
  profileImage?: string,
  currentGroupId?: string,      // 현재 활성 그룹
  createdAt: timestamp,
  lastActiveAt: timestamp
}
```

### Collection: `groups` (공유 그룹)
```firestore
groups/{groupId} {
  name: string,
  description?: string,
  inviteCode: string,           // QR코드용 초대 코드
  createdBy: userId,
  members: [
    {
      userId: string,
      displayName: string,
      role: 'owner' | 'member',
      joinedAt: timestamp
    }
  ],
  settings: {
    allowInvite: boolean,
    maxMembers: number
  },
  createdAt: timestamp,
  updatedAt: timestamp
}
```

### Collection: `gifticons` (그룹별 서브컬렉션)
```firestore
groups/{groupId}/gifticons/{gifticonId} {
  // 기존 필드들
  name: string,
  brand: string,
  displayBrand: string,
  barcode: string,
  expireAt: timestamp,
  isCashCard: boolean,
  totalCash: number,
  remainCash: number,
  isUsed: boolean,
  memo: string,
  
  // 이미지 정보
  originImageUrl?: string,      // Firebase Storage URL
  croppedImageUrl?: string,     // Firebase Storage URL
  croppedRect?: {
    left: number,
    top: number,
    right: number,
    bottom: number
  },
  
  // 공유 관련 필드
  addedBy: userId,              // 추가한 사람
  addedByName: string,          // 추가한 사람 이름
  groupId: string,              // 소속 그룹
  
  // 동기화 관련
  syncStatus: 'synced' | 'pending' | 'conflict',
  localId?: string,             // 로컬 DB ID (오프라인용)
  
  createdAt: timestamp,
  updatedAt: timestamp,
  updatedBy: userId,
  updatedByName: string
}
```

### Collection: `usage_history` (그룹별 서브컬렉션)
```firestore
groups/{groupId}/usageHistory/{historyId} {
  gifticonId: string,
  gifticonName: string,         // 기프티콘 이름 (참조용)
  usedBy: userId,               // 사용한 사람
  usedByName: string,           // 사용한 사람 이름
  addedBy: userId,              // 기프티콘 추가한 사람
  addedByName: string,          // 기프티콘 추가한 사람 이름
  amount: number,               // 사용 금액
  location?: {
    x: number,
    y: number,
    address?: string
  },
  groupId: string,
  localId?: string,             // 로컬 DB ID (오프라인용)
  usedAt: timestamp
}
```

### Collection: `wifi_credentials` (그룹별 서브컬렉션)
```firestore
groups/{groupId}/wifiCredentials/{wifiId} {
  ssid: string,
  password: string,
  security: 'WPA' | 'WEP' | 'OPEN',
  hidden: boolean,
  location?: {
    name: string,               // 예: "우리집", "카페"
    address?: string
  },
  qrCode?: string,              // QR 코드 데이터
  addedBy: userId,
  addedByName: string,
  createdAt: timestamp,
  updatedAt: timestamp
}
```

### Collection: `memberships` (그룹별 서브컬렉션)
```firestore
groups/{groupId}/memberships/{membershipId} {
  brandName: string,            // 예: "투썸플레이스"
  membershipNumber: string,
  membershipType: string,       // 예: "VIP", "일반"
  holderName?: string,          // 멤버십 소유자명
  expiryDate?: timestamp,
  barcode?: string,             // 바코드가 있는 경우
  qrCode?: string,              // QR코드가 있는 경우
  addedBy: userId,
  addedByName: string,
  createdAt: timestamp,
  updatedAt: timestamp
}
```

### Collection: `sync_queue` (오프라인 동기화용)
```firestore
users/{userId}/syncQueue/{actionId} {
  action: 'create' | 'update' | 'delete',
  collection: 'gifticons' | 'usageHistory' | 'wifiCredentials' | 'memberships',
  documentId: string,
  groupId: string,
  data: object,                 // 변경할 데이터
  localId?: string,            // 로컬 DB ID
  timestamp: timestamp,
  retryCount: number,
  status: 'pending' | 'processing' | 'failed' | 'completed'
}
```

## 로컬 DB 구조 수정사항

### 기존 테이블 수정

#### gifticon_table 수정
```sql
ALTER TABLE gifticon_table ADD COLUMN group_id TEXT;
ALTER TABLE gifticon_table ADD COLUMN added_by TEXT;
ALTER TABLE gifticon_table ADD COLUMN added_by_name TEXT;
ALTER TABLE gifticon_table ADD COLUMN updated_by TEXT;
ALTER TABLE gifticon_table ADD COLUMN updated_by_name TEXT;
ALTER TABLE gifticon_table ADD COLUMN firebase_id TEXT;
ALTER TABLE gifticon_table ADD COLUMN sync_status TEXT DEFAULT 'pending';
```

#### usage_history_table 수정
```sql
ALTER TABLE usage_history_table ADD COLUMN used_by TEXT;
ALTER TABLE usage_history_table ADD COLUMN used_by_name TEXT;
ALTER TABLE usage_history_table ADD COLUMN added_by TEXT;
ALTER TABLE usage_history_table ADD COLUMN added_by_name TEXT;
ALTER TABLE usage_history_table ADD COLUMN firebase_id TEXT;
ALTER TABLE usage_history_table ADD COLUMN group_id TEXT;
```

### 새로운 테이블 추가

#### wifi_credentials_table
```sql
CREATE TABLE wifi_credentials_table (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  firebase_id TEXT,
  group_id TEXT NOT NULL,
  ssid TEXT NOT NULL,
  password TEXT NOT NULL,
  security TEXT NOT NULL,
  hidden INTEGER DEFAULT 0,
  location_name TEXT,
  location_address TEXT,
  qr_code TEXT,
  added_by TEXT NOT NULL,
  added_by_name TEXT NOT NULL,
  created_at INTEGER NOT NULL,
  updated_at INTEGER NOT NULL,
  sync_status TEXT DEFAULT 'pending'
);
```

#### memberships_table
```sql
CREATE TABLE memberships_table (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  firebase_id TEXT,
  group_id TEXT NOT NULL,
  brand_name TEXT NOT NULL,
  membership_number TEXT NOT NULL,
  membership_type TEXT,
  holder_name TEXT,
  expiry_date INTEGER,
  barcode TEXT,
  qr_code TEXT,
  added_by TEXT NOT NULL,
  added_by_name TEXT NOT NULL,
  created_at INTEGER NOT NULL,
  updated_at INTEGER NOT NULL,
  sync_status TEXT DEFAULT 'pending'
);
```

#### sync_queue_table
```sql
CREATE TABLE sync_queue_table (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  action TEXT NOT NULL,
  collection_name TEXT NOT NULL,
  document_id TEXT NOT NULL,
  group_id TEXT NOT NULL,
  data_json TEXT NOT NULL,
  local_id INTEGER,
  timestamp INTEGER NOT NULL,
  retry_count INTEGER DEFAULT 0,
  status TEXT DEFAULT 'pending'
);
```

## 오프라인 동기화 전략

### 1. WorkManager를 활용한 백그라운드 동기화
```kotlin
class SyncWorker : CoroutineWorker() {
  override suspend fun doWork(): Result {
    // 1. 로컬 변경사항을 Firebase로 업로드
    // 2. Firebase 변경사항을 로컬로 다운로드
    // 3. 충돌 해결
    return Result.success()
  }
}
```

### 2. Firebase Offline Persistence
```kotlin
FirebaseFirestore.getInstance().apply {
  firestoreSettings = FirebaseFirestoreSettings.Builder()
    .setPersistenceEnabled(true)
    .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
    .build()
}
```

### 3. 실시간 리스너
```kotlin
// 그룹 데이터 실시간 동기화
firestore.collection("groups")
  .document(groupId)
  .collection("gifticons")
  .addSnapshotListener { snapshot, error ->
    // 실시간 변경사항 처리
    syncWithLocalDatabase(snapshot)
  }
```

## 구현 단계

### Phase 1: 기본 그룹 기능
1. Firebase 프로젝트 설정
2. 그룹 생성/가입 기능
3. 기존 기프티콘 테이블을 그룹 기반으로 수정

### Phase 2: 공유 기능 확장
1. WiFi QR 공유 기능
2. 멤버십 공유 기능
3. 작성자 표시 기능

### Phase 3: 오프라인 동기화
1. 로컬 변경사항 큐잉
2. WorkManager 동기화 로직
3. 충돌 해결 메커니즘

### Phase 4: 실시간 기능
1. 실시간 변경사항 알림
2. 푸시 알림 연동
3. UI 실시간 업데이트

## 보안 고려사항

1. **Firestore Security Rules**: 그룹 멤버만 해당 그룹 데이터 접근 가능
2. **WiFi 비밀번호 암호화**: 로컬/Firebase 모두 암호화 저장
3. **멤버십 정보 보안**: 민감한 개인정보 암호화
4. **초대 코드 관리**: 일회성 또는 만료 시간 설정