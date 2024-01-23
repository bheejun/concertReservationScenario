package com.example.concert.util

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class StringListToStringConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        return attribute?.joinToString(",") ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return dbData?.split(",")?.filter { it.isNotEmpty() } ?: listOf()
    }
}