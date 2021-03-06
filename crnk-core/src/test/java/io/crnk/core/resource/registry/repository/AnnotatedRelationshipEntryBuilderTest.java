package io.crnk.core.resource.registry.repository;

import io.crnk.core.mock.models.Project;
import io.crnk.core.mock.models.Task;
import io.crnk.core.module.ModuleRegistry;
import io.crnk.legacy.locator.JsonServiceLocator;
import io.crnk.legacy.locator.SampleJsonServiceLocator;
import io.crnk.legacy.registry.AnnotatedRelationshipEntryBuilder;
import io.crnk.legacy.registry.RepositoryInstanceBuilder;
import io.crnk.legacy.repository.annotations.JsonApiRelationshipRepository;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class AnnotatedRelationshipEntryBuilderTest {

	private ModuleRegistry moduleRegistry = new ModuleRegistry();


	@Test
	public void onInstanceOfAnnotatedRelationshipRepositoryShouldReturnTargetClass() {

		// GIVEN
		final AnnotatedRelationshipEntryBuilder builder = new AnnotatedRelationshipEntryBuilder(moduleRegistry,
				new RepositoryInstanceBuilder(new SampleJsonServiceLocator(), SimpleRelationshipRepository.class));


		// WHEN
		final String targetResourceType = builder.getTargetResourceType();

		// THEN
		assertThat(targetResourceType).isEqualTo("projects");
	}

	@Test
	public void onInstanceOfAnonymousDescendantOfAnnotatedRelationshipRepositoryShouldReturnTargetClass() {

		// GIVEN
		final AnnotatedRelationshipEntryBuilder builder = new AnnotatedRelationshipEntryBuilder(moduleRegistry,
				new RepositoryInstanceBuilder(new JsonServiceLocator() {
					@Override
					public <T> T getInstance(Class<T> clazz) {
						return (T) new SimpleRelationshipRepository() {
						};
					}
				}, SimpleRelationshipRepository.class)
		);

		// WHEN
		final String targetResourceType = builder.getTargetResourceType();

		// THEN
		assertThat(targetResourceType).isEqualTo("projects");

	}

	@Test
	public void onInstanceOfNonAnnotatedClassShouldThrowIllegalArgumentException() {

		// GIVEN
		final AnnotatedRelationshipEntryBuilder builder = new AnnotatedRelationshipEntryBuilder(moduleRegistry,
				new RepositoryInstanceBuilder(new JsonServiceLocator() {
					@Override
					public <T> T getInstance(Class<T> clazz) {
						return (T) new Object();
					}
				}, SimpleRelationshipRepository.class)
		);

		// WHEN
		try {
			builder.getTargetResourceType();
		} catch (Exception e) {
			// THEN
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
		}
	}

	@JsonApiRelationshipRepository(source = Task.class, target = Project.class)
	public static class SimpleRelationshipRepository {

	}

}
