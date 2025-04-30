package di


import org.koin.core.qualifier.named
import org.koin.dsl.module
import presentation.Cli.Navigator
import presentation.Cli.Routes
import presentation.Cli.Screen
import presentation.Cli.UiController
import presentation.Cli.demoScreens.DemoScreen1
import presentation.Cli.demoScreens.DemoScreen2

val presentationModel = module {
    single { UiController() }

    single<Screen>(qualifier = named(Routes.DEMO_SCREEN1_ROUTE)) {
        DemoScreen1(get(), get())
    }

    single<Screen>(qualifier = named(Routes.DEMO_SCREEN2_ROUTE)) {
        DemoScreen2(get(), get())
    }

    single {
        // Inject a lambda to resolve screens by route
        Navigator { route ->
            try {
                getKoin().get<Screen>(qualifier = named(route))
            } catch (e: Exception) {
                DemoScreen1(get(), get())
            }
        }
    }
}