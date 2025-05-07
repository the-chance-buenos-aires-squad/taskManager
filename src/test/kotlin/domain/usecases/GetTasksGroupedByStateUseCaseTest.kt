package domain.usecases

import com.google.common.truth.Truth.assertThat
import createDummyTaskState
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.createDummyProject
import dummyData.createDummyTask
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class GetTasksGroupedByStateUseCaseTest {

    private lateinit var getTasksGroupedByStateUseCase: GetTasksGroupedByStateUseCase
    private val getTasksUseCase: GetTasksUseCase = mockk()
    private val getTaskStatesUseCase: GetAllTaskStatesUseCase = mockk(relaxed =true )

    private val projectId = UUID.randomUUID()
    private val dummyProject = createDummyProject(id = projectId, name = "Test Project")

    val todoState = createDummyTaskState(id = UUID.fromString("00000000-1000-0000-0000-000000000000"), projectId = projectId, name = "To Do")
    val inProgressState = createDummyTaskState(id = UUID.fromString("00000000-2000-0000-0000-000000000000"), projectId = projectId, name = "In Progress")
    val otherState = createDummyTaskState(id = UUID.fromString("00000000-3000-0000-0000-000000000000"), projectId = UUID.randomUUID(), name = "Other")
    val projectState = createDummyTaskState(id = UUID.fromString("00000000-1000-0000-0000-000000000000"), projectId = projectId, name = "To Do")
    val otherProjectState = createDummyTaskState(id = UUID.fromString("00000000-2000-0000-0000-000000000000"), projectId = UUID.randomUUID(), name = "In Progress")

    val task1 = createDummyTask( projectId = projectId, stateId = UUID.fromString("00000000-1000-0000-0000-000000000000"), title = "Task 1")
    val task2 = createDummyTask( projectId = projectId, stateId = UUID.fromString("00000000-1000-0000-0000-000000000000"), title = "Task 2")
    val task3 = createDummyTask( projectId = projectId, stateId = UUID.fromString("00000000-2000-0000-0000-000000000000"), title = "Task 3")
    val task4 = createDummyTask( projectId = UUID.randomUUID(), stateId = UUID.fromString("00000000-2000-0000-0000-000000000000"), title = "Task 4")
    val taskOtherProject = createDummyTask( projectId = UUID.randomUUID(), stateId = UUID.fromString("00000000-3000-0000-0000-000000000000"), title = "Task 4")
    val taskOtherState = createDummyTask( projectId = projectId, stateId = UUID.fromString("00000000-3000-0000-0000-000000000000"), title = "Task 5")

    @BeforeEach
    fun setup() {
        getTasksGroupedByStateUseCase = GetTasksGroupedByStateUseCase(getTasksUseCase, getTaskStatesUseCase)
    }

    @Test
    fun `getTasksGroupedByState should return list of states when state have task in specific project`() {
        // Given
        every { getTaskStatesUseCase.execute(any()) } returns listOf(todoState, inProgressState, otherState)
        every { getTasksUseCase.getTasks() } returns listOf(task1, task2, task3, taskOtherProject, taskOtherState)

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        assertThat(result).hasSize(2)

    }

    @Test
    fun `getTasksGroupedByState should group tasks by their state correctly`() {
        // Given
        every { getTaskStatesUseCase.execute(any()) } returns listOf(todoState, inProgressState, otherState)
        every { getTasksUseCase.getTasks() } returns listOf(task1, task2, task3, taskOtherProject, taskOtherState)

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        val stateWithTasks1 = result.find { it.state.id == UUID.fromString("00000000-1000-0000-0000-000000000000") }
        assertThat(stateWithTasks1?.tasks).containsExactly(task1, task2)

        val stateWithTasks2 = result.find { it.state.id == UUID.fromString("00000000-2000-0000-0000-000000000000") }
        assertThat(stateWithTasks2?.tasks).containsExactly(task3)
    }

    @Test
    fun `getTasksGroupedByState returns states with empty tasks when no tasks exist`() {
        // Given
        every { getTaskStatesUseCase.execute(any()) } returns listOf(todoState, inProgressState)
        every { getTasksUseCase.getTasks() } returns emptyList()

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        assertThat(result[0].tasks).isEmpty()
    }

    @Test
    fun `getTasksGroupedByState returns empty list when there are no states for the project`() {
        // Given
        every { getTaskStatesUseCase.execute(any()) } returns listOf(otherState)
        every { getTasksUseCase.getTasks() } returns listOf(
            createDummyTask( projectId = projectId, stateId = UUID.fromString("00000000-3000-0000-0000-000000000000"), title = "Task 1")
        )

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTasksGroupedByState filters out tasks and states from other projects`() {
        // Given
        every { getTaskStatesUseCase.execute(any()) } returns listOf(projectState, otherProjectState)
        every { getTasksUseCase.getTasks() } returns listOf(task1, task4)

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        assertThat(result[0].state).isEqualTo(projectState)
    }
}