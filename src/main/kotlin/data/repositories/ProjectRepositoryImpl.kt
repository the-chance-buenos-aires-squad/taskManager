package data.repositories

import data.dataSource.ProjectDataSource
import domain.repositories.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
): ProjectRepository {
}