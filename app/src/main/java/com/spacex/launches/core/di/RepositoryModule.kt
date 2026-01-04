package com.spacex.launches.core.di

import com.spacex.launches.launchesList.data.repository.LaunchesListRepositoryImpl
import com.spacex.launches.launchesList.domain.repository.LaunchesListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLaunchRepository(launchesListRepositoryImpl: LaunchesListRepositoryImpl): LaunchesListRepository
}