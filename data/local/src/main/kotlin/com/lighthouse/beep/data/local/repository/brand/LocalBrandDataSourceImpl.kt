package com.lighthouse.beep.data.local.repository.brand

import com.lighthouse.beep.data.local.database.dao.BrandLocationDao
import com.lighthouse.beep.data.local.database.entity.DBBrandSectionEntity
import com.lighthouse.beep.data.local.database.mapper.brand.toEntity
import com.lighthouse.beep.data.local.database.mapper.brand.toModel
import com.lighthouse.beep.data.repository.brand.LocalBrandDataSource
import com.lighthouse.beep.model.brand.BrandLocation
import com.lighthouse.beep.model.location.Dms
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

internal class LocalBrandDataSourceImpl @Inject constructor(
    private val brandLocationDao: BrandLocationDao,
) : LocalBrandDataSource {

    override suspend fun getSectionId(
        brand: String,
        x: Dms,
        y: Dms,
    ): Long? {
        return brandLocationDao.getSectionId(brand, x, y)
    }

    override suspend fun getBrandPlaceInfo(sectionId: Long): List<BrandLocation> {
        return brandLocationDao.getBrands(sectionId).map {
            it.toModel()
        }
    }

    override suspend fun getBrandPlaceInfo(
        brand: String,
        x: Dms,
        y: Dms,
    ): List<BrandLocation> {
        return brandLocationDao.getBrands(getSectionId(brand, x, y)).map {
            it.toModel()
        }
    }

    override suspend fun insertSection(brand: String, x: Dms, y: Dms): Long {
        return brandLocationDao.insertSection(DBBrandSectionEntity(brand, x, y))
    }

    override suspend fun insertBrandPlaceInfo(
        brandLocationList: List<BrandLocation>,
        gap: Int,
    ) {
        if (brandLocationList.isEmpty()) {
            return
        }

        val brand = brandLocationList.first().displayBrand
        for (location in brandLocationList) {
            val x = location.x.sampling(sGap = gap)
            val y = location.y.sampling(sGap = gap)
            var sectionId = brandLocationDao.getSectionId(brand, x, y)
            if (sectionId == null) {
                sectionId = insertSection(brand, x, y)
            }
            brandLocationDao.insertBrand(location.toEntity(sectionId))
        }
    }

    override suspend fun removeExpirationBrands() {
        val date: Date = Calendar.getInstance().apply {
            add(Calendar.MONTH, -1)
        }.time
        return brandLocationDao.removeExpirationBrands(date.time)
    }
}
