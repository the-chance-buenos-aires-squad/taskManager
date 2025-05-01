import com.google.common.truth.Truth.assertThat
import data.dataSource.StateCSVDataSource
import data.repositories.StateRepositoryImpl
import domain.entities.State
import dummyStateData.DummyState
import io.mockk.*
import junit.framework.TestCase.*
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

    @Test
    fun `should edit state successfully when this state is existing`() {
        val todoState = DummyState.todo
        val updatedToDoState = State("1", "In Progress", "P001")

        every { mockCSVDataSource.getAllStates() } returns listOf(todoState)
        every { mockCSVDataSource.editState(updatedToDoState) } returns true

        val result = stateRepository.editState(updatedToDoState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when editing non existent state`() {
        val nonExistentState =  State("99", "Non-existent State", "P12")
        every { mockCSVDataSource.getAllStates() } returns emptyList()

        val result = stateRepository.editState(nonExistentState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should not update state when projectId does not match`() {
        val todoState = DummyState.done
        val updatedDoState =  State("3", "In Progress", "P012")

        every { mockCSVDataSource.getAllStates() } returns listOf(todoState)

        val result = stateRepository.editState(updatedDoState)

        assertThat(result).isFalse()
    }


    // TODO: Write below all tests for deleteState fun.

}