package id.apwdevs.app.detail.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.apwdevs.app.detail.databinding.ItemNodataBinding

class NoDataItem: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ItemNodataBinding.inflate(inflater, container, false).root
    }

}