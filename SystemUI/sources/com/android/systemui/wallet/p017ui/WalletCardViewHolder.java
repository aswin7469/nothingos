package com.android.systemui.wallet.p017ui;

import android.view.View;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1893R;

/* renamed from: com.android.systemui.wallet.ui.WalletCardViewHolder */
class WalletCardViewHolder extends RecyclerView.ViewHolder {
    final CardView mCardView;
    WalletCardViewInfo mCardViewInfo;
    final ImageView mImageView;

    WalletCardViewHolder(View view) {
        super(view);
        CardView cardView = (CardView) view.requireViewById(C1893R.C1897id.card);
        this.mCardView = cardView;
        this.mImageView = (ImageView) cardView.requireViewById(C1893R.C1897id.card_image);
    }
}
