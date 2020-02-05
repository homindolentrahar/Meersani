package com.homindolentrahar.meersani.di.component

import android.app.Application
import com.homindolentrahar.meersani.BaseApplication
import com.homindolentrahar.meersani.di.module.ActivityBuildersModule
import com.homindolentrahar.meersani.di.module.AppModule
import com.homindolentrahar.meersani.di.module.FragmentBuildersModule
import com.homindolentrahar.meersani.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        FragmentBuildersModule::class,
        AppModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}