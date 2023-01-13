package com.android.systemui.wallet.p017ui;

import android.view.View;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1894R;

/* renamed from: com.android.systemui.wallet.ui.WalletCardViewHolder */
class WalletCardViewHolder extends RecyclerView.ViewHolder {
    final CardView mCardView;
    WalletCardViewInfo mCardViewInfo;
    final ImageView mImageView;

    WalletCardViewHolder(View view) {
        super(view);
        CardView cardView = (CardView) view.requireViewById(C1894R.C1898id.card);
        this.mCardView = cardView;
        this.mImageView = (ImageView) cardView.requireViewById(C1894R.C1898id.card_image);
    }
}
