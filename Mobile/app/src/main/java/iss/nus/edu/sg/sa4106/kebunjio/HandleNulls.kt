package iss.nus.edu.sg.sa4106.kebunjio

class HandleNulls {

    companion object {
        fun ifNullString(toCheck: String?,elseVal: String = ""): String {
            if (toCheck == null) {
                return elseVal
            }
            return toCheck
        }

        fun ifNullBoolean(toCheck: Boolean?, elseVal: Boolean = false): Boolean {
            if (toCheck == null) {
                return elseVal
            }
            return toCheck
        }
    }

}