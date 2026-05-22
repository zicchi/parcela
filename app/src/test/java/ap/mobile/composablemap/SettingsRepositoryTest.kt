package ap.mobile.composablemap

import ap.mobile.composablemap.fake.FakeSettingsRepository
import ap.mobile.composablemap.optimizer.Optimizer
import ap.mobile.composablemap.repository.PreferencesKeys
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SettingsRepositoryTest {

    private lateinit var repository: FakeSettingsRepository

    @Before
    fun setUp() {
        repository = FakeSettingsRepository()
    }

    // --- getString ---

    @Test
    fun getString_optimizerReturnsAcoByDefault() {
        val result = repository.getString(PreferencesKeys.OPTIMIZER)

        assertEquals("ACO", result)
    }

    @Test
    fun getString_hostReturnsLocalhostByDefault() {
        val result = repository.getString(PreferencesKeys.HOST)

        assertEquals("http://localhost", result)
    }

    @Test
    fun getString_returnsNullForUnknownKey() {
        val result = repository.getString("UNKNOWN_KEY")

        assertNull(result)
    }

    @Test(expected = Exception::class)
    fun getString_throwsWhenShouldThrowError() {
        repository.shouldThrowError = true

        repository.getString(PreferencesKeys.HOST)
    }

    // --- putString ---

    @Test
    fun putString_optimizerThenGetStringReturnsNewValue() {
        repository.putString(PreferencesKeys.OPTIMIZER, "ABC")

        val result = repository.getString(PreferencesKeys.OPTIMIZER)

        assertEquals("ABC", result)
    }

    @Test
    fun putString_hostUpdatesValue() {
        repository.putString(PreferencesKeys.HOST, "http://192.168.1.1:8080")

        val result = repository.getString(PreferencesKeys.HOST)

        assertEquals("http://192.168.1.1:8080", result)
    }

    @Test
    fun putString_calledTwiceKeepsLastValue() {
        repository.putString(PreferencesKeys.HOST, "http://first.com")
        repository.putString(PreferencesKeys.HOST, "http://second.com")

        val result = repository.getString(PreferencesKeys.HOST)

        assertEquals("http://second.com", result)
    }

    // --- getBoolean ---

    @Test
    fun getBoolean_useApiReturnsFalseByDefault() {
        val result = repository.getBoolean(PreferencesKeys.USE_API)

        assertFalse(result!!)
    }

    @Test
    fun getBoolean_heuristicInitReturnsFalseByDefault() {
        val result = repository.getBoolean(PreferencesKeys.HEURISTIC_INIT)

        assertFalse(result!!)
    }

    // --- putBoolean ---

    @Test
    fun putBoolean_useApiTrueThenGetBooleanReturnsTrue() {
        repository.putBoolean(PreferencesKeys.USE_API, true)

        val result = repository.getBoolean(PreferencesKeys.USE_API)

        assertTrue(result!!)
    }

    @Test
    fun putBoolean_heuristicInitTrueThenGetBooleanReturnsTrue() {
        repository.putBoolean(PreferencesKeys.HEURISTIC_INIT, true)

        val result = repository.getBoolean(PreferencesKeys.HEURISTIC_INIT)

        assertTrue(result!!)
    }

    // --- getOptimizer ---

    @Test
    fun getOptimizer_returnsAcoByDefault() {
        val result = repository.getOptimizer()

        assertEquals(Optimizer.ACO, result)
    }

    @Test
    fun setOptimizer_abcThenGetOptimizerReturnsAbc() {
        repository.setOptimizer(Optimizer.ABC)

        val result = repository.getOptimizer()

        assertEquals(Optimizer.ABC, result)
    }

    @Test
    fun setOptimizer_canSwitchBackAndForth() {
        repository.setOptimizer(Optimizer.ABC)
        repository.setOptimizer(Optimizer.ACO)

        val result = repository.getOptimizer()

        assertEquals(Optimizer.ACO, result)
    }
}
