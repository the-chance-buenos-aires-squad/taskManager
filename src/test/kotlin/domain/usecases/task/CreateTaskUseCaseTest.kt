/*
package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.entities.Task
import domain.repositories.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
//TODO FIX ME
class CreateTaskUseCaseTest {

    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private lateinit var addTaskUseCase: AddTaskUseCase
    private val validTitle = "Valid Task Title"
    private val validDescription = "Valid Task Description"
    private val validProjectId = UUID.randomUUID()
    private val validStateId = UUID.randomUUID()
    private val validAssignedToId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        addTaskUseCase = AddTaskUseCase(taskRepository)
    }

    @Test
    fun `should create task successfully when all parameters are valid`() = runTest {
        coEvery { taskRepository.addTask(any()) } returns true

        val result = addTaskUseCase
            .execute(
                UUID.randomUUID(),
                validTitle,
                validDescription,
                validProjectId,
                validStateId,
                validAssignedToId,
            )

        assertThat(result).isTrue()
        coVerify(exactly = 1) { taskRepository.addTask(any()) }
    }

    @Test
    fun `should return false when create task unSuccessfully`() = runTest {
        coEvery { taskRepository.addTask(any()) } returns false
        val result = addTaskUseCase
            .execute(
                UUID.randomUUID(),
                validTitle,
                validDescription,
                validProjectId,
                validStateId,
                validAssignedToId
            )

        assertThat(result).isFalse()
        coVerify(exactly = 1) { taskRepository.addTask(any()) }
    }

    @Test
    fun `should throw TaskTitleEmptyException when title is empty`() = runTest {
        assertThrows<TaskTitleEmptyException> {
            addTaskUseCase.execute(
                UUID.randomUUID(),
                " ",
                validDescription,
                validProjectId,
                validStateId,
                validAssignedToId,
            )
        }
    }

    @Test
    fun `should throw TaskDescriptionEmptyException when description is empty`() = runTest {
        assertThrows<TaskDescriptionEmptyException> {
            addTaskUseCase.execute(
                UUID.randomUUID(),
                validTitle,
                "",
                validProjectId,
                validStateId,
                validAssignedToId,
            )
        }
    }

    @Test
    fun `should throw InvalidProjectIdException when projectId is zero`() = runTest {
        assertThrows<InvalidProjectIdException> {
            addTaskUseCase.execute(
                UUID.randomUUID(),
                validTitle,
                validDescription,
                UUID(0, 0),
                validStateId,
                validAssignedToId,
            )
        }
    }

    @Test
    fun `should use provided stateId when stateId is given`() = runTest {
        val taskSlot = slot<Task>()
        coEvery { taskRepository.addTask(capture(taskSlot)) } returns true

        addTaskUseCase.execute(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
        )

        assertThat(taskSlot.captured.stateId).isEqualTo(validStateId)
    }

    @Test
    fun `should use provided assignedTo when assignedTo is given`() = runTest {
        val taskSlot = slot<Task>()
        coEvery { taskRepository.addTask(capture(taskSlot)) } returns true

        addTaskUseCase.execute(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
        )

        assertThat(taskSlot.captured.assignedTo).isEqualTo(validAssignedToId)
    }

    @Test
    fun `should set assignedTo to null when assignedTo is not provided`() = runTest {
        val taskSlot = slot<Task>()
        coEvery { taskRepository.addTask(capture(taskSlot)) } returns true

        addTaskUseCase.execute(
            id = UUID.randomUUID(),
            title = validTitle,
            description = validDescription,
            projectId = validProjectId,
            stateId = validStateId,
        )

        assertThat(taskSlot.captured.assignedTo).isNull()
    }

    @Test
    fun `should return true when task is successfully created`() = runTest {
        coEvery { taskRepository.addTask(any()) } returns true

        val result = addTaskUseCase.execute(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
        )

        assertThat(result).isTrue()
    }

    @Test
    fun `should allow title and description with leading or trailing spaces`() = runTest {
        val trimmedTitle = "  Valid Title  "
        val trimmedDescription = "  Valid Description  "
        val taskSlot = slot<Task>()
        coEvery { taskRepository.addTask(capture(taskSlot)) } returns true

        addTaskUseCase.execute(
            UUID.randomUUID(),
            trimmedTitle,
            trimmedDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
        )

        coVerify { taskRepository.addTask(any()) }
        assertThat(taskSlot.captured.title).isEqualTo(trimmedTitle)
        assertThat(taskSlot.captured.description).isEqualTo(trimmedDescription)
    }

    @Test
    fun `should throw TaskTitleEmptyException when title is only whitespace characters`() = runTest {
        assertThrows<TaskTitleEmptyException> {
            addTaskUseCase.execute(
                UUID.randomUUID(),
                "\n\t",
                validDescription,
                validProjectId,
                validStateId,
                validAssignedToId,
            )
        }
    }

    @Test
    fun `should throw TaskDescriptionEmptyException when description is only whitespace characters`() = runTest {
        assertThrows<TaskDescriptionEmptyException> {
            addTaskUseCase.execute(
                UUID.randomUUID(),
                validTitle,
                "\t\t  ",
                validProjectId,
                validStateId,
                validAssignedToId,
            )
        }
    }

    @Test
    fun `should accept valid manually created UUID for projectId`() = runTest {
        val manualUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        val taskSlot = slot<Task>()
        coEvery { taskRepository.addTask(capture(taskSlot)) } returns true

        addTaskUseCase.execute(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            manualUUID,
            validStateId,
            validAssignedToId,
        )

        assertThat(taskSlot.captured.projectId).isEqualTo(manualUUID)
    }

}*/
