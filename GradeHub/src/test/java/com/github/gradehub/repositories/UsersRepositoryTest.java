
package com.github.gradehub.repositories;

import com.github.gradehub.entities.Users;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersRepositoryTest extends BaseTestSetup {

    @Test
    public void testFindByEmail() {
        Optional<Users> retrievedUser = usersRepository.findByEmail(professor.getEmail());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getPersonFirstName()).isEqualTo(professor.getPersonFirstName());
    }

    @Test
    public void testFindByNameAndLastName() {
        Optional<Users> retrievedUser = usersRepository.findByNameAndLastName(student1.getPersonFirstName(), student1.getPersonLastName());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo(student1.getEmail());
    }

    @Test
    public void testFindUsersByCourseId() {
        List<Users> usersInCourse = usersRepository.findUsersByCourseId(course.getCourseId());

        assertThat(usersInCourse).hasSize(2);
        assertThat(usersInCourse).extracting("personFirstName")
                .containsExactlyInAnyOrder(student1.getPersonFirstName(), student2.getPersonFirstName());
    }
}


