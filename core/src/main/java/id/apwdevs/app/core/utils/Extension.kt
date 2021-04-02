package id.apwdevs.app.core.utils

import java.security.InvalidParameterException
import kotlin.jvm.internal.Reflection
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

inline fun <reified T> Any.getFieldValue(fieldName: String): T {
    this::class.memberProperties.forEach { kCallable ->
        if (fieldName == kCallable.name) {
            return kCallable.getter.call(this) as T
        }
    }
    throw RuntimeException("Something happen with field, please check it again!")
}

inline fun <reified T: Any> Any.plainMap(classf: Class<T>): T? {
    val classOf = Reflection.createKotlinClass(classf)

    if(!classOf.isData)
        throw Exception("Please provide a valid class data")
    val receiverValue = mutableListOf<Any?>()
    val holdedFields = this::class.primaryConstructor?.parameters!!
    classOf.primaryConstructor?.parameters?.forEach {
        val field = holdedFields.find { pred -> pred.name == it.name }
        when {
            field != null -> {
                // check types
                if(field.type != it.type)
                    throw Exception("Field ${it.name} type is not match with the subject")
                receiverValue.add(this.getFieldValue(it.name!!))
            }
            it.type.isMarkedNullable -> {
                receiverValue.add(null)
            }
            else -> {
                throw InvalidParameterException("Cannot find parameter ${it.name} in resulted class")
            }
        }
    }
    val argument = receiverValue.toTypedArray()
    return classf.constructors[0].newInstance(*argument) as T?
}