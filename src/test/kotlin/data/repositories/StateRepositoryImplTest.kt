import data.dataSource.StateCSVDataSource
import data.repositories.StateRepositoryImpl
import io.mockk.*
import org.junit.jupiter.api.*

class StateRepositoryImplTest {

    private val mockCSVDataSource = mockk<StateCSVDataSource>(relaxed = true)
    private lateinit var stateRepository: StateRepositoryImpl

    @BeforeEach
    fun setUp() {
        stateRepository = StateRepositoryImpl(mockCSVDataSource)
    }

    // TODO: Write below all test for createState fun. (write here shahed)


    // TODO: Write below all tests for editState fun.


    // TODO: Write below all tests for deleteState fun.

}