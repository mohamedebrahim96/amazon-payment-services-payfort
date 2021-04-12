package com.payfort.fortpaymentsdk.callbacks;

import android.content.Intent;

public interface FortCallBackManager {

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    class Factory {
        public static FortCallBackManager create() {
            return new FortCallback();
        }
    }
}
