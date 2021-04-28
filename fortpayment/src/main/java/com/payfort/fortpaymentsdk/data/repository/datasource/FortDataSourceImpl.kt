package com.payfort.fortpaymentsdk.data.repository.datasource

import com.google.gson.Gson
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.security.DataSecurityService
import com.payfort.fortpaymentsdk.security.aes.AESCipherManager
import com.payfort.fortpaymentsdk.utils.MapUtils
import io.reactivex.Observable
import java.security.interfaces.RSAPublicKey
import javax.crypto.spec.SecretKeySpec

/**
 * FortDataSourceImpl implements FortDataSource
 * @property fortEndpoint FortEndpoint
 * @constructor
 * FortDataSource is responsible for responsible for fetching data from a given REST API.
 */
class FortDataSourceImpl constructor(private val fortEndpoint: FortEndpoint) : FortDataSource {
    companion object {
        private val aesCipherManager = AESCipherManager()
        private val gson = Gson()
        private var rsaPublicKey: RSAPublicKey? = null
    }

    /**
     * first the api should gain Public key from server then it's
     * responsible for validate FortRequest
     * @param request SdkRequest
     * @return Observable<SdkResponse>
     */
    override fun validate(request: SdkRequest): Observable<SdkResponse> {


        val secretKeySpec = aesCipherManager.generateAESKey()
        return fortEndpoint.getSslCertificate().flatMap {
            val peerCertificates = it.raw().handshake?.peerCertificates
            rsaPublicKey = DataSecurityService.getPublicKey(peerCertificates)
            fortEndpoint.validateData(getEncryptedData(rsaPublicKey, request, secretKeySpec))
        }.map {
            val decryptMsg = aesCipherManager.decryptMsg(it, secretKeySpec)
            MapUtils.collectResponse(gson, decryptMsg, request.requestMap)
        }


    }
    /**
     * first the api should gain Public key from server then it's
     * responsible for validate Card Number
     * @param request SdkRequest
     * @return Observable<String>
     */
    override fun validateCardNumber(request: SdkRequest): Observable<String> {


        val secretKeySpec = aesCipherManager.generateAESKey()
        return if (rsaPublicKey == null) {
            fortEndpoint.getSslCertificate().flatMap {
                val peerCertificates = it.raw().handshake?.peerCertificates
                rsaPublicKey = DataSecurityService.getPublicKey(peerCertificates)
                fortEndpoint.validateData(getEncryptedData(rsaPublicKey, request, secretKeySpec))
            }.map {
                val decryptMsg = aesCipherManager.decryptMsg(it, secretKeySpec)
                decryptMsg
            }
        } else fortEndpoint.validateData(getEncryptedData(rsaPublicKey, request, secretKeySpec))
            .map {
                val decryptMsg = aesCipherManager.decryptMsg(it, secretKeySpec)
                decryptMsg
            }
    }

    /**
     * first the api should gain Public key from server then it's
     * responsible for precessing payment operation
     * @param request SdkRequest
     * @return Observable<SdkResponse>
     */
    override fun pay(request: SdkRequest): Observable<SdkResponse> {
        val secretKeySpec = aesCipherManager.generateAESKey()
        return if (rsaPublicKey == null) {
            fortEndpoint.getSslCertificate().flatMap {
                val peerCertificates = it.raw().handshake?.peerCertificates
                rsaPublicKey = DataSecurityService.getPublicKey(peerCertificates)
                fortEndpoint.processData(getEncryptedData(rsaPublicKey, request, secretKeySpec))
            }.map {
                val decryptMsg = aesCipherManager.decryptMsg(it, secretKeySpec)
                MapUtils.collectResponse(gson, decryptMsg, request.requestMap)
            }
        } else fortEndpoint.processData(getEncryptedData(rsaPublicKey, request, secretKeySpec))
            .map {
                val decryptMsg = aesCipherManager.decryptMsg(it, secretKeySpec)
                MapUtils.collectResponse(gson, decryptMsg, request.requestMap)
            }
    }

    /**
     * first the api should gain Public key from server then it's
     * responsible for log failures
     * @param request SdkRequest
     * @return Observable<SdkResponse>
     */
    override fun logData(request: SdkRequest): Observable<SdkResponse> {
        val secretKeySpec = aesCipherManager.generateAESKey()
        return if (rsaPublicKey == null) {
            fortEndpoint.getSslCertificate().flatMap {
                val peerCertificates = it.raw().handshake?.peerCertificates
                rsaPublicKey = DataSecurityService.getPublicKey(peerCertificates)
                fortEndpoint.logData(getEncryptedData(rsaPublicKey, request, secretKeySpec))
            }.map {
                val decryptMsg = aesCipherManager.decryptMsg(it, secretKeySpec)
                MapUtils.collectResponse(gson, decryptMsg, request.requestMap)
            }
        } else fortEndpoint.logData(getEncryptedData(rsaPublicKey, request, secretKeySpec))
            .map {
                val decryptMsg = aesCipherManager.decryptMsg(it, secretKeySpec)
                var collectResponse = MapUtils.collectResponse(gson, decryptMsg, request.requestMap)
                collectResponse

            }
    }

    /**
     * responsible for encrypt SdkRequest data with public key
     * @param publicKey RSAPublicKey?
     * @param body SdkRequest
     * @param secretKeySpec SecretKeySpec?
     * @return String
     */
    private fun getEncryptedData(
        publicKey: RSAPublicKey?,
        body: SdkRequest,
        secretKeySpec: SecretKeySpec?
    ): String {
        return DataSecurityService.encryptRequestData(
            gson.toJson(body, SdkRequest::class.java),
            publicKey,
            secretKeySpec
        )
    }


}
