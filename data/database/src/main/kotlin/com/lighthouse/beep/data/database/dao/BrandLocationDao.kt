package com.lighthouse.beep.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lighthouse.beep.data.database.entity.DBBrandLocationEntity
import com.lighthouse.beep.data.database.entity.DBBrandSectionEntity
import com.lighthouse.beep.model.location.Dms

@Dao
internal interface BrandLocationDao {

    @Query("SELECT * FROM brand_location_table WHERE parent_section_id = :sectionId")
    suspend fun getBrands(sectionId: Long?): List<DBBrandLocationEntity>

    @Query("SELECT section_id FROM brand_section_table WHERE brand = LOWER(:brand) AND x = :x AND y = :y LIMIT 1")
    suspend fun getSectionId(
        brand: String,
        x: Dms,
        y: Dms,
    ): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: DBBrandLocationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSection(section: DBBrandSectionEntity): Long

    /**
     * 기준 시간 이하의 Section 정보 삭제
     * */
    @Query("DELETE FROM brand_section_table WHERE search_date <= :time")
    suspend fun removeExpirationBrands(time: Long)
}
