package com.ebay.dozhao.myweatherapp.event

class SearchDoneEvent {
    enum class ErrorMessageDetail {
        NO_RESPONSE,
        NO_RESPONSE_BODY,
        RESPONSE_NOT_SUCCESSFUL,
        NETWORK_EXCEPTION;

        override fun toString(): String {
            return when (this) {
                NO_RESPONSE -> "Error: No response from server"
                NO_RESPONSE_BODY -> "Error: No weather info in response"
                RESPONSE_NOT_SUCCESSFUL -> "Fail to get response: "
                NETWORK_EXCEPTION -> "Network exception"
            }
        }
    }

    var errorMessage: String = ""
}