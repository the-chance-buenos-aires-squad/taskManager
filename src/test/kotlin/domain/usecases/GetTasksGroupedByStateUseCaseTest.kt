package domain.usecases

import domain.entities.TaskState
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.createDummyTaskForStates
import dummyData.dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test

class GetTasksGroupedByStateUseCaseTest {
    private val getTasksUseCase: GetTasksUseCase = mockk()
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase = mockk()
    private lateinit var getTasksGroupedByStateUseCase: GetTasksGroupedByStateUseCase

    val todoState = DummyTaskState.todo
    val inProgressState = DummyTaskState.inProgress
    val doneState = DummyTaskState.done

    val states = listOf(
        todoState,
        inProgressState,
        doneState
    )

    @BeforeEach
    fun setup() {
        getTasksGroupedByStateUseCase = GetTasksGroupedByStateUseCase(getTasksUseCase, getAllTaskStatesUseCase)
    }

    @Test
    fun `getTasksGroupedByState should group tasks by state when multiple states exist`() {
        // Given

        val task1 = createDummyTaskForStates(stateId = todoState.id)
        val task2 = createDummyTaskForStates(stateId = inProgressState.id)
        val task3 = createDummyTaskForStates(stateId = todoState.id)

        every { getAllTaskStatesUseCase.execute() } returns listOf(todoState, inProgressState)
        every { getTasksUseCase.getTasks() } returns listOf(task1, task2, task3)

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState()

        // Then
        assertEquals(2, result.size)

        val state1Group = result.find { it.state.id == todoState.id }
        assertEquals(2, state1Group?.tasks?.size)
        assertTrue(state1Group?.tasks?.containsAll(listOf(task1, task3)) ?: false)

        val state2Group = result.find { it.state.id == inProgressState.id }
        assertEquals(1, state2Group?.tasks?.size)
        assertEquals(task2, state2Group?.tasks?.first())
    }

    @Test
    fun `getTasksGroupedByState should include empty states when no tasks exist`() {
        // Given
        val emptyState = TaskState("3", "Done", UUID.randomUUID().toString())
        every { getAllTaskStatesUseCase.execute() } returns listOf(emptyState)
        every { getTasksUseCase.getTasks() } returns emptyList()

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState()

        // Then
        assertEquals(1, result.size)
        assertEquals(emptyState, result.first().state)
        assertTrue(result.first().tasks.isEmpty())
    }

    @Test
    fun `getTasksGroupedByState should maintain state order from repository`() {
        // Given

        every { getAllTaskStatesUseCase.execute() } returns states
        every { getTasksUseCase.getTasks() } returns emptyList()

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState()

        // Then
        assertEquals(3, result.size)
        assertEquals(listOf("To Do", "In Progress","Done"), result.map { it.state.name })
    }

    @Test
    fun `getTasksGroupedByState should handle tasks with non-existing states`() {
        // Given
        val validState = TaskState("5", "Valid", UUID.randomUUID().toString())
        val taskWithInvalidState = createDummyTaskForStates(stateId = "5")

        every { getAllTaskStatesUseCase.execute() } returns listOf(validState)
        every { getTasksUseCase.getTasks() } returns listOf(taskWithInvalidState)

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState()

        // Then
        assertEquals(1, result.size)
    }


}
