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
    fun `getString OPTIMIZER returns ACO by default`() {
        val result = repository.getString(PreferencesKeys.OPTIMIZER)

        assertEquals("ACO", result)
    }

    @Test
    fun `getString HOST returns localhost by default`() {
        val result = repository.getString(PreferencesKeys.HOST)

        assertEquals("http://localhost", result)
    }

    @Test
    fun `getString returns null for unknown key`() {
        val result = repository.getString("UNKNOWN_KEY")

        assertNull(result)
    }

    @Test(expected = Exception::class)
    fun `getString throws when shouldThrowError is true`() {
        repository.shouldThrowError = true

        repository.getString(PreferencesKeys.HOST)
    }

    // --- putString ---

    @Test
    fun `putString OPTIMIZER then getString returns new value`() {
        repository.putString(PreferencesKeys.OPTIMIZER, "ABC")

        val result = repository.getString(PreferencesKeys.OPTIMIZER)

        assertEquals("ABC", result)
    }

    @Test
    fun `putString HOST updates value`() {
        repository.putString(PreferencesKeys.HOST, "http://192.168.1.1:8080")

        val result = repository.getString(PreferencesKeys.HOST)

        assertEquals("http://192.168.1.1:8080", result)
    }

    @Test
    fun `putString called twice keeps last value`() {
        repository.putString(PreferencesKeys.HOST, "http://first.com")
        repository.putString(PreferencesKeys.HOST, "http://second.com")

        val result = repository.getString(PreferencesKeys.HOST)

        assertEquals("http://second.com", result)
    }

    // --- getBoolean ---

    @Test
    fun `getBoolean USE_API returns false by default`() {
        val result = repository.getBoolean(PreferencesKeys.USE_API)

        assertFalse(result!!)
    }

    @Test
    fun `getBoolean HEURISTIC_INIT returns false by default`() {
        val result = repository.getBoolean(PreferencesKeys.HEURISTIC_INIT)

        assertFalse(result!!)
    }

    // --- putBoolean ---

    @Test
    fun `putBoolean USE_API true then getBoolean returns true`() {
        repository.putBoolean(PreferencesKeys.USE_API, true)

        val result = repository.getBoolean(PreferencesKeys.USE_API)

        assertTrue(result!!)
    }

    @Test
    fun `putBoolean HEURISTIC_INIT true then getBoolean returns true`() {
        repository.putBoolean(PreferencesKeys.HEURISTIC_INIT, true)

        val result = repository.getBoolean(PreferencesKeys.HEURISTIC_INIT)

        assertTrue(result!!)
    }

    // --- getOptimizer ---

    @Test
    fun `getOptimizer returns ACO by default`() {
        val result = repository.getOptimizer()

        assertEquals(Optimizer.ACO, result)
    }

    @Test
    fun `setOptimizer ABC then getOptimizer returns ABC`() {
        repository.setOptimizer(Optimizer.ABC)

        val result = repository.getOptimizer()

        assertEquals(Optimizer.ABC, result)
    }

    @Test
    fun `setOptimizer can switch back and forth`() {
        repository.setOptimizer(Optimizer.ABC)
        repository.setOptimizer(Optimizer.ACO)

        val result = repository.getOptimizer()

        assertEquals(Optimizer.ACO, result)
    }
}
