package id.apwdevs.app.discover.ui

import id.apwdevs.app.res.util.PageType

interface FragmentMessenger {
    fun onItemClick(pageType: PageType, item: Any)
}