package ap.mobile.composablemap.fake

import ap.mobile.composablemap.optimizer.Optimizer
import ap.mobile.composablemap.repository.PreferencesKeys

/**
 * Fake repository untuk settings — menggantikan DataStore agar tidak perlu Android context.
 * Gunakan Fake untuk test yang butuh baca/tulis preferensi secara berurutan.
 */
class FakeSettingsRepository {

    private val store = mutableMapOf<String, Any>(
        PreferencesKeys.OPTIMIZER to "ACO",
        PreferencesKeys.OPT_METHOD to "RANDOM",
        PreferencesKeys.HOST to "http://localhost",
        PreferencesKeys.USE_API to false,
        PreferencesKeys.HEURISTIC_INIT to false
    )

    var shouldThrowError = false

    fun getString(key: String): String? {
        if (shouldThrowError) throw Exception("Simulated error")
        return store[key] as? String
    }

    fun putString(key: String, value: String) {
        store[key] = value
    }

    fun getBoolean(key: String): Boolean? {
        return store[key] as? Boolean
    }

    fun putBoolean(key: String, value: Boolean) {
        store[key] = value
    }

    fun getOptimizer(): Optimizer {
        val value = getString(PreferencesKeys.OPTIMIZER) ?: "ACO"
        return Optimizer.valueOf(value)
    }

    fun setOptimizer(optimizer: Optimizer) {
        putString(PreferencesKeys.OPTIMIZER, optimizer.name)
    }
}
