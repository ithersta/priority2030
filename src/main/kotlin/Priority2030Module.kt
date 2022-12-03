import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import services.Morpher

val priority2030Module = module(createdAtStart = true) {
    includes(defaultModule)
    val mainConfig = readMainProperties()
    single { mainConfig }
    single { mainConfig.emailSecrets }
    single { Morpher(mainConfig.morpherToken) }
}
