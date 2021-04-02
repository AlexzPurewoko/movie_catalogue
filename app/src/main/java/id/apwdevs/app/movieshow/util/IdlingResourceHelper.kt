package id.apwdevs.app.movieshow.util

/**
 * This class is for provide a blueprint for idling resource
 * that be gona used in instrumentation testing.
 * Keep the class empty for release variant, and provide the
 * implementation in debug variant
 */
open class IdlingResourceHelper {
    open fun increment() {}
    open fun decrement() {}
}