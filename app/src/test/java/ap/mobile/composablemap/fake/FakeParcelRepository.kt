package ap.mobile.composablemap.fake

import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.optimizer.Delivery
import ap.mobile.composablemap.optimizer.Optimizer
import ap.mobile.composablemap.repository.Result
import java.io.IOException

/**
 * Fake pengganti ParcelRepository — tidak butuh Android Context.
 * Dipakai di unit test agar bisa jalan tanpa emulator.
 */
class FakeParcelRepository {

    private val parcels = mutableListOf<ParcelMapItem>()
    var shouldThrowError = false

    fun addParcel(parcel: ParcelMapItem) { parcels.add(parcel) }
    fun clearParcels() { parcels.clear() }

    fun getAllParcels(): List<ParcelMapItem> {
        if (shouldThrowError) throw IOException("Simulated error")
        return parcels.toList()
    }

    suspend fun computeDelivery(
        optimizer: Optimizer,
        startParcel: ParcelMapItem? = null,
        progress: (Float) -> Unit = {}
    ): Result<Delivery> {
        if (shouldThrowError) return Result.Error(Exception("Simulated error"))
        if (parcels.isEmpty()) return Result.Error(Exception("No parcels"))
        progress(0.5f)
        progress(1.0f)
        return Result.Success(Delivery(parcels.toList(), distance = 1500f, duration = 60f))
    }
}
