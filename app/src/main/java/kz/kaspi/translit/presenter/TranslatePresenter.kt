package kz.kaspi.translit.presenter

import androidx.lifecycle.LiveData
import kz.kaspi.translit.contract.ContractInterface
import kz.kaspi.translit.data.entity.TranslateEntity
import kz.kaspi.translit.view.fragments.MainFragments

class TranslatePresenter(_view: ContractInterface.View): ContractInterface.Presenter {

    private var view: ContractInterface.View = _view

    init {
        view.initView()
    }

//click fragment, send args -> call func translate -> save message -> Model
    override  fun startFunction(arg : String) {
        MainFragments.modell?.saveMessageKirLat(arg)
        view.updateViewData()
    }

    override fun getMsgPresenter(): LiveData<List<TranslateEntity>>? {
            return  MainFragments.modell?.getAllMsgKirLat()
    }
}