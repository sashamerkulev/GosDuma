package ru.merkulyevsasha.gddomain

import android.content.Context
import android.content.Intent
import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.domain.R
import ru.merkulyevsasha.gdcore.AktDistributor
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

class AktDistributorImpl(
    private val context: Context,
    private val resourceProvider: ResourceProvider
) : AktDistributor {
    override fun distribute(item: Akt) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val sb = StringBuilder()
        sb.append(String.format(resourceProvider.getString(R.string.akt_info_send), item.sourceName, item.title))
        sb.append(resourceProvider.getString(R.string.application_info_send))
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString())
        context.startActivity(Intent.createChooser(intent, resourceProvider.getString(R.string.akt_chooser_title)))
    }

    override fun distribute(item: AktComment) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val sb = StringBuilder()
        sb.append(String.format(resourceProvider.getString(R.string.aktcomment_info_send), item.userName, item.comment))
        sb.append(resourceProvider.getString(R.string.application_info_send))
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString())
        context.startActivity(Intent.createChooser(intent, resourceProvider.getString(R.string.akt_chooser_title)))
    }
}