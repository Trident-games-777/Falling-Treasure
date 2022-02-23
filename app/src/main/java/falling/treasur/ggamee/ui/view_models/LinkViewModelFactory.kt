package falling.treasur.ggamee.ui.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import falling.treasur.ggamee.database.LinkDb

class LinkViewModelFactory(
    private val app: Application,
    private val db: LinkDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LinkViewModel(app, db) as T
    }
}