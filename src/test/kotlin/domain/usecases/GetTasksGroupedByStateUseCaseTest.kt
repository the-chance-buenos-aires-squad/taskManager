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

    val todoState = createDummyTaskState(id = "state1", projectId = projectId.toString(), name = "To Do")
    val inProgressState = createDummyTaskState(id = "state2", projectId = projectId.toString(), name = "In Progress")
    val otherState = createDummyTaskState(id = "state3", projectId = UUID.randomUUID().toString(), name = "Other")
    val projectState = createDummyTaskState(id = "state1", projectId = projectId.toString(), name = "To Do")
    val otherProjectState = createDummyTaskState(id = "state2", projectId = UUID.randomUUID().toString(), name = "In Progress")

    val task1 = createDummyTask( projectId = projectId, stateId = "state1", title = "Task 1")
    val task2 = createDummyTask( projectId = projectId, stateId = "state1", title = "Task 2")
    val task3 = createDummyTask( projectId = projectId, stateId = "state2", title = "Task 3")
    val task4 = createDummyTask( projectId = UUID.randomUUID(), stateId = "state2", title = "Task 4")
    val taskOtherProject = createDummyTask( projectId = UUID.randomUUID(), stateId = "state3", title = "Task 4")
    val taskOtherState = createDummyTask( projectId = projectId, stateId = "state3", title = "Task 5")

    @BeforeEach
    fun setup() {
        getTasksGroupedByStateUseCase = GetTasksGroupedByStateUseCase(getTasksUseCase, getTaskStatesUseCase)
    }

    @Test
    fun `getTasksGroupedByState should return list of states when state have task in specific project`() {
        // Given
        every { getTaskStatesUseCase.execute() } returns listOf(todoState, inProgressState, otherState)
        every { getTasksUseCase.getTasks() } returns listOf(task1, task2, task3, taskOtherProject, taskOtherState)

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        assertThat(result).hasSize(2)

    }

    @Test
    fun `getTasksGroupedByState should group tasks by their state correctly`() {
        // Given
        every { getTaskStatesUseCase.execute() } returns listOf(todoState, inProgressState, otherState)
        every { getTasksUseCase.getTasks() } returns listOf(task1, task2, task3, taskOtherProject, taskOtherState)

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        val stateWithTasks1 = result.find { it.state.id == "state1" }
        assertThat(stateWithTasks1?.tasks).containsExactly(task1, task2)

        val stateWithTasks2 = result.find { it.state.id == "state2" }
        assertThat(stateWithTasks2?.tasks).containsExactly(task3)
    }

    @Test
    fun `getTasksGroupedByState returns states with empty tasks when no tasks exist`() {
        // Given
        every { getTaskStatesUseCase.execute() } returns listOf(todoState, inProgressState)
        every { getTasksUseCase.getTasks() } returns emptyList()

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        assertThat(result[0].tasks).isEmpty()
    }

    @Test
    fun `getTasksGroupedByState returns empty list when there are no states for the project`() {
        // Given
        every { getTaskStatesUseCase.execute() } returns listOf(otherState)
        every { getTasksUseCase.getTasks() } returns listOf(
            createDummyTask( projectId = projectId, stateId = "state3", title = "Task 1")
        )

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTasksGroupedByState filters out tasks and states from other projects`() {
        // Given
        every { getTaskStatesUseCase.execute() } returns listOf(projectState, otherProjectState)
        every { getTasksUseCase.getTasks() } returns listOf(task1, task4)

        // When
        val result = getTasksGroupedByStateUseCase.getTasksGroupedByState(dummyProject)

        // Then
        assertThat(result[0].state).isEqualTo(projectState)
    }
}