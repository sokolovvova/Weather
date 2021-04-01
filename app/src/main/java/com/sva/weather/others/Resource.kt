package com.sva.weather.others

data class Resource<out T>(val status: Status,val data: T?,val message: String?) {

    companion object{
        fun<T> success(data:T):Resource<T> = Resource(status= Status.SUCCESS,data = data,message = null)
        fun<T> successWithErrors(data:T, message: String?):Resource<T> = Resource(status= Status.SUCCESS_WITH_ERRORS,data = data,message = message)
        fun<T> error(message: String?):Resource<T> = Resource(status= Status.ERROR,data = null,message = message)
        fun<T> failed(message: String?):Resource<T> = Resource(status= Status.FAILED,data = null,message = message)
    }
}