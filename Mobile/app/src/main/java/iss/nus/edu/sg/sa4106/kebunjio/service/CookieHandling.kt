package iss.nus.edu.sg.sa4106.kebunjio.service

import java.net.HttpURLConnection

class CookieHandling {
    companion object {
        fun extractSessionCookie(connection: HttpURLConnection): String {
            val headers: MutableMap<String, MutableList<String>>? = connection.headerFields
            val cookies: MutableList<String>? = headers!!["Set-Cookie"]
            var sessionCookie: String = ""

            if (cookies != null) {
                for (cookie in cookies) {
                    if (cookie.startsWith("JSESSIONID")) {
                        sessionCookie = cookie.split(";")[0]
                    }
                }
            }
            return sessionCookie
        }

        fun setSessionCookie(connection: HttpURLConnection, sessionCookie: String?): Boolean {
            if (sessionCookie==null||sessionCookie=="") {
                return false
            }
            connection.setRequestProperty("Cookie",sessionCookie)
            return true
        }
    }
}