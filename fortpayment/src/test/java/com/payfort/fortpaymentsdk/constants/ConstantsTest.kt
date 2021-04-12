package com.payfort.fortpaymentsdk.constants

import org.junit.Assert.*
import org.junit.Test

class ConstantsTest {


    @Test
    fun testConstants(){
        assertEquals(Constants.ENVIRONMENT_PRODUCTION,"https://checkout.payfort.com")
        assert(Constants.LANGUAGES.ARABIC=="ar")

    }
}