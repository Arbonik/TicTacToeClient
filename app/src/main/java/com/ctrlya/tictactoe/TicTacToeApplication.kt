package com.ctrlya.tictactoe

import android.app.Application
import androidx.room.Room
import com.ctrlya.tictactoe.database.TicTacToeDatabase
import com.ctrlya.tictactoe.database.memory.MemoryInteractor
import com.ctrlya.tictactoe.database.memory.MemoryRepository
import com.ctrlya.tictactoe.network.NetworkGameInteractor
import com.ctrlya.tictactoe.network.TicTacToeClient
import com.ctrlya.tictactoe.ui.game.GameViewModel
import com.ctrlya.tictactoe.ui.learn.LearnViewModel
import com.ctrlya.tictactoe.ui.network.ConnectGameViewModel
import com.ctrlya.tictactoe.ui.rate.RatingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class TicTacToeApplication : Application() {

    val appModule = module {
        factory { TicTacToeClient() }
        factory { NetworkGameInteractor(get()) }

        single {
            Room.databaseBuilder(
                androidContext(),
                TicTacToeDatabase::class.java,
                "TicTacToeDatamase"
            ).build()
        }
        single { get<TicTacToeDatabase>().memoryDao() }
        single { MemoryRepository(get()) }
        single { MemoryInteractor(get()) }

        viewModel<GameViewModel> { GameViewModel(get()) }
        viewModel<ConnectGameViewModel> { ConnectGameViewModel(get()) }
        viewModel<LearnViewModel> { LearnViewModel(get()) }
        viewModel<RatingViewModel> { RatingViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@TicTacToeApplication)
            modules(appModule)
        }
    }
    // тз:
    // разработать приложение крестики - нолики для игры по сети
    // регистрация - авторизация
    // кнопка найти игру
    // настройки игры - размер поля, количество до победы
    // игра на устройстве и по сети
    // сохранение партии и просмотр как в плееере -> след ход

    // разработать архитектуру приложения:
    // экраны (навигация, без дизайна), viewModels, репозитории
    // типы данных используемых (передаваемые по сети)
    // запросы используемые и веб сокеты, места подключения

    // документация по core
    // примеры для создания, настройки игры
    // пример для создания игрока
    // пример для создания правил игры


}