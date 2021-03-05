import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface DispatcherProvider {
    fun io(): CoroutineContext
    fun main(): CoroutineContext
    fun default(): CoroutineContext
}

class DispatcherProviderImpl : DispatcherProvider {
    override fun io(): CoroutineContext = Dispatchers.IO
    override fun main(): CoroutineContext = Dispatchers.Main
    override fun default(): CoroutineContext = Dispatchers.Default
}

class TestDispatcherProviderImpl(
    private val context: CoroutineContext
) :
    DispatcherProvider {
    override fun io(): CoroutineContext = context
    override fun main(): CoroutineContext = context
    override fun default(): CoroutineContext = context
}