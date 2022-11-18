import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val priority2030Module = module(createdAtStart = true) {
    includes(defaultModule)
    val mainConfig = readMainProperties()
    single { mainConfig }
    single { mainConfig.emailSecrets }
}
