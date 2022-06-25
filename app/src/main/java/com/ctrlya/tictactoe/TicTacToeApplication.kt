package com.ctrlya.tictactoe

import android.app.Application
import com.ctrlya.tictactoe.network.NetworkGameInteractor
import com.ctrlya.tictactoe.network.TicTacToeClient
import com.ctrlya.tictactoe.ui.game.GameViewModel
import com.ctrlya.tictactoe.ui.network.ConnectGameViewModel
import com.ctrlya.tictactoe.ui.network.NetworkGameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class TicTacToeApplication : Application() {

    val appModule = module {
        factory { TicTacToeClient() }
        factory { NetworkGameInteractor(get()) }

        viewModel<GameViewModel>{ GameViewModel(get()) }
        viewModel<ConnectGameViewModel>{ ConnectGameViewModel(get()) }
        viewModel<NetworkGameViewModel>{ NetworkGameViewModel(get(),get()) }
    }

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@TicTacToeApplication)
            modules(appModule)
        }
    }
}