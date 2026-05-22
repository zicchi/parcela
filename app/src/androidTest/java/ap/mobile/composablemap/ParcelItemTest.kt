package ap.mobile.composablemap

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit4.runners.AndroidJUnit4
import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.view.ParcelDestination
import ap.mobile.composablemap.view.ParcelItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParcelItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- ParcelItem: tampilan nama penerima ---

    @Test
    fun parcelItem_showsRecipientName() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelItem(
                    parcel = ParcelMapItem(
                        id = 1,
                        recipientName = "Budi Santoso",
                        address = "Jl. Malioboro No. 1"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Budi Santoso").assertIsDisplayed()
    }

    // --- ParcelItem: tampilan alamat ---

    @Test
    fun parcelItem_showsAddress() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelItem(
                    parcel = ParcelMapItem(
                        id = 1,
                        recipientName = "Budi Santoso",
                        address = "Jl. Malioboro No. 1"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Jl. Malioboro No. 1").assertIsDisplayed()
    }

    // --- ParcelItem: tampilan koordinat ---

    @Test
    fun parcelItem_showsCoordinates() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelItem(
                    parcel = ParcelMapItem(
                        id = 1,
                        lat = -7.9666,
                        lng = 112.6326,
                        recipientName = "Test User"
                    )
                )
            }
        }

        // Format: " ${parcel.lat}, ${parcel.lng}" (ada spasi di depan)
        composeTestRule.onNodeWithText(" -7.9666, 112.6326").assertIsDisplayed()
    }

    // --- ParcelItem: paket reguler tetap tampil ---

    @Test
    fun parcelItem_regularType_showsName() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelItem(
                    parcel = ParcelMapItem(
                        id = 2,
                        recipientName = "Siti Rahayu",
                        address = "Jl. Diponegoro 5",
                        type = "Regular"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Siti Rahayu").assertIsDisplayed()
    }

    // --- ParcelItem: paket Priority tetap menampilkan nama ---

    @Test
    fun parcelItem_priorityType_showsRecipientName() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelItem(
                    parcel = ParcelMapItem(
                        id = 3,
                        recipientName = "Ahmad Fauzi",
                        address = "Jl. Ahmad Yani 10",
                        type = "Priority"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Ahmad Fauzi").assertIsDisplayed()
    }

    // --- ParcelDestination: menampilkan semua parcel dalam list ---

    @Test
    fun parcelDestination_showsAllParcels() {
        val parcels = listOf(
            ParcelMapItem(id = 1, recipientName = "User Satu", address = "Alamat 1"),
            ParcelMapItem(id = 2, recipientName = "User Dua", address = "Alamat 2"),
            ParcelMapItem(id = 3, recipientName = "User Tiga", address = "Alamat 3"),
        )

        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelDestination(parcels = parcels, onBackHandler = {})
            }
        }

        composeTestRule.onNodeWithText("User Satu").assertIsDisplayed()
        composeTestRule.onNodeWithText("User Dua").assertIsDisplayed()
        composeTestRule.onNodeWithText("User Tiga").assertIsDisplayed()
    }

    // --- ParcelDestination: list kosong tidak crash ---

    @Test
    fun parcelDestination_emptyList_doesNotCrash() {
        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelDestination(parcels = emptyList(), onBackHandler = {})
            }
        }

        // Tidak ada node parcel — composable tetap bisa render tanpa crash
        composeTestRule.onNodeWithText("User Satu").assertDoesNotExist()
    }

    // --- ParcelDestination: hanya 1 parcel ---

    @Test
    fun parcelDestination_singleParcel_showsCorrectly() {
        val parcels = listOf(
            ParcelMapItem(id = 10, recipientName = "Nizar Zulfikar", address = "Jl. Veteran 3")
        )

        composeTestRule.setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                ParcelDestination(parcels = parcels, onBackHandler = {})
            }
        }

        composeTestRule.onNodeWithText("Nizar Zulfikar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jl. Veteran 3").assertIsDisplayed()
    }
}
