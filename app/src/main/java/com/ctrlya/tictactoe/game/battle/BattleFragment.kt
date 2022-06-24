package com.arbonik.tictactoebot.ui.game.battle


class BattleFragment @Inject constructor() :
    GameFragment<BattleViewModel, BattlePartyController>() {
    override val gameViewModel: BattleViewModel by hiltNavGraphViewModels(R.id.navigation_main)
    private val args: BattleFragmentArgs by navArgs()
    override val game: Game? by lazy { args.game }
}