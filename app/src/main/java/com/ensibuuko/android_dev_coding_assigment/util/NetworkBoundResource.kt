package com.ensibuuko.android_dev_coding_assigment.util

import kotlinx.coroutines.flow.*


inline fun <ResultType, RequestType>networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch : suspend () -> RequestType,
    crossinline saveFetchResult : suspend (RequestType) -> Unit,
    crossinline shouldFetch : (ResultType) -> Boolean = { true}
) = flow {
    //collecting from a flow once
    val data = query().first()
    val flow = if (shouldFetch(data)){
        emit(Resource.Loading(data))
        try {
            //fetch Data from api and save to room db
            saveFetchResult(fetch())
            //fetch data from room and notify Resource
            query().map { Resource.Success(it) }
        }catch (throwable : Throwable){
            //Incase of error
            query().map { Resource.Error(throwable, it) }
        }

    }else{
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}