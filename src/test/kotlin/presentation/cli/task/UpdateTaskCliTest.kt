package presentation.cli.task

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.NoTasksFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.task.GetTasksUseCase
import domain.usecases.task.UpdateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.createDummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController

class UpdateTaskCliTest {
    private val getAllTasksUseCase:GetTasksUseCase= mockk(relaxed = true)
    private val updateTaskUseCase: UpdateTaskUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private val getAllProjectsUseCase:GetAllProjectsUseCase= mockk(relaxed = true)
    private val getAllTaskStatesUseCase:GetAllTaskStatesUseCase= mockk(relaxed = true)
    private lateinit var updateTaskCli: UpdateTaskCli

    @BeforeEach
    fun setup() {
        updateTaskCli= UpdateTaskCli(getAllTasksUseCase,updateTaskUseCase, uiController,getAllProjectsUseCase,getAllTaskStatesUseCase)
    }

    @Test
    fun `should throw exception if no tasks`() {
        val exception = assertThrows<NoTasksFoundException> {
            updateTaskCli.update()
        }
        assertThat(exception.message).isEqualTo("No tasks found.")
    }

    @Test
    fun `should throw exception if user input is null`() {
        every { getAllTasksUseCase.getTasks() } returns listOf(createDummyTasks())
        every { uiController.readInput() } returns ""

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateTaskCli.update()
        }
        assertThat(exception.message).isEqualTo("ID can't be empty")
    }

    @Test
    fun `should throw exception if user input Invalid value`() {
        every { getAllTasksUseCase.getTasks() } returns listOf(createDummyTasks())
        every { uiController.readInput() } returns "a"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateTaskCli.update()
        }
        assertThat(exception.message).isEqualTo("Should enter valid value")
    }

    @Test
    fun `should throw exception if user input title is empty`() {
        every { getAllTasksUseCase.getTasks() } returns listOf(createDummyTasks())
        every { uiController.readInput() } returnsMany listOf("1", "")

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateTaskCli.update()
        }
        assertThat(exception.message).isEqualTo("New title can't be empty")
    }

    @Test
    fun `should throw exception if user input description is empty`() {
        every { getAllTasksUseCase.getTasks() } returns listOf(createDummyTasks())
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "")

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateTaskCli.update()
        }
        assertThat(exception.message).isEqualTo("New description can't be empty")
    }

    @Test
    fun `should throw exception if user input is zero`() {
        every { getAllTasksUseCase.getTasks() } returns listOf(createDummyTasks(), createDummyTasks())
        every { uiController.readInput() } returns "0"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateTaskCli.update()
        }
        assertThat(exception.message).isEqualTo("should enter found id")
    }

    @Test
    fun `should throw exception if user input greater than number of tasks`() {
        every { getAllTasksUseCase.getTasks() } returns listOf(createDummyTasks(), createDummyTasks())
        every { uiController.readInput() } returns "3"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateTaskCli.update()
        }
        assertThat(exception.message).isEqualTo("should enter found id")
    }

    @Test
    fun `should call execute function in update task use case when I call update function and success to create task`() {
        every { getAllTasksUseCase.getTasks() } returns listOf(createDummyTasks())
        every { uiController.readInput() } returns "1"

        updateTaskCli.update()

        verify { getAllTasksUseCase.getTasks() }
    }

    @Test
    fun `should call execute function in update task use case when I call update function and failed to create task`() {
        every { getAllTasksUseCase.getTasks() } returns listOf(createDummyTasks())
        every { uiController.readInput() } returns "1"
        every { updateTaskUseCase.updateTask(any(),any(),any(),any(),
            any(),any(),any(),any(),any()) } returns false

        updateTaskCli.update()

        verify { updateTaskUseCase.updateTask(any(),any(),any(),any(),
            any(),any(),any(),any(),any()) }
    }
}