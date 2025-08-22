package com.lighthouse.beep.data.repository.brand

import com.lighthouse.beep.model.brand.BrandLocation
import com.lighthouse.beep.model.location.Dms
import com.lighthouse.beep.model.location.DmsPos
import com.lighthouse.beep.model.location.DmsRect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class BrandRepositoryImpl @Inject constructor(
    private val remoteBrandDataSource: RemoteBrandDataSource,
    private val localBrandDataSource: LocalBrandDataSource,
) : BrandRepository {

    /**
     * 브랜드별 가장 가까운것을 찾기 위해 한번에 가져옴
     * depth 를 기본적으로 1로 설정
     */
    override suspend fun searchBrandLocationList(
        brandNames: List<String>,
        xDD: Double,
        yDD: Double,
        cachedSectionIds: Set<Long>,
        depth: Int,
        gap: Int,
    ): Result<List<BrandLocation>> = runCatching {
        mutableListOf<BrandLocation>().also { list ->
            for (brand in brandNames) {
                findSection(DmsPos(xDD, yDD), depth, gap) { nx, ny ->
                    val sectionId = localBrandDataSource.getSectionId(brand, nx, ny)
                    if (sectionId in cachedSectionIds) {
                        return@findSection
                    }
                    list += getBrandPlaceInfo(brand, nx, ny, sectionId, depth, gap).getOrThrow()
                }
            }
        }
    }

    /**
     * 지도에서 빠른 로딩을 위해 검색 되는것 마다 바로 전달
     * depth 를 기본적으로 2로 설정
     */
    override fun searchBrandLocationListWithFlow(
        brandNames: List<String>,
        xDD: Double,
        yDD: Double,
        cachedSectionIds: Set<Long>,
        depth: Int,
        gap: Int,
    ): Flow<Result<List<BrandLocation>>> = flow {
        for (brand in brandNames) {
            findSection(DmsPos(xDD, yDD), depth, gap) { nx, ny ->
                val sectionId = localBrandDataSource.getSectionId(brand, nx, ny)
                if (sectionId in cachedSectionIds) {
                    return@findSection
                }
                emit(getBrandPlaceInfo(brand, nx, ny, sectionId, depth, gap))
            }
        }
    }

    private suspend fun findSection(
        dmsPos: DmsPos,
        depth: Int,
        gap: Int,
        block: suspend (baseX: Dms, baseY: Dms) -> Unit,
    ) {
        findSection(dmsPos.lat, dmsPos.lon, depth, gap, block)
    }

    private suspend fun findSection(
        x: Dms,
        y: Dms,
        depth: Int,
        gap: Int,
        block: suspend (nx: Dms, ny: Dms) -> Unit,
    ) {
        val xBase = x.sampling(sGap = gap)
        val yBase = y.sampling(sGap = gap)
        for (py in -depth..depth) {
            for (px in -depth..depth) {
                val nx = xBase + Dms(seconds = px * gap)
                val ny = yBase + Dms(seconds = py * gap)
                block(nx, ny)
            }
        }
    }

    private suspend fun getBrandPlaceInfo(
        brand: String,
        nx: Dms,
        ny: Dms,
        sectionId: Long?,
        depth: Int,
        gap: Int,
    ): Result<List<BrandLocation>> = runCatching {
        if (sectionId == null) {
            // 검색 범위를 depth * 2로 하면 못찾은 구역을 발견 해도 모든 곳을 한번에 다운
            searchBrandPlaceInfo(brand, nx, ny, depth * 2, gap)
            localBrandDataSource.getBrandPlaceInfo(brand, nx, ny)
        } else {
            localBrandDataSource.getBrandPlaceInfo(sectionId)
        }
    }

    private suspend fun searchBrandPlaceInfo(
        brand: String,
        x: Dms,
        y: Dms,
        depth: Int,
        gap: Int,
    ) {
        val xBase = x.sampling(sGap = gap)
        val yBase = y.sampling(sGap = gap)
        val nRect = DmsRect(
            xBase + Dms(seconds = -depth * gap),
            yBase + Dms(seconds = -depth * gap),
            xBase + Dms(seconds = (depth + 1) * gap),
            yBase + Dms(seconds = (depth + 1) * gap),
        )
        val downloadList = remoteBrandDataSource.getBrandPlaceInfo(brand, nRect).getOrThrow()
        findSection(x, y, depth, gap) { nx, ny ->
            localBrandDataSource.insertSection(brand, nx, ny)
        }
        localBrandDataSource.insertBrandPlaceInfo(downloadList, gap)
    }

    override suspend fun removeExpirationBrands() {
        localBrandDataSource.removeExpirationBrands()
    }

    // TODO: useCase 로 이동 하기
    companion object {
        private const val DEPTH = 2
        private const val GAP = 20
    }
}
