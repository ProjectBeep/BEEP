package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lighthouse.beep.data.local.database.entity.DBGifticonDetailEntity
import com.lighthouse.beep.data.local.database.entity.DBItemEntity
import com.lighthouse.beep.data.local.database.entity.DBItemImageEntity
import com.lighthouse.beep.data.local.database.model.DBBrandCategory
import com.lighthouse.beep.data.local.database.model.DBGifticonDetail
import com.lighthouse.beep.data.local.database.model.DBGifticonEditInfo
import com.lighthouse.beep.data.local.database.model.DBGifticonImageData
import com.lighthouse.beep.data.local.database.model.DBGifticonListItem
import com.lighthouse.beep.data.local.database.model.DBGifticonResource
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
internal interface GifticonDao {

    @Query("SELECT COUNT(*) > 0 FROM items_table WHERE owner_id = :userId AND type = 'GIFTICON' AND id IN (SELECT item_id FROM gifticon_details_table WHERE is_used = :isUsed)")
    fun isExistGifticon(userId: String, isUsed: Boolean): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM items_table WHERE owner_id = :userId AND type = 'GIFTICON' AND id IN (SELECT item_id FROM gifticon_details_table WHERE is_used = :isUsed)")
    fun getGifticonCount(userId: String, isUsed: Boolean): Flow<Int>

    @Transaction
    @Query("""
        SELECT i.id, i.name, i.code, i.code_type, gd.brand, gd.display_brand, gd.expire_at, 
               gd.is_cash_card, gd.total_cash, gd.remain_cash, gd.is_used, gd.used_at, gd.memo,
               i.created_at, i.updated_at
        FROM items_table i
        INNER JOIN gifticon_details_table gd ON i.id = gd.item_id
        WHERE i.owner_id = :userId AND i.id = :gifticonId AND i.type = 'GIFTICON'
    """)
    fun getGifticonDetail(userId: String, gifticonId: Long): Flow<DBGifticonDetail?>

    @Transaction
    @Query("""
        SELECT i.id, i.name, i.code, i.code_type, gd.brand, gd.display_brand, gd.expire_at,
               gd.is_cash_card, gd.total_cash, gd.remain_cash, gd.is_used, gd.memo,
               ii.local_uri as origin_uri, ii.cropped_rect
        FROM items_table i
        INNER JOIN gifticon_details_table gd ON i.id = gd.item_id
        LEFT JOIN item_images_table ii ON i.id = ii.item_id AND ii.image_type = 'ORIGINAL'
        WHERE i.owner_id = :userId AND i.id = :gifticonId AND i.type = 'GIFTICON'
    """)
    suspend fun getGifticonEditInfo(userId: String, gifticonId: Long): DBGifticonEditInfo?

    @Transaction
    @Query("""
        SELECT i.id, i.name, gd.brand, gd.display_brand,
               orig.local_uri as origin_uri, orig.cropped_rect,
               crop.local_uri as cropped_uri, thumb.local_uri as thumbnail_uri
        FROM items_table i
        INNER JOIN gifticon_details_table gd ON i.id = gd.item_id
        LEFT JOIN item_images_table orig ON i.id = orig.item_id AND orig.image_type = 'ORIGINAL'
        LEFT JOIN item_images_table crop ON i.id = crop.item_id AND crop.image_type = 'CROPPED'
        LEFT JOIN item_images_table thumb ON i.id = thumb.item_id AND thumb.image_type = 'THUMBNAIL'
        WHERE i.owner_id = :userId AND i.type = 'GIFTICON' AND i.id IN (:gifticonIdList)
    """)
    suspend fun getGifticonResourceList(userId: String, gifticonIdList: List<Long>): List<DBGifticonResource>

    @Transaction
    @Query("""
        SELECT i.id, i.name, gd.brand, gd.display_brand,
               orig.local_uri as origin_uri, orig.cropped_rect,
               crop.local_uri as cropped_uri, thumb.local_uri as thumbnail_uri
        FROM items_table i
        INNER JOIN gifticon_details_table gd ON i.id = gd.item_id
        LEFT JOIN item_images_table orig ON i.id = orig.item_id AND orig.image_type = 'ORIGINAL'
        LEFT JOIN item_images_table crop ON i.id = crop.item_id AND crop.image_type = 'CROPPED'
        LEFT JOIN item_images_table thumb ON i.id = thumb.item_id AND thumb.image_type = 'THUMBNAIL'
        WHERE i.owner_id = :userId AND i.type = 'GIFTICON'
    """)
    suspend fun getGifticonResourceList(userId: String): List<DBGifticonResource>

    @Transaction
    @Query("""
        SELECT i.id, i.name, gd.brand, gd.display_brand, gd.expire_at,
               gd.is_cash_card, gd.total_cash, gd.remain_cash, gd.is_used,
               thumb.local_uri as thumbnail_uri
        FROM items_table i
        INNER JOIN gifticon_details_table gd ON i.id = gd.item_id
        LEFT JOIN item_images_table thumb ON i.id = thumb.item_id AND thumb.image_type = 'THUMBNAIL'
        WHERE i.owner_id = :userId AND i.type = 'GIFTICON' AND gd.is_used = :isUsed
        ORDER BY 
            CASE WHEN :sortBy = 'name' AND :isAsc = 1 THEN i.name END ASC,
            CASE WHEN :sortBy = 'name' AND :isAsc = 0 THEN i.name END DESC,
            CASE WHEN :sortBy = 'brand' AND :isAsc = 1 THEN gd.display_brand END ASC,
            CASE WHEN :sortBy = 'brand' AND :isAsc = 0 THEN gd.display_brand END DESC,
            CASE WHEN :sortBy = 'expire' AND :isAsc = 1 THEN gd.expire_at END ASC,
            CASE WHEN :sortBy = 'expire' AND :isAsc = 0 THEN gd.expire_at END DESC,
            CASE WHEN :sortBy = 'recent' AND :isAsc = 1 THEN i.created_at END ASC,
            CASE WHEN :sortBy = 'recent' AND :isAsc = 0 THEN i.created_at END DESC
    """)
    fun getGifticonList(userId: String, isUsed: Boolean, sortBy: String, isAsc: Int): Flow<List<DBGifticonListItem>>

    @Transaction
    @Query("""
        SELECT i.id, i.name, gd.brand, gd.display_brand, gd.expire_at,
               gd.is_cash_card, gd.total_cash, gd.remain_cash, gd.is_used,
               thumb.local_uri as thumbnail_uri
        FROM items_table i
        INNER JOIN gifticon_details_table gd ON i.id = gd.item_id
        LEFT JOIN item_images_table thumb ON i.id = thumb.item_id AND thumb.image_type = 'THUMBNAIL'
        WHERE i.owner_id = :userId AND i.type = 'GIFTICON' AND gd.brand = :brand
        ORDER BY 
            CASE WHEN :sortBy = 'name' AND :isAsc = 1 THEN i.name END ASC,
            CASE WHEN :sortBy = 'name' AND :isAsc = 0 THEN i.name END DESC,
            CASE WHEN :sortBy = 'brand' AND :isAsc = 1 THEN gd.display_brand END ASC,
            CASE WHEN :sortBy = 'brand' AND :isAsc = 0 THEN gd.display_brand END DESC,
            CASE WHEN :sortBy = 'expire' AND :isAsc = 1 THEN gd.expire_at END ASC,
            CASE WHEN :sortBy = 'expire' AND :isAsc = 0 THEN gd.expire_at END DESC,
            CASE WHEN :sortBy = 'recent' AND :isAsc = 1 THEN i.created_at END ASC,
            CASE WHEN :sortBy = 'recent' AND :isAsc = 0 THEN i.created_at END DESC
    """)
    fun getGifticonListByBrand(userId: String, brand: String, sortBy: String, isAsc: Int): Flow<List<DBGifticonListItem>>

    @Transaction
    @Query("""
        SELECT i.id, i.name, gd.brand, gd.display_brand,
               orig.local_uri as origin_uri, orig.cropped_rect,
               crop.local_uri as cropped_uri, thumb.local_uri as thumbnail_uri
        FROM items_table i
        INNER JOIN gifticon_details_table gd ON i.id = gd.item_id
        LEFT JOIN item_images_table orig ON i.id = orig.item_id AND orig.image_type = 'ORIGINAL'
        LEFT JOIN item_images_table crop ON i.id = crop.item_id AND crop.image_type = 'CROPPED'
        LEFT JOIN item_images_table thumb ON i.id = thumb.item_id AND thumb.image_type = 'THUMBNAIL'
        WHERE i.owner_id = :userId AND i.type = 'GIFTICON'
    """)
    suspend fun getGifticonImageDataList(userId: String): List<DBGifticonImageData>

    @Transaction
    suspend fun insertGifticonList(entityList: List<DBGifticonEditInfo>): List<Long> {
        val itemIds = mutableListOf<Long>()
        for (entity in entityList) {
            val itemEntity = DBItemEntity(
                id = null,
                firebaseId = null,
                type = "GIFTICON",
                ownerId = entity.userId,
                code = entity.barcode,
                codeType = entity.barcodeType,
                name = entity.name,
                createdAt = Date(),
                updatedAt = Date(),
                syncStatus = "PENDING"
            )
            
            val itemId = insertItem(itemEntity)
            itemIds.add(itemId)
            
            val detailEntity = DBGifticonDetailEntity(
                item_id = itemId,
                brand = entity.brand,
                displayBrand = entity.displayBrand,
                expireAt = entity.expireAt,
                isCashCard = entity.isCashCard,
                totalCash = entity.totalCash,
                remainCash = entity.remainCash,
                isUsed = false,
                usedAt = null,
                memo = entity.memo
            )
            insertGifticonDetail(detailEntity)
            
            // Insert images if they exist
            entity.originUri?.let { uri ->
                val imageEntity = DBItemImageEntity(
                    id = null,
                    itemId = itemId,
                    imageType = "ORIGINAL",
                    localUri = uri,
                    firebaseUrl = null,
                    croppedRect = entity.croppedRect,
                    createdAt = Date()
                )
                insertItemImage(imageEntity)
            }
            
            entity.croppedUri?.let { uri ->
                val imageEntity = DBItemImageEntity(
                    id = null,
                    itemId = itemId,
                    imageType = "CROPPED",
                    localUri = uri,
                    firebaseUrl = null,
                    croppedRect = null,
                    createdAt = Date()
                )
                insertItemImage(imageEntity)
            }
            
            entity.thumbnailUri?.let { uri ->
                val imageEntity = DBItemImageEntity(
                    id = null,
                    itemId = itemId,
                    imageType = "THUMBNAIL",
                    localUri = uri,
                    firebaseUrl = null,
                    croppedRect = null,
                    createdAt = Date()
                )
                insertItemImage(imageEntity)
            }
        }
        return itemIds
    }

    @Transaction
    suspend fun updateGifticon(entity: DBGifticonEditInfo) {
        val itemEntity = getItemById(entity.gifticonId) ?: return
        
        val updatedItem = itemEntity.copy(
            name = entity.name,
            code = entity.barcode,
            codeType = entity.barcodeType,
            updatedAt = Date(),
            syncStatus = "PENDING"
        )
        updateItem(updatedItem)
        
        val detailEntity = getGifticonDetailByItemId(entity.gifticonId) ?: return
        val updatedDetail = detailEntity.copy(
            brand = entity.brand,
            displayBrand = entity.displayBrand,
            expireAt = entity.expireAt,
            isCashCard = entity.isCashCard,
            totalCash = entity.totalCash,
            remainCash = entity.remainCash,
            memo = entity.memo
        )
        updateGifticonDetail(updatedDetail)
        
        // Update images
        entity.originUri?.let { uri ->
            val existingImage = getItemImageByType(entity.gifticonId, "ORIGINAL")
            if (existingImage != null) {
                updateItemImage(existingImage.copy(localUri = uri, croppedRect = entity.croppedRect))
            } else {
                insertItemImage(DBItemImageEntity(
                    id = null,
                    itemId = entity.gifticonId,
                    imageType = "ORIGINAL",
                    localUri = uri,
                    firebaseUrl = null,
                    croppedRect = entity.croppedRect,
                    createdAt = Date()
                ))
            }
        }
    }

    @Query("DELETE FROM items_table WHERE owner_id = :userId AND type = 'GIFTICON'")
    suspend fun deleteGifticon(userId: String)

    @Query("DELETE FROM items_table WHERE owner_id = :userId AND type = 'GIFTICON' AND id IN (:gifticonIdList)")
    suspend fun deleteGifticon(userId: String, gifticonIdList: List<Long>)

    @Query("UPDATE items_table SET owner_id = :newUserId, sync_status = 'PENDING', updated_at = :currentTime WHERE owner_id = :oldUserId AND type = 'GIFTICON'")
    suspend fun transferGifticon(oldUserId: String, newUserId: String, currentTime: Date = Date())

    @Query("""
        SELECT gd.brand, gd.display_brand, COUNT(*) as count
        FROM items_table i
        INNER JOIN gifticon_details_table gd ON i.id = gd.item_id
        WHERE i.owner_id = :userId AND i.type = 'GIFTICON'
        GROUP BY gd.brand, gd.display_brand
        ORDER BY gd.display_brand
    """)
    fun getBrandCategoryList(userId: String): Flow<List<DBBrandCategory>>

    @Query("""
        UPDATE gifticon_details_table 
        SET is_used = 1, used_at = :usedAt 
        WHERE item_id IN (
            SELECT id FROM items_table 
            WHERE owner_id = :userId AND type = 'GIFTICON' AND id IN (:gifticonIdList)
        )
    """)
    suspend fun useGifticonList(userId: String, gifticonIdList: List<Long>, usedAt: Date)

    @Query("""
        UPDATE gifticon_details_table 
        SET is_used = 0, used_at = null 
        WHERE item_id IN (
            SELECT id FROM items_table 
            WHERE owner_id = :userId AND type = 'GIFTICON' AND id IN (:gifticonIdList)
        )
    """)
    suspend fun revertGifticonList(userId: String, gifticonIdList: List<Long>)

    @Query("""
        UPDATE gifticon_details_table 
        SET is_used = :isUsed, remain_cash = :remainCash
        WHERE item_id IN (
            SELECT id FROM items_table 
            WHERE owner_id = :userId AND type = 'GIFTICON' AND id = :gifticonId
        )
    """)
    suspend fun updateGifticonUseInfo(userId: String, gifticonId: Long, isUsed: Boolean, remainCash: Int)

    // Helper methods
    @Insert
    suspend fun insertItem(item: DBItemEntity): Long

    @Update
    suspend fun updateItem(item: DBItemEntity)

    @Query("SELECT * FROM items_table WHERE id = :itemId")
    suspend fun getItemById(itemId: Long): DBItemEntity?

    @Insert
    suspend fun insertGifticonDetail(detail: DBGifticonDetailEntity)

    @Update
    suspend fun updateGifticonDetail(detail: DBGifticonDetailEntity)

    @Query("SELECT * FROM gifticon_details_table WHERE item_id = :itemId")
    suspend fun getGifticonDetailByItemId(itemId: Long): DBGifticonDetailEntity?

    @Insert
    suspend fun insertItemImage(image: DBItemImageEntity)

    @Update
    suspend fun updateItemImage(image: DBItemImageEntity)

    @Query("SELECT * FROM item_images_table WHERE item_id = :itemId AND image_type = :imageType")
    suspend fun getItemImageByType(itemId: Long, imageType: String): DBItemImageEntity?
}