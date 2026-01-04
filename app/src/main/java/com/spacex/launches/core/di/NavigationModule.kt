package com.spacex.launches.core.di

import com.spacex.launches.core.navigation.Navigator
import com.spacex.launches.core.navigation.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigatorRepository(navigatorImpl: NavigatorImpl): Navigator
}