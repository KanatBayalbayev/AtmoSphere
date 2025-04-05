package dev.android.atmosphere.domain.repository

import dev.android.atmosphere.domain.model.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {

    fun observeNetworkStatus(): Flow<NetworkStatus>

    fun isNetworkAvailable(): Boolean
}