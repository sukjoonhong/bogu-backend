package com.bogu.service

import com.bogu.domain.entity.postgresql.Hospital
import com.bogu.repo.postgresql.HospitalRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class HospitalAutoCompletionService(
    private val hospitalRepository: HospitalRepository,
) {
    val hospitalIdByNameMap = mutableMapOf<String, Long>()
    val allNames = mutableListOf<String>()

    @PostConstruct
    fun init() {
        val hospitalIdByName = hospitalRepository.findAllNames().associate { it to it.split(":").first().toLong() }
        hospitalIdByNameMap.putAll(hospitalIdByName)
        allNames.addAll(hospitalIdByName.keys)
    }

    fun searchHospitalsWithLevenshtein(query: String): List<Hospital> {
        val lowerQuery = query.lowercase()

        val matched = allNames
            .map { name ->
                val distance = levenshtein(lowerQuery, name.lowercase())
                name to distance
            }
            .sortedBy { it.second } // 거리 기준 정렬
            .map { it.first }
            .take(5)

        return hospitalRepository.findAllById(matched.map { hospitalIdByNameMap[it] })
    }

    fun levenshtein(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }

        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j

        for (i in 1..a.length) {
            for (j in 1..b.length) {
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,     // 삭제
                    dp[i][j - 1] + 1,     // 삽입
                    dp[i - 1][j - 1] + if (a[i - 1] == b[j - 1]) 0 else 1  // 교체
                )
            }
        }

        return dp[a.length][b.length]
    }
}