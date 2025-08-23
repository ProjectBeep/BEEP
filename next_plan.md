# BEEP 그룹 기반 공유 기능 기획서 (수정본)

## 논의 결과 및 수정사항

### 데이터 구조 변경
- **DB 버전 업그레이드 불필요**: 앱 미출시로 기존 테이블 수정 없이 진행
- **그룹-기프티콘 관계 변경**: 기프티콘에 group_id가 아닌, 그룹이 gifticonIds 배열을 가지는 구조
- **다대다 관계**: 한 기프티콘을 여러 그룹에 공유 가능
- **서버 중심 설계**: group_id나 gifticon_id로 데이터 조회하는 구조

### 통합 테이블 설계 결정
WiFi, 멤버십, 기프티콘을 하나의 테이블로 통합:
- **공통점**: 모두 QR/바코드 기반 공유 아이템
- **Type 필드**: "GIFTICON", "WIFI", "MEMBERSHIP"으로 구분
- **metadata JSON**: 타입별 특수 데이터 저장
- **일관된 UX**: 통일된 추가/조회/공유 플로우

### WiFi 자동 연결 기능
- **QR 표준 형식**: `WIFI:T:WPA;S:MyNetwork;P:MyPassword;H:false;;`
- **Android 10+ 지원**: WifiNetworkSuggestion API 활용
- **클릭 한 번 연결**: QR 스캔 후 저장된 WiFi 정보로 자동 연결
- **보안**: 비밀번호 Android Keystore 암호화 저장

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

## 수정된 Firebase 데이터 구조 설계

### Collection: `users`
```firestore
users/{userId} {
  email: string,
  displayName: string,
  profileImage?: string,
  currentGroupId?: string,      // 현재 활성 그룹
  joinedGroups: [groupId],      // 가입한 그룹 목록
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
  memberIds: [userId],          // 멤버 ID 배열
  sharedItemIds: [itemId],      // 공유된 아이템 ID 배열 (통합)
  settings: {
    allowInvite: boolean,
    maxMembers: number
  },
  createdAt: timestamp,
  updatedAt: timestamp
}
```

### Collection: `shared_items` (통합 컬렉션)
```firestore
shared_items/{itemId} {
  // 공통 필드
  type: 'GIFTICON' | 'WIFI' | 'MEMBERSHIP',
  name: string,                 // 기프티콘명/SSID/멤버십명
  brand: string,                // 브랜드명
  displayBrand: string,
  code: string,                 // 바코드/QR코드
  codeType: string,             // QR_CODE, BARCODE_128, etc
  
  // 이미지 정보
  originImageUrl?: string,      // Firebase Storage URL
  croppedImageUrl?: string,     // Firebase Storage URL
  croppedRect?: {
    left: number, top: number,
    right: number, bottom: number
  },
  
  // 공유 정보
  addedBy: userId,
  addedByName: string,
  sharedGroups: [groupId],      // 공유된 그룹 목록
  
  // 타입별 메타데이터 (JSON)
  metadata: {
    // GIFTICON
    "expireAt"?: timestamp,
    "isCashCard"?: boolean,
    "totalCash"?: number,
    "remainCash"?: number,
    "isUsed"?: boolean,
    "memo"?: string,
    
    // WIFI
    "password"?: string,        // 암호화된 비밀번호
    "security"?: "WPA" | "WEP" | "OPEN",
    "hidden"?: boolean,
    "locationName"?: string,    // "우리집", "카페"
    "locationAddress"?: string,
    
    // MEMBERSHIP
    "membershipNumber"?: string,
    "membershipType"?: string,  // "VIP", "일반"
    "holderName"?: string,
    "expiryDate"?: timestamp
  },
  
  // 동기화 정보
  syncStatus: 'synced' | 'pending' | 'conflict',
  localId?: string,
  
  createdAt: timestamp,
  updatedAt: timestamp,
  updatedBy: userId,
  updatedByName: string
}
```

### Collection: `usage_history` (사용 기록)
```firestore
usage_history/{historyId} {
  itemId: string,               // shared_items ID
  itemName: string,
  itemType: string,             // GIFTICON, WIFI, MEMBERSHIP
  usedBy: userId,
  usedByName: string,
  addedBy: userId,
  addedByName: string,
  amount?: number,              // 사용 금액 (기프티콘만)
  location?: {
    x: number, y: number,
    address?: string
  },
  groupId: string,
  localId?: string,
  usedAt: timestamp
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

## 수정된 로컬 DB 구조

### 기존 테이블 유지
- **gifticon_table**: 기존 구조 그대로 유지 (개인 기프티콘용)
- **usage_history_table**: 삭제 예정 (통합 사용 기록으로 대체)

### 새로운 테이블 추가

#### shared_items_table (통합 아이템 테이블)
```kotlin
@Entity(tableName = "shared_items_table")
data class DBSharedItemEntity(
  @PrimaryKey(autoGenerate = true) val id: Long?,
  @ColumnInfo(name = "firebase_id") val firebaseId: String?,
  @ColumnInfo(name = "type") val type: String, // GIFTICON, WIFI, MEMBERSHIP
  @ColumnInfo(name = "name") val name: String,
  @ColumnInfo(name = "brand") val brand: String,
  @ColumnInfo(name = "display_brand") val displayBrand: String,
  @ColumnInfo(name = "code") val code: String, // 바코드/QR코드
  @ColumnInfo(name = "code_type") val codeType: String,
  @ColumnInfo(name = "origin_image_uri") val originImageUri: Uri?,
  @ColumnInfo(name = "cropped_image_uri") val croppedImageUri: Uri?,
  @ColumnInfo(name = "cropped_rect") val croppedRect: Rect?,
  @ColumnInfo(name = "metadata") val metadata: String, // JSON
  @ColumnInfo(name = "added_by") val addedBy: String,
  @ColumnInfo(name = "added_by_name") val addedByName: String,
  @ColumnInfo(name = "sync_status") val syncStatus: String,
  @ColumnInfo(name = "created_at") val createdAt: Date,
  @ColumnInfo(name = "updated_at") val updatedAt: Date,
  @ColumnInfo(name = "updated_by") val updatedBy: String,
  @ColumnInfo(name = "updated_by_name") val updatedByName: String
)
```

#### groups_table
```kotlin
@Entity(tableName = "groups_table")
data class DBGroupEntity(
  @PrimaryKey val id: String, // Firebase group ID
  @ColumnInfo(name = "name") val name: String,
  @ColumnInfo(name = "description") val description: String?,
  @ColumnInfo(name = "invite_code") val inviteCode: String,
  @ColumnInfo(name = "created_by") val createdBy: String,
  @ColumnInfo(name = "is_owner") val isOwner: Boolean,
  @ColumnInfo(name = "joined_at") val joinedAt: Date,
  @ColumnInfo(name = "last_sync_at") val lastSyncAt: Date?
)
```

#### group_shared_items_cross_ref (다대다 관계)
```kotlin
@Entity(
  tableName = "group_shared_items_cross_ref",
  primaryKeys = ["group_id", "shared_item_id"]
)
data class GroupSharedItemsCrossRef(
  @ColumnInfo(name = "group_id") val groupId: String,
  @ColumnInfo(name = "shared_item_id") val sharedItemId: Long,
  @ColumnInfo(name = "shared_at") val sharedAt: Date
)
```

#### unified_usage_history_table (통합 사용 기록)
```kotlin
@Entity(tableName = "unified_usage_history_table")
data class DBUnifiedUsageHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long?,
  @ColumnInfo(name = "firebase_id") val firebaseId: String?,
  @ColumnInfo(name = "item_id") val itemId: Long, // shared_items_table.id
  @ColumnInfo(name = "item_name") val itemName: String,
  @ColumnInfo(name = "item_type") val itemType: String,
  @ColumnInfo(name = "used_by") val usedBy: String,
  @ColumnInfo(name = "used_by_name") val usedByName: String,
  @ColumnInfo(name = "added_by") val addedBy: String,
  @ColumnInfo(name = "added_by_name") val addedByName: String,
  @ColumnInfo(name = "amount") val amount: Int?, // 사용 금액 (기프티콘만)
  @ColumnInfo(name = "location_x") val locationX: Dms?,
  @ColumnInfo(name = "location_y") val locationY: Dms?,
  @ColumnInfo(name = "location_address") val locationAddress: String?,
  @ColumnInfo(name = "group_id") val groupId: String,
  @ColumnInfo(name = "used_at") val usedAt: Date,
  @ColumnInfo(name = "sync_status") val syncStatus: String
)
```

#### sync_queue_table
```kotlin
@Entity(tableName = "sync_queue_table")
data class DBSyncQueueEntity(
  @PrimaryKey(autoGenerate = true) val id: Long?,
  @ColumnInfo(name = "action") val action: String, // CREATE, UPDATE, DELETE
  @ColumnInfo(name = "collection_name") val collectionName: String,
  @ColumnInfo(name = "document_id") val documentId: String,
  @ColumnInfo(name = "group_id") val groupId: String?,
  @ColumnInfo(name = "data_json") val dataJson: String,
  @ColumnInfo(name = "local_id") val localId: Long?,
  @ColumnInfo(name = "timestamp") val timestamp: Date,
  @ColumnInfo(name = "retry_count") val retryCount: Int,
  @ColumnInfo(name = "status") val status: String // PENDING, PROCESSING, COMPLETED, FAILED
)
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

## WiFi 자동 연결 구현 상세

### WiFi QR 코드 표준 형식
```
WIFI:T:WPA2;S:MyNetwork;P:MyPassword;H:false;;
```
- **T**: 보안 타입 (WPA/WPA2/WEP/nopass)
- **S**: SSID (네트워크 이름)
- **P**: 비밀번호
- **H**: Hidden 네트워크 여부 (true/false)

### Android 구현 방법

#### 1. Android 10+ (API 29) - WifiNetworkSuggestion
```kotlin
class WifiConnectionManager @Inject constructor(
    private val context: Context
) {
    fun connectToWifi(wifiData: WifiData): Boolean {
        val suggestion = WifiNetworkSuggestion.Builder()
            .setSsid(wifiData.ssid)
            .setWpa2Passphrase(wifiData.password)
            .build()

        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val status = wifiManager.addNetworkSuggestions(listOf(suggestion))
        
        return status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS
    }
}
```

#### 2. Android 11+ (API 30) - 시스템 다이얼로그 활용
```kotlin
fun showWifiConnectionDialog(wifiData: WifiData) {
    val intent = Intent(Settings.ACTION_WIFI_ADD_NETWORKS)
    val config = WifiNetworkSuggestion.Builder()
        .setSsid(wifiData.ssid)
        .setWpa2Passphrase(wifiData.password)
        .build()
        
    intent.putParcelableArrayListExtra(
        Settings.EXTRA_WIFI_NETWORK_LIST,
        arrayListOf(config)
    )
    startActivityForResult(intent, WIFI_CONNECTION_REQUEST)
}
```

### 필요한 권한
```xml
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

### WiFi 보안 처리
```kotlin
// Android Keystore를 활용한 비밀번호 암호화
class WifiSecurityManager {
    fun encryptPassword(password: String, alias: String): String {
        // AES 암호화 구현
    }
    
    fun decryptPassword(encryptedPassword: String, alias: String): String {
        // AES 복호화 구현
    }
}
```

## 수정된 구현 단계

### Phase 1: 기본 그룹 기능 + 통합 테이블
1. **Firebase 설정**
   - Firebase Auth, Firestore, Storage 초기화
   - 보안 규칙 설정
   
2. **로컬 DB 확장**
   - shared_items_table, groups_table 추가
   - 통합 Entity 및 DAO 구현
   
3. **그룹 관리 기능**
   - 그룹 생성/가입/초대 UI
   - QR코드 초대 시스템

### Phase 2: 통합 아이템 공유 (기프티콘/WiFi/멤버십)
1. **통합 추가 플로우**
   - QR/바코드 스캔 → 타입 자동 인식
   - 타입별 메타데이터 파싱 및 저장
   
2. **WiFi 자동 연결**
   - WifiNetworkSuggestion API 구현
   - Android Keystore 암호화
   
3. **멤버십 관리**
   - 멤버십 번호/바코드 저장
   - 만료일 알림

### Phase 3: 오프라인 동기화
1. **동기화 큐 시스템**
   - 로컬 변경사항 추적
   - WorkManager 백그라운드 동기화
   
2. **충돌 해결**
   - Last-Write-Wins 전략
   - 사용자 선택 가능한 충돌 해결

### Phase 4: 실시간 기능
1. **Firestore 리스너**
   - 그룹 데이터 실시간 동기화
   - UI 자동 업데이트
   
2. **푸시 알림**
   - 새 아이템 공유 알림
   - 그룹 초대 알림

## 보안 고려사항

1. **Firestore Security Rules**: 그룹 멤버만 해당 그룹 데이터 접근 가능
2. **WiFi 비밀번호 암호화**: Android Keystore로 로컬/Firebase 모두 암호화 저장
3. **멤버십 정보 보안**: 민감한 개인정보 암호화
4. **초대 코드 관리**: 일회성 또는 만료 시간 설정
5. **데이터 검증**: 클라이언트/서버 양쪽에서 데이터 무결성 검증