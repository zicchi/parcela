package ap.mobile.composablemap

import ap.mobile.composablemap.fake.FakeParcelRepository
import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.optimizer.Optimizer
import ap.mobile.composablemap.repository.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ParcelRepositoryTest {

    private lateinit var repository: FakeParcelRepository

    private val parcel1 = ParcelMapItem(
        id = 1, lat = -8.01815, lng = 112.62943,
        recipientName = "Nizar Zulfikar", type = "Regular"
    )
    private val parcel2 = ParcelMapItem(
        id = 2, lat = -8.01895, lng = 112.62941,
        recipientName = "Ibrahim Eka", type = "Priority"
    )

    @Before
    fun setUp() {
        repository = FakeParcelRepository()
    }

    // --- getAllParcels ---

    @Test
    fun `getAllParcels returns all added parcels`() {
        repository.addParcel(parcel1)
        repository.addParcel(parcel2)

        val result = repository.getAllParcels()

        assertEquals(2, result.size)
    }

    @Test
    fun `getAllParcels returns empty when no parcel added`() {
        val result = repository.getAllParcels()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllParcels returns correct recipient name`() {
        repository.addParcel(parcel1)

        val result = repository.getAllParcels()

        assertEquals("Nizar Zulfikar", result[0].recipientName)
    }

    @Test
    fun `getAllParcels returns correct parcel type`() {
        repository.addParcel(parcel2)

        val result = repository.getAllParcels()

        assertEquals("Priority", result[0].type)
    }

    @Test(expected = Exception::class)
    fun `getAllParcels throws exception when shouldThrowError is true`() {
        repository.shouldThrowError = true

        repository.getAllParcels()
    }

    // --- computeDelivery ---

    @Test
    fun `computeDelivery returns Success when parcels exist`() = runBlocking {
        repository.addParcel(parcel1)
        repository.addParcel(parcel2)

        val result = repository.computeDelivery(Optimizer.ACO)

        assertTrue(result is Result.Success)
    }

    @Test
    fun `computeDelivery returns Error when no parcels`() = runBlocking {
        val result = repository.computeDelivery(Optimizer.ACO)

        assertTrue(result is Result.Error)
    }

    @Test
    fun `computeDelivery returns Error when shouldThrowError is true`() = runBlocking {
        repository.addParcel(parcel1)
        repository.shouldThrowError = true

        val result = repository.computeDelivery(Optimizer.ACO)

        assertTrue(result is Result.Error)
    }

    @Test
    fun `computeDelivery result contains correct parcel count`() = runBlocking {
        repository.addParcel(parcel1)
        repository.addParcel(parcel2)

        val result = repository.computeDelivery(Optimizer.ACO)
        val delivery = (result as Result.Success).data

        assertEquals(2, delivery.parcels.size)
    }

    @Test
    fun `computeDelivery result has positive distance and duration`() = runBlocking {
        repository.addParcel(parcel1)

        val result = repository.computeDelivery(Optimizer.ABC)
        val delivery = (result as Result.Success).data

        assertTrue(delivery.distance > 0f)
        assertTrue(delivery.duration > 0f)
    }

    @Test
    fun `computeDelivery calls progress callback`() = runBlocking {
        repository.addParcel(parcel1)
        val progressList = mutableListOf<Float>()

        repository.computeDelivery(Optimizer.ACO, progress = { progressList.add(it) })

        assertTrue(progressList.isNotEmpty())
        assertEquals(1.0f, progressList.last(), 0.001f)
    }
}
