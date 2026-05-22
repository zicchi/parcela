package ap.mobile.composablemap

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit4.runners.AndroidJUnit4
import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.view.DeliveryContent
import ap.mobile.composablemap.view.DeliveryMetaInformation
import ap.mobile.composablemap.view.ParcelDeliveryItem
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeliveryContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- ParcelDeliveryItem: tampilan nama penerima ---

    @Test
    fun parcelDeliveryItem_showsRecipientName() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelDeliveryItem(
                    parcel = ParcelMapItem(
                        id = 1,
                        recipientName = "Djoko Sudemo",
                        address = "Jl. Soekarno Hatta 12"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Djoko Sudemo").assertIsDisplayed()
    }

    // --- ParcelDeliveryItem: tampilan alamat ---

    @Test
    fun parcelDeliveryItem_showsAddress() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelDeliveryItem(
                    parcel = ParcelMapItem(
                        id = 1,
                        recipientName = "Djoko Sudemo",
                        address = "Jl. Soekarno Hatta 12"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Jl. Soekarno Hatta 12").assertIsDisplayed()
    }

    // --- DeliveryMetaInformation: menampilkan jumlah paket ---

    @Test
    fun deliveryMetaInformation_showsParcelCount() {
        val parcels = listOf(
            ParcelMapItem(id = 1, recipientName = "A"),
            ParcelMapItem(id = 2, recipientName = "B"),
            ParcelMapItem(id = 3, recipientName = "C"),
        )

        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryMetaInformation(
                    parcels = parcels,
                    distance = 0f,
                    duration = 0f
                )
            }
        }

        // Menampilkan "3" (jumlah parcel)
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
    }

    // --- DeliveryMetaInformation: menampilkan jarak terformat ---

    @Test
    fun deliveryMetaInformation_showsFormattedDistance() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryMetaInformation(
                    parcels = emptyList(),
                    distance = 1.5f,
                    duration = 0f
                )
            }
        }

        // Format: "%.2f km" → "1.50 km"
        composeTestRule.onNodeWithText("1.50 km").assertIsDisplayed()
    }

    // --- DeliveryMetaInformation: menampilkan durasi terformat ---

    @Test
    fun deliveryMetaInformation_showsFormattedDuration() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryMetaInformation(
                    parcels = emptyList(),
                    distance = 0f,
                    duration = 0.5f
                )
            }
        }

        // Format: "%.2f hrs" → "0.50 hrs"
        composeTestRule.onNodeWithText("0.50 hrs").assertIsDisplayed()
    }

    // --- DeliveryContent: tombol "Delivery Route" saat tidak loading ---

    @Test
    fun deliveryContent_notLoading_showsDeliveryRouteButton() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryContent(
                    isLoading = false,
                    parcels = emptyList(),
                    loadingProgress = 0f
                )
            }
        }

        composeTestRule.onNodeWithText("Delivery Route").assertIsDisplayed()
    }

    // --- DeliveryContent: tombol "Stop" saat loading ---

    @Test
    fun deliveryContent_loading_showsStopButton() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryContent(
                    isLoading = true,
                    parcels = emptyList(),
                    loadingProgress = 0.4f
                )
            }
        }

        composeTestRule.onNodeWithText("Stop").assertIsDisplayed()
    }

    // --- DeliveryContent: teks progress saat loading ---

    @Test
    fun deliveryContent_loading_showsProgressText() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryContent(
                    isLoading = true,
                    parcels = emptyList(),
                    loadingProgress = 0.6f
                )
            }
        }

        // "Computing delivery route... 60%"
        composeTestRule.onNodeWithText("Computing delivery route... 60%").assertIsDisplayed()
    }

    // --- DeliveryContent: daftar parcel tampil saat tidak loading ---

    @Test
    fun deliveryContent_notLoading_showsParcelList() {
        val parcels = listOf(
            ParcelMapItem(id = 1, recipientName = "Eko Prasetyo"),
            ParcelMapItem(id = 2, recipientName = "Rina Wulandari"),
        )

        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryContent(
                    isLoading = false,
                    parcels = parcels,
                    loadingProgress = 0f
                )
            }
        }

        composeTestRule.onNodeWithText("Eko Prasetyo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rina Wulandari").assertIsDisplayed()
    }

    // --- DeliveryContent: callback dipanggil saat tombol diklik ---

    @Test
    fun deliveryContent_buttonClick_triggersCallback() {
        var clicked = false

        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryContent(
                    isLoading = false,
                    parcels = emptyList(),
                    loadingProgress = 0f,
                    onGetDeliveryRecommendation = { clicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Delivery Route").performClick()

        assertTrue(clicked)
    }

    // --- DeliveryContent: callback TIDAK dipanggil saat loading dan tombol diklik ---

    @Test
    fun deliveryContent_loading_buttonClickDoesNotTriggerCallback() {
        var clicked = false

        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryContent(
                    isLoading = true,
                    parcels = emptyList(),
                    loadingProgress = 0.3f,
                    onGetDeliveryRecommendation = { clicked = true }
                )
            }
        }

        // Klik tombol "Stop" saat isLoading = true
        // Logika di composable: if (!isLoading) onGetDeliveryRecommendation() — jadi callback tidak dipanggil
        composeTestRule.onNodeWithText("Stop").performClick()

        assertTrue(!clicked)
    }

    // --- DeliveryContent: meta information selalu tampil ---

    @Test
    fun deliveryContent_showsMetaInformation() {
        val parcels = listOf(
            ParcelMapItem(id = 1, recipientName = "A"),
            ParcelMapItem(id = 2, recipientName = "B"),
        )

        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                DeliveryContent(
                    isLoading = false,
                    parcels = parcels,
                    loadingProgress = 0f,
                    distance = 3.75f,
                    duration = 1.25f
                )
            }
        }

        // Jumlah parcel, jarak, durasi semuanya tampil
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        composeTestRule.onNodeWithText("3.75 km").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.25 hrs").assertIsDisplayed()
    }
}
