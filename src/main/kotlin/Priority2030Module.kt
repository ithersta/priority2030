import email.readEmailSecrets
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

val priority2030Module = module(createdAtStart = true) {
    includes(defaultModule)
    single { readPropertiesFile() }
    single { readEmailSecrets(get()) }
}