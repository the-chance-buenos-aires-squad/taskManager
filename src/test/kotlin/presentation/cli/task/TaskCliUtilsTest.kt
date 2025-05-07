package presentation.cli.task

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import domain.repositories.UserRepository
import dummyData.DummyUser
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.LocalDateTime
import java.util.*

class TaskCliUtilsTest {

    private val task = DummyTasks.validTask.copy(
        title = "Old Title",
        description = "Old Description",
        assignedTo = null,
        updatedAt = LocalDateTime.now()
    )

    private val dummyUser = DummyUser.dummyUserOne
    private val dummyStates = listOf(
        DummyTaskState.todo,
        DummyTaskState.inProgress,
        DummyTaskState.done
    )

    private lateinit var userRepository: UserRepository
    private lateinit var uiController: UiController

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        uiController = mockk(relaxed = true)
    }

    @Test
    fun `fetchProjectTasks should return tasks matching project ID`() = runTest {
        val projectId = UUID.randomUUID()
        val task1 = DummyTasks.validTask.copy(title = "Task One", projectId = projectId)
        val task2 = DummyTasks.validTask.copy(title = "Task Two", projectId = projectId)

        val output = ByteArrayOutputStream()
        val uiController = UiController(PrintStream(output), Scanner(System.`in`))

        val tasks = listOf(task1, task2)
        val result = TaskCliUtils.fetchProjectTasks(tasks, projectId, uiController)

        assertThat(result).hasSize(2)
        assertThat(output.toString()).contains("1 - Task One")
        assertThat(output.toString()).contains("2 - Task Two")
    }

    @Test
    fun `fetchProjectTasks should return empty list and print message when no tasks match`() = runTest {
        val projectId = UUID.randomUUID()

        val output = ByteArrayOutputStream()
        val uiController = UiController(PrintStream(output), Scanner(System.`in`))

        val result = TaskCliUtils.fetchProjectTasks(emptyList(), projectId, uiController)

        assertThat(result).isEmpty()
        assertThat(output.toString()).contains("No tasks found for this project.")
    }

    @Test
    fun `selectTask should return selected task when valid input is given`() = runTest {
        val input = ByteArrayInputStream("1\n".toByteArray())
        val output = ByteArrayOutputStream()
        val uiController = UiController(PrintStream(output), Scanner(input))

        val task1 = DummyTasks.validTask.copy(title = "Task One")
        val task2 = DummyTasks.validTask.copy(title = "Task Two")

        val result = TaskCliUtils.selectTask(listOf(task1, task2), uiController)

        assertThat(result).isEqualTo(task1)
    }

    @Test
    fun `selectTask should return null after two invalid attempts`() = runTest {
        val input = ByteArrayInputStream("5\n0\n".toByteArray())
        val output = ByteArrayOutputStream()
        val uiController = UiController(PrintStream(output), Scanner(input))

        val task1 = DummyTasks.validTask.copy(title = "Task One")
        val task2 = DummyTasks.validTask.copy(title = "Task Two")

        val result = TaskCliUtils.selectTask(listOf(task1, task2), uiController)

        assertThat(result).isNull()
        assertThat(output.toString()).contains("Invalid input. Returning to dashboard.")
    }

    @Test
    fun `promptForTaskIndex should return correct index`() = runTest {
        val input = ByteArrayInputStream("2\n".toByteArray())
        val output = ByteArrayOutputStream()
        val uiController = UiController(PrintStream(output), Scanner(input))

        val index = TaskCliUtils.promptForTaskIndex(2, uiController)

        assertThat(index).isEqualTo(1)
    }

    @Test
    fun `promptForTaskIndex should return null after invalid inputs`() = runTest {
        val input = ByteArrayInputStream("abc\n9\n".toByteArray())
        val output = ByteArrayOutputStream()
        val uiController = UiController(PrintStream(output), Scanner(input))

        val index = TaskCliUtils.promptForTaskIndex(2, uiController)

        assertThat(index).isNull()
        assertThat(output.toString()).contains("Invalid input. Returning to dashboard.")
    }

    @Test
    fun `promptForUpdatedTask should update all fields when new inputs are provided`() = runTest {
        coEvery { uiController.readInput() } returnsMany listOf(
            "New Title",       // title
            "New Description", // description
            "2",               // state index
            dummyUser.username // username
        )
        coEvery { userRepository.getUserByUserName(dummyUser.username) } returns dummyUser

        val updatedTask = TaskCliUtils.promptForUpdatedTask(
            task, dummyStates, userRepository, uiController
        )

        assertThat(updatedTask.title).isEqualTo("New Title")
        assertThat(updatedTask.description).isEqualTo("New Description")
        assertThat(updatedTask.stateId).isEqualTo(dummyStates[1].id)
        assertThat(updatedTask.assignedTo).isEqualTo(dummyUser.id)
        assertThat(updatedTask.updatedAt).isNotEqualTo(task.updatedAt)
    }

    @Test
    fun `promptForUpdatedTask should keep current values when inputs are empty or invalid`() = runTest {
        coEvery { uiController.readInput() } returnsMany listOf(
            "",   // title → keep old
            "",   // description → keep old
            "9",  // invalid state index → keep old
            "unknownUser" // unknown user → keep old
        )
        coEvery { userRepository.getUserByUserName("unknownUser") } returns null

        val updatedTask = TaskCliUtils.promptForUpdatedTask(
            task.copy(stateId = dummyStates[0].id), dummyStates, userRepository, uiController
        )

        assertThat(updatedTask.title).isEqualTo(task.title)
        assertThat(updatedTask.description).isEqualTo(task.description)
        assertThat(updatedTask.stateId).isEqualTo(dummyStates[0].id)
        assertThat(updatedTask.assignedTo).isEqualTo(null)
        assertThat(updatedTask.updatedAt).isNotEqualTo(task.updatedAt)
    }
}