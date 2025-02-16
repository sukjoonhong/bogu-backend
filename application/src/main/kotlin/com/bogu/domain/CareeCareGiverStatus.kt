package com.bogu.domain


enum class CareeCareGiverStatus {
    REQUESTED,
    CANCELED,
    MATCHED,
    CONTRACTED;

    companion object {
        private fun getInitialVisibleStatuses(): List<CareeCareGiverStatus> {
            return listOf(REQUESTED)
        }

        fun initialVisibleStatuses(): List<String> {
            return getInitialVisibleStatuses().map { it.name }
        }
    }
}

