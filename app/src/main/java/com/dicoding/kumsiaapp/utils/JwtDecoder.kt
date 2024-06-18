package com.dicoding.kumsiaapp.utils

import com.auth0.android.jwt.JWT


object JwtDecoder {
    fun decode(token: String): JWT {
        val jwt = JWT(token)

        for (item in jwt.claims.keys) {
            println(item)
            println(jwt.getClaim(item).asString())
        }

        return jwt
    }
}