package com.ledger.app.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.app.utils.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("ledger_settings", Context.MODE_PRIVATE)
    }

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val currency = sharedPreferences.getString("currency", Constants.DEFAULT_CURRENCY)
            ?: Constants.DEFAULT_CURRENCY
        val theme =
            sharedPreferences.getString("theme", Constants.THEME_SYSTEM) ?: Constants.THEME_SYSTEM

        _state.update {
            it.copy(
                currency = currency,
                theme = theme
            )
        }
    }

    fun updateCurrency(currency: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString("currency", currency).apply()
            _state.update { it.copy(currency = currency) }
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString("theme", theme).apply()
            _state.update { it.copy(theme = theme) }
        }
    }

    fun exportData() {
        viewModelScope.launch {
            _state.update { it.copy(isExporting = true) }

            try {
                // TODO: Implement data export logic
                _state.update {
                    it.copy(
                        isExporting = false,
                        exportSuccess = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isExporting = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun importData() {
        viewModelScope.launch {
            _state.update { it.copy(isImporting = true) }

            try {
                // TODO: Implement data import logic
                _state.update {
                    it.copy(
                        isImporting = false,
                        importSuccess = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isImporting = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun backupData() {
        viewModelScope.launch {
            _state.update { it.copy(isBackingUp = true) }

            try {
                // TODO: Implement data backup logic
                _state.update {
                    it.copy(
                        isBackingUp = false,
                        backupSuccess = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isBackingUp = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun resetState() {
        _state.update {
            it.copy(
                exportSuccess = false,
                importSuccess = false,
                backupSuccess = false,
                error = null
            )
        }
    }

    data class SettingsState(
        val currency: String = Constants.DEFAULT_CURRENCY,
        val theme: String = Constants.THEME_SYSTEM,
        val isExporting: Boolean = false,
        val isImporting: Boolean = false,
        val isBackingUp: Boolean = false,
        val exportSuccess: Boolean = false,
        val importSuccess: Boolean = false,
        val backupSuccess: Boolean = false,
        val error: String? = null
    )
}
