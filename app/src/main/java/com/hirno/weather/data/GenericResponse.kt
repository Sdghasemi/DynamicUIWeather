package com.hirno.weather.data

import com.hirno.weather.network.response.NetworkResponse

/**
 * A typealias used for using generic [ErrorDTO] as the ErrorModel type of [NetworkResponse]
 */
typealias GenericResponse<S> = NetworkResponse<S, ErrorDTO>