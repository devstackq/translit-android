package kz.kaspi.translit.contract

import androidx.lifecycle.LiveData
import kz.kaspi.translit.data.entity.TranslateEntity

interface ContractInterface {

    interface View {
        fun initView()
        fun updateViewData()
    }

    interface Presenter {
        fun startFunction(arg: String)
        fun getMsgPresenter(): LiveData<List<TranslateEntity>>?
    }

    interface Model {
        fun getAllMsgKirLat(): LiveData<List<TranslateEntity>>
        fun saveMessageKirLat(input: String)
    }
}