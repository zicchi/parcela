package ap.mobile.composablemap.fake

import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.repository.ParcelRepository
import ap.mobile.composablemap.usecase.DeliveryUseCase
import io.mockk.mockk

class FakeDeliveryUseCase(
    private val fakeParcels: MutableList<ParcelMapItem> = mutableListOf()
) : DeliveryUseCase(
    // repo tidak pernah dipakai karena getPackagesToDeliver() di-override
    repo = mockk(relaxed = true)
) {
    fun addParcel(parcel: ParcelMapItem) { fakeParcels.add(parcel) }

    override fun getPackagesToDeliver(): List<ParcelMapItem> {
        return fakeParcels.toList()
    }
}
