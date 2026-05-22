package ap.mobile.composablemap

import android.app.Application
import ap.mobile.composablemap.fake.FakeDeliveryUseCase
import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.util.MainDispatcherRule
import ap.mobile.composablemap.viewmodel.MapViewModel
import com.google.android.gms.maps.model.LatLng
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeUseCase: FakeDeliveryUseCase
    private lateinit var viewModel: MapViewModel

    private val mockApp = mockk<Application>(relaxed = true)

    @Before
    fun setUp() {
        fakeUseCase = FakeDeliveryUseCase()
        every { mockApp.applicationContext } returns mockk(relaxed = true)

        // Inject FakeDeliveryUseCase — VM tidak tau soal Repository atau network
        viewModel = MapViewModel(app = mockApp, deliveryUseCase = fakeUseCase)
    }

    // --- State awal ---

    @Test
    fun initialState_mapHasDefaultPosition() {
        val state = viewModel.mapUiState.value

        assertEquals(-7.9666, state.currentPosition.latitude, 0.001)
        assertEquals(112.6326, state.currentPosition.longitude, 0.001)
    }

    @Test
    fun initialState_defaultZoomIs15() {
        assertEquals(15.0f, viewModel.mapUiState.value.zoom, 0.001f)
    }

    @Test
    fun initialState_deliveryRouteIsEmpty() {
        assertTrue(viewModel.mapUiState.value.deliveryRoute.isEmpty())
    }

    @Test
    fun initialState_parcelSheetIsHidden() {
        assertFalse(viewModel.parcelState.value.showParcelSheet)
    }

    // --- moveToLocation ---

    @Test
    fun moveToLocation_updatesCurrentPosition() {
        viewModel.moveToLocation(LatLng(-6.2088, 106.8456))

        assertEquals(-6.2088, viewModel.mapUiState.value.currentPosition.latitude, 0.001)
    }

    @Test
    fun moveToSingapore_updatesToCertainCoordinate() {
        viewModel.moveToSingapore()

        assertEquals(1.35, viewModel.mapUiState.value.currentPosition.latitude, 0.001)
        assertEquals(103.87, viewModel.mapUiState.value.currentPosition.longitude, 0.001)
    }

    // --- setZoomLevel ---

    @Test
    fun setZoomLevel_updatesZoomInState() {
        viewModel.setZoomLevel(18.0f)

        assertEquals(18.0f, viewModel.mapUiState.value.zoom, 0.001f)
    }

    // --- setCameraPosition ---

    @Test
    fun setCameraPosition_updatesCameraInState() {
        viewModel.setCameraPosition(LatLng(-7.25, 112.75))

        assertEquals(-7.25, viewModel.mapUiState.value.cameraPosition.latitude, 0.001)
    }

    // --- parcelSheet ---

    @Test
    fun parcelSheet_showTrueUpdatesState() {
        viewModel.parcelSheet(true)

        assertTrue(viewModel.parcelState.value.showParcelSheet)
    }

    @Test
    fun parcelSheet_showFalseHidesSheet() {
        viewModel.parcelSheet(true)
        viewModel.parcelSheet(false)

        assertFalse(viewModel.parcelState.value.showParcelSheet)
    }

    // --- selectParcel ---

    @Test
    fun selectParcel_updatesSelectedParcelInState() {
        val parcel = ParcelMapItem(id = 5, recipientName = "Nizar Zulfikar")

        viewModel.selectParcel(parcel)

        assertEquals(5, viewModel.parcelState.value.parcel.id)
        assertEquals("Nizar Zulfikar", viewModel.parcelState.value.parcel.recipientName)
    }

    @Test
    fun selectParcel_nullResetsToDefault() {
        viewModel.selectParcel(null)

        assertEquals(0, viewModel.parcelState.value.parcel.id)
    }

    // --- setProgress ---

    @Test
    fun setProgress_updatesComputingProgressInState() {
        viewModel.setProgress(0.75f)

        assertEquals(0.75f, viewModel.deliveryUiState.value.computingProgress, 0.001f)
    }

    // --- getParcels (via FakeDeliveryUseCase, tanpa network) ---

    @Test
    fun getParcels_loadsParcelFromFakeUseCase() = runTest {
        fakeUseCase.addParcel(ParcelMapItem(id = 1, recipientName = "Test User"))
        fakeUseCase.addParcel(ParcelMapItem(id = 2, recipientName = "Test User 2"))

        viewModel.getParcels()
        advanceUntilIdle()

        assertEquals(2, viewModel.parcelState.value.parcels.size)
    }

    @Test
    fun getParcels_emptyWhenFakeUseCaseHasNoParcels() = runTest {
        viewModel.getParcels()
        advanceUntilIdle()

        assertTrue(viewModel.parcelState.value.parcels.isEmpty())
    }

    @Test
    fun getParcels_parcelNameMatchesFakeData() = runTest {
        fakeUseCase.addParcel(ParcelMapItem(id = 1, recipientName = "Nizar Zulfikar"))

        viewModel.getParcels()
        advanceUntilIdle()

        assertEquals("Nizar Zulfikar", viewModel.parcelState.value.parcels[0].recipientName)
    }
}
