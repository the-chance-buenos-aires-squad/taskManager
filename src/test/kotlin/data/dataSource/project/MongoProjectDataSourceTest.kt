package data.dataSource.project

import com.google.common.truth.Truth.assertThat
import data.dto.ProjectDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class MongoProjectDataSourceTest {

 private lateinit var dataSource: FakeMongoProjectDataSource
 private lateinit var testProject: ProjectDto

 @BeforeEach
 fun setUp() {
  dataSource = FakeMongoProjectDataSource()
  testProject = ProjectDto(
   id = UUID.randomUUID().toString(),
   name = "PlanMate",
   description = "CLI-based Project Manager",
   createdAt = LocalDateTime.now().toString()
  )
 }

 @Test
 fun `addProject should return true and allow retrieval by id`() = runTest {
  val added = dataSource.addProject(testProject)
  assertThat(added).isTrue()

  val result = dataSource.getProjectById(UUID.fromString(testProject.id))
  assertThat(result).isEqualTo(testProject)
 }

 @Test
 fun `getAllProjects should return all added projects`() = runTest {
  val project2 = testProject.copy(id = UUID.randomUUID().toString(), name = "SecondProject")
  dataSource.addProject(testProject)
  dataSource.addProject(project2)

  val allProjects = dataSource.getAllProjects()
  assertThat(allProjects).hasSize(2)
  assertThat(allProjects).containsExactly(testProject, project2)
 }

 @Test
 fun `deleteProject should remove the project by id`() = runTest {
  dataSource.addProject(testProject)

  val deleted = dataSource.deleteProject(UUID.fromString(testProject.id))
  assertThat(deleted).isTrue()

  val result = dataSource.getProjectById(UUID.fromString(testProject.id))
  assertThat(result).isNull()
 }

 @Test
 fun `updateProject should modify existing project`() = runTest {
  dataSource.addProject(testProject)

  val updatedProject = testProject.copy(name = "Updated Project")
  val updated = dataSource.updateProject(updatedProject)
  assertThat(updated).isTrue()

  val result = dataSource.getProjectById(UUID.fromString(testProject.id))
  assertThat(result?.name).isEqualTo("Updated Project")
 }

 @Test
 fun `getProjectById should return null for nonexistent id`() = runTest {
  val result = dataSource.getProjectById(UUID.randomUUID())
  assertThat(result).isNull()
 }
}