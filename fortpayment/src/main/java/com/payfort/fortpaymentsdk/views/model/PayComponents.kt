package com.payfort.fortpaymentsdk.views.model

import com.payfort.fortpaymentsdk.views.CardCvvView
import com.payfort.fortpaymentsdk.views.CardExpiryView
import com.payfort.fortpaymentsdk.views.CardHolderNameView
import com.payfort.fortpaymentsdk.views.FortCardNumberView

data class PayComponents(
    internal val cardNumberView: FortCardNumberView,
    internal val cvvView: CardCvvView,
    internal val cardExpiryView: CardExpiryView,
    internal val holderNameView: CardHolderNameView
)
