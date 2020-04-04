package com.chargealarm.constant

object AppConstants {

    object UiValidationConstants {
        const val EMAIL_EMPTY = 101
        const val PASSWORD_EMPTY = 102
        const val INVALID_PASSWORD = 103
        const val INVALID_EMAIL = 104
        const val NAME_EMPTY = 105
        const val INVALID_NAME = 106
        const val PHONE_EMPTY = 107
        const val INVALID_PHONE = 108
    }

    object NetworkingConstants {
        const val EMPTY_DATA_ERROR_CODE = 451
        const val INTERNAL_SERVER_ERROR_CODE = 500
        const val NO_INTERNET_CONNECTION = 9
        const val ACCOUNT_BLOCKED_CODE = 403
        const val UNAUTHORIZED = 401
    }

    object PermissionConstants {
        const val REQ_CODE_GALLERY = 100
        const val REQ_CODE_CAMERA = 101
        const val MESSAGE_CODE_CAMERA = 102
        const val MESSAGE_CODE_STORAGE = 103
        const val MESSAGE_CODE_CAMERA_STORAGE = 104
    }

    object PreferenceConstants {
        const val REFRESH_TOKEN = "refresh_token"
        const val ACCESS_TOKEN = "access_token"
        const val USER_DETAILS = "user_detail"
        const val DEVICE_TOKEN = "device_token"
        const val DEVICE_ID = "device_id"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
        const val BATTERY_PERCENTAGE_FOR_ALARM = "alarm"
    }

    object DateTimeConstants {
        const val DATE = "yyyy-MM-dd"
        const val DATE_FULL = "EEEE, d MMMM yyyy"
        const val DATE_SIMPLE = "d MMMM yyyy"
        const val WEEKDAY = "EEEE"
        const val DATE_TIME = "yyyy-MM-dd HH:mm:ss"
        const val TWEL_HOUR_FORMAT_TIME = "hh:mm:ss"
        const val TWENTY_FOUR_HOUR_FORMAT_TIME = "HH:mm:ss"
        const val TWENTY_FOUR_HOUR_FORMAT_TIME_AM = "hh:mm a"
        const val HOUR_FORMAT_TIME = "hh:mm:ss"
        const val HOUR_MINUTE_TIME = "hh:mm"
        const val SERVER_TO = "dd-MM-yyyy"
        const val AM_PM = "a"
        const val DATE_FORMAT = "dd-MM-yyyy"
    }

    object SplashConstants {
        const val OPEN_LOGIN_SCREEN = "open_login_screen"
        const val OPEN_HOME_SCREEN = "open_home_screen"
    }

    object Platform {
        const val PLATFORM_IOS = "1"
        const val PLATFORM_ANDROID = "2"
    }
}