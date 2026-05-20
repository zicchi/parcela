package ap.mobile.composablemap.usecase

import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.repository.ParcelRepository

class DeliveryUseCase {
    companion object{
        fun getPackagesToDeliver(repo: ParcelRepository): List<ParcelMapItem> {
            return repo.getAllParcels()
        }
    }
}