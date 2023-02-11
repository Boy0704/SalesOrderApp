package com.apps.salesorder.data.model


data class SoSync(
    var so_header: List<SoHeader>,
    var so_detail: List<SoDetail>
)
