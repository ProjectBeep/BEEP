package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lighthouse.beep.data.local.database.entity.DBBrandLocationEntity
import com.lighthouse.beep.data.local.database.entity.DBBrandSectionEntity
import com.lighthouse.beep.model.location.Dms

@Dao
internal interface BrandLocationDao {

    @Query("SELECT id FROM brand_section_table WHERE brand = :brand AND x = :x AND y = :y")
    suspend fun getSectionId(brand: String, x: Dms, y: Dms): Long?

    @Query("SELECT * FROM brand_location_table WHERE section_id = :sectionId")
    suspend fun getBrands(sectionId: Long?): List<DBBrandLocationEntity>

    @Insert
    suspend fun insertSection(section: DBBrandSectionEntity): Long

    @Insert
    suspend fun insertBrand(brand: DBBrandLocationEntity)

    @Query("DELETE FROM brand_location_table WHERE created_at < :timestamp")
    suspend fun removeExpirationBrands(timestamp: Long)

    @Query("DELETE FROM brand_section_table WHERE id NOT IN (SELECT DISTINCT section_id FROM brand_location_table WHERE section_id IS NOT NULL)")
    suspend fun cleanupOrphanedSections()
}