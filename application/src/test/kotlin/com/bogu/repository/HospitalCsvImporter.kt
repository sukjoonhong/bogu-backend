package com.bogu.repository

import com.bogu.annotation.LocalBootTest
import com.bogu.domain.entity.postgresql.Hospital
import com.bogu.repo.postgresql.HospitalRepository
import io.kotest.core.spec.style.StringSpec
import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@LocalBootTest
class HospitalCsvImporter(
    private val hospitalRepository: HospitalRepository
): StringSpec({

    "local db 에 import" {
        val resource = ClassPathResource("hospital.tsv")
        val inputStream = resource.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))

        // 첫 줄은 헤더니까 건너뜀
        reader.readLine()

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        reader.lineSequence().forEachIndexed { index, line ->
            val tokens = line.split("\t")
            if (tokens.size < 16) return@forEachIndexed

            val hospital = Hospital(
                encryptedCode = tokens[0],
                name = tokens[1],
                typeCode = tokens[2],
                typeName = tokens[3],
                sidoCode = tokens[4],
                sidoName = tokens[5],
                sigunguCode = tokens[6],
                sigunguName = tokens[7],
                eupmyeondong = tokens[8],
                postalCode = tokens[9],
                address = tokens[10],
                phone = tokens[11],
                homepage = tokens[12],
                establishedAt = tokens[13].takeIf { it.isNotBlank() }?.let { LocalDate.parse(it, formatter) },
                longitude = tokens[14].toDoubleOrNull(),
                latitude = tokens[15].toDoubleOrNull(),
            )

            hospitalRepository.save(hospital)
        }
    }
})