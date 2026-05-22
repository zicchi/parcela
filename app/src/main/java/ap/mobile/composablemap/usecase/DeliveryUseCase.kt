package ap.mobile.composablemap.usecase

import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.repository.ParcelRepository

open class DeliveryUseCase(private val repo: ParcelRepository) {
    open fun getPackagesToDeliver(): List<ParcelMapItem> {
        return repo.getAllParcels()
    }
}